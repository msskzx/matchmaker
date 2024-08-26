#!/bin/bash

OUTPUT_DIR="./bin/"
SRC_DIR="./src/"

echo "Compiling Java files..."
mkdir -p $OUTPUT_DIR
javac -d $OUTPUT_DIR $SRC_DIR/server/Server.java $SRC_DIR/client/Clients12.java

echo "Starting Server..."
java -cp $OUTPUT_DIR server.Server &
SERVER_PID=$!

sleep 1

echo "Starting Client1 and Client2..."
java -cp $OUTPUT_DIR client.Clients12 &
CLIENTS12_PID=$!

sleep 1

wait $CLIENTS12_PID
wait $SERVER_PID

echo "All processes finished."
