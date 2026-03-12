package com.Arli.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProducerApp {

    private static final String GET_URL =
            "https://hly784ig9d.execute-api.us-east-1.amazonaws.com/default/transacciones";

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando Producer...");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GET_URL))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Error al consumir GET. Código: " + response.statusCode());
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            LoteResponse lote = mapper.readValue(response.body(), LoteResponse.class);

            if (lote.transacciones == null || lote.transacciones.isEmpty()) {
                System.out.println("No hay transacciones en el lote.");
                return;
            }

            System.out.println("Lote recibido: " + lote.loteId);
            System.out.println("Cantidad de transacciones: " + lote.transacciones.size());

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                for (Transaccion tx : lote.transacciones) {

                    if (tx.bancoDestino == null || tx.bancoDestino.isBlank()) {
                        System.out.println("Transacción sin banco destino: " + tx.idTransaccion);
                        continue;
                    }

                    String queueName = tx.bancoDestino.trim();

                    channel.queueDeclare(queueName, true, false, false, null);

                    String json = mapper.writeValueAsString(tx);

                    channel.basicPublish("", queueName, null, json.getBytes());

                    System.out.println("Enviada " + tx.idTransaccion + " a cola " + queueName);
                }
            }

            System.out.println("Producer finalizado correctamente.");

        } catch (Exception e) {
            System.out.println("Error en Producer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}