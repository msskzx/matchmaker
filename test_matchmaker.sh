#!/bin/bash

OUTPUT_DIR="./bin/"
SRC_DIR="./src/"

echo "Compiling Java files..."
mkdir -p $OUTPUT_DIR
javac -d $OUTPUT_DIR $SRC_DIR/matchmaker/ServerMain.java $SRC_DIR/player/Player1.java $SRC_DIR/player/Player2.java

echo "Starting Server..."
java -cp $OUTPUT_DIR matchmaker.ServerMain &
SERVER_PID=$!

sleep 1

echo "Starting Player1..."
java -cp $OUTPUT_DIR player.Player1 &
PLAYER1_PID=$!

sleep 1

echo "Starting Player2..."
java -cp $OUTPUT_DIR player.Player2 &
PLAYER2_PID=$!

sleep 1

kill $PLAYER1_PID
kill $PLAYER2_PID
kill $SERVER_PID

echo "All processes finished."
