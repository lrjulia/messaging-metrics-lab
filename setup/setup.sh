#!/bin/bash
rabbitmqctl set_permissions -p / guest ".*" ".*" ".*"

rabbitmq-plugins enable rabbitmq_management