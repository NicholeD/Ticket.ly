#!/usr/bin/env sh
set -e

TICKET_STORAGE_DYNAMODB_CONTAINER_NAME="ticket-storage-dynamodb"

TICKET_STORAGE_CONTAINER=$(docker ps -a -f "name=${TICKET_STORAGE_DYNAMODB_CONTAINER_NAME}" -q)
echo "TICKET_STORAGE_CONTAINER"

if [ ! -z "$TICKET_STORAGE_CONTAINER" ]; then
    echo "Removing container: ${TICKET_STORAGE_DYNAMODB_CONTAINER_NAME}"
    docker rm -f ${TICKET_STORAGE_CONTAINER}
fi

echo "Running container: ${TICKET_STORAGE_DYNAMODB_CONTAINER_NAME}"
docker run --name ${TICKET_STORAGE_DYNAMODB_CONTAINER_NAME} -p 8000:8000 -d amazon/dynamodb-local

echo "Waiting 10 seconds for dynamodb to boot..."
sleep 10