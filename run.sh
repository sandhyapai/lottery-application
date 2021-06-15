#!/usr/bin/env bash

echo "Creating mvn build"
mvn clean install

cp target/lottery-application-0.0.1-SNAPSHOT.jar docker

cd docker

docker-compose up --build
