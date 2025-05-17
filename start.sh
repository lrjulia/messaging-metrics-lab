#!/bin/bash

set -e

echo "🔨 Building publisher..."
cd rabbitmq-publisher
./mvnw clean package -DskipTests

echo "🔨 Building agent..."
cd ../rabbitmq-agent
./mvnw clean package -DskipTests

echo "🐳 Starting Docker Compose..."
cd ..
docker-compose up --build -d