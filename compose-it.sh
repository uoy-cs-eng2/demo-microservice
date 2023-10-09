#!/bin/bash

docker compose -f compose-it.yaml -p demo-microservice "$@"
