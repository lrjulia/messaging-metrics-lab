#!/bin/bash

set -euo pipefail

echo "🧹 Stopping and cleaning up containers and volumes..."
docker compose down --remove-orphans || true

echo "🧹 Removing broken RabbitMQ/ZooKeeper/Kafka volumes..."
docker volume rm -f messaging-metrics-lab_rabbitmq_data || true
docker volume rm -f messaging-metrics-lab_zookeeper_data || true
docker volume rm -f messaging-metrics-lab_kafka_data || true

build_module() {
  local module=$1
  echo "🔨 Building $module..."
  pushd "$module" > /dev/null
  if [[ -x ./mvnw ]]; then
    ./mvnw clean package -DskipTests
  else
    echo "❌ No mvnw found in $module"
    exit 1
  fi
  popd > /dev/null
}

build_module publisher
build_module rabbitmq-agent
build_module kafka-agent
build_module sqs-agent

echo "📦 Checking available disk space..."
df -h /

echo "🐳 Starting Docker Compose..."
docker compose up --build -d