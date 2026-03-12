package com.Arli.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsumerApp {

    private static final String QUEUE_NAME = "GYT";
    private static final String POST_URL =
            "https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("Esperando mensajes en cola: " + QUEUE_NAME);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            long tag = delivery.getEnvelope().getDeliveryTag();
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            try {
                Transaccion tx = mapper.readValue(message, Transaccion.class);

                tx.nombre = "Arlin Guisel Castillo Cermeño";
                tx.carnet = "0905-24-22303";
                tx.correo = "acastilloc31@miumg.edu.gt";

                String jsonPost = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tx);

                 System.out.println("JSON que se enviará al POST:");
                 System.out.println(jsonPost);
              
                  HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(POST_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPost))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    channel.basicAck(tag, false);
                    System.out.println("POST correcto, ACK enviado: " + tx.idTransaccion);
                } else {
                    System.out.println("POST falló, se reencola. Código: " + response.statusCode());
                    channel.basicNack(tag, false, true);
                }

            } catch (Exception e) {
                System.out.println("Error procesando mensaje: " + e.getMessage());
                channel.basicNack(tag, false, true);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}