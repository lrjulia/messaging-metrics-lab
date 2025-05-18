#!/bin/bash

set -e

echo "🔨 Building publisher..."
cd publisher
./mvnw clean package -DskipTests

echo "🔨 Building rabbitmq agent..."
cd ../rabbitmq-agent
./mvnw clean package -DskipTests

echo "🔨 Building kafka agent..."
cd ../kafka-agent
./mvnw clean package -DskipTests

echo "🐳 Starting Docker Compose..."
cd ..
docker-compose up --build -d