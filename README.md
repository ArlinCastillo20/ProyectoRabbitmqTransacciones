
Proyecto: Procesamiento de Transacciones con RabbitMQ

Nombre: Arlin Guisel Castillo Cermeño 
Carné: 0905-24-22303  
Curso: Programación III  


 Descripción del Proyecto:
Este proyecto implementa un sistema distribuido en Java utilizando RabbitMQ para procesar transacciones bancarias.

El sistema sigue el patrón Producer–Consumer para desacoplar la generacion de transacciones del procesamiento final.

 Arquitectura:

API (GET) → Producer → RabbitMQ → Consumer → API (POST)

1. El Producer obtiene un lote de transacciones desde una API externa.
2. Cada transacción se envía a una cola según el banco destino.
3. RabbitMQ distribuye los mensajes.
4. El Consumer recibe cada transacción.
5. El Consumer agrega los datos del estudiante.
6. El Consumer envía la transacción al endpoint POST.

 Tecnologías utilizadas

- Java 17
- Maven
- RabbitMQ
- Jackson (JSON)
- HttpClient


 Funcionamiento

1.Iniciar RabbitMQ
2. Ejecutar ProducerApp para enviar las transacciones a las colas.
3. Ejecutar ConsumerApp para consumir los mensajes.
5. El consumer procesa las transacciones y las envía al API.

Resultado

El sistema envía correctamente las transacciones al endpoint y RabbitMQ confirma los mensajes mediante ACK.