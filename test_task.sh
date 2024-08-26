#!/bin/bash

OUTPUT_DIR="./bin/"
SRC_DIR="./src/"

echo "Compiling Java files..."
mkdir -p $OUTPUT_DIR
javac -d $OUTPUT_DIR $SRC_DIR/server/Server.java $SRC_DIR/client/Client1.java $SRC_DIR/client/Client2.java

echo "Starting Server..."
java -cp $OUTPUT_DIR server.Server &
SERVER_PID=$!

sleep 1

echo "Starting Client1..."
java -cp $OUTPUT_DIR client.Client1 &
CLIENT1_PID=$!

sleep 1

echo "Starting Client2..."
java -cp $OUTPUT_DIR client.Client2 &
CLIENT2_PID=$!

wait $CLIENT1_PID
wait $CLIENT2_PID
wait $SERVER_PID

echo "All processes finished."
