#!/bin/bash

pushd todo-cli
./gradlew eclipse
popd

pushd todo-microservice
./gradlew eclipse
popd
