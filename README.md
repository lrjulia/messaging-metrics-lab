# Messaging Metrics Lab

This repository contains a small lab environment with RabbitMQ, Kafka and monitoring tools such as Prometheus and Grafana. A few Spring Boot services are provided to publish and consume messages so you can experiment locally.

## Requirements

- **Docker** and the Docker **compose** plugin.
- **Java 21** for running the Maven wrapper used by the services.

Maven itself does not need to be installed because each module includes the `mvnw` wrapper script.

## Getting started
1. Clone this repository.
2. Create a file named `.env` inside the `publisher` directory containing your AWS credentials. For example:

   ```env
   AWS_ACCESS_KEY_ID=YOUR_ACCESS_KEY
   AWS_SECRET_ACCESS_KEY=YOUR_SECRET
   AWS_REGION=YOUR_AWS_REGION
   SQS_URL=YOUR_QUEUE_URL
   ```

3. From the project root execute:

   ```bash
   ./start.sh
   ```

The `start.sh` script builds all modules and then starts the Docker Compose stack. After it finishes you can access the monitoring tools at the following URLs:

| Tool        | URL                      | Default Login |
|-------------|--------------------------|---------------|
| RabbitMQ UI | <http://localhost:15672> | guest:guest   |
| Prometheus  | <http://localhost:9090>  |               |
| Grafana     | <http://localhost:3000>  | admin:admin   |