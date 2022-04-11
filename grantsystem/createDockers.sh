#!/bin/bash

mvn package

docker build . -t grantsystem-backend:latest

docker build ./grant-app -t grantsystem-frontend:latest