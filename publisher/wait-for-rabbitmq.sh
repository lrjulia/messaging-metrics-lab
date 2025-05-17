#!/bin/bash
set -e

echo "⏳ Waiting for RabbitMQ..."

until nc -z rabbitmq 5672; do
  sleep 1
done

echo "✅ RabbitMQ is up - executing command"
exec "$@"