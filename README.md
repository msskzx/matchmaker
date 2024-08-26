# Multi-Threaded Matchmaker

## Motivation

The motivation is to build a turn-based game where two players compete against each other.

## Objective

The objective is to build a multi-threaded server that acts as a matchmaker to accept connections from players. When two players are queued, it creates a game for them.

## Proof of Concept

As a proof of concept, a client-server architecture is used where the server class accepts an incoming connection from client 1 and then from client 2. The server then waits for client 1's move and forwards it to client 2, and similarly for client 2.

## Methodology

This is achieved using a Client-Server architecture where the player connects to the matchmaker server, and the server handles communication between players. Various components in this architecture are designed to achieve loose coupling as much as possible. The Observer pattern is used to broadcast changes from the game to each player.

### Server-Side Components

- **MatchMakerServer**: Responsible for accepting connections and forwarding communication between players, the matchmaker, and the game. This class holds a Matchmaker instance and a concurrent hash map of player clients.
- **Matchmaker**: Responsible for maintaining a queue of players, adding new players to the queue, creating games when enough players are available, and notifying players when a new game is created. It can also keep track of running games.
- **Game**: Manages the game between two players, handles turns, notifies players about their turn, and updates the game state using the incoming player move if it is their turn.

### Client-Side Components

- **Player**: Handles player actions like queueing, playing, and processing notifications from the PlayerClient.
- **PlayerClient**: Forwards communication between the player and the server.

## Running

To test the proof of concept:
```bash
./test_task.sh
```

Output:
```
Compiling Java files...
Starting Server...
Server is waiting for connections...
Starting Client1...
Client 1 connected to server.
Client 1 connected.
Starting Client2...
Client 2 connected.
Client 2 connected to server.
Received: 1 
Received: 1 2 
Received: 1 2 3 
Received: 1 2 3 4 
Received: 1 2 3 4 5 
Received: 1 2 3 4 5 6 
Received: 1 2 3 4 5 6 7 
Received: 1 2 3 4 5 6 7 8 
Received: 1 2 3 4 5 6 7 8 9 
Received: 1 2 3 4 5 6 7 8 9 10 
Received: 1 2 3 4 5 6 7 8 9 10 11 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 
Received: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 
```


To test the matchmaker:
```bash
./test_matchmaker.sh
```

Output, however matchmaker still needs some synchronization adjustments:
```
Compiling Java files...
Starting Server...
Listening for connections...
Starting Player1...
Started player Client 1
Queueing
Listening for connections...
Waiting for player ID...
New player connected with ID: 1
Added player 1 to queue
Queue size: 1
Starting Player2...
Listening for connections...
Waiting for player ID...
Started player Client 2
Queueing
New player connected with ID: 2
Added player 2 to queue
Queue size: 2
Creating a new game
Created a new Game for player 1 and 2
Game Started!
Game Started!
Game Started!
Opponent goes first!
Game Started!
You go first!
Received: 1
Received: 1 2
Received: 1 2 3
Received: 1 2 3 4
Received: 1 2 5
Received: 1 2 5 6
Received: 1 2 3 4 7
Received: 1 2 3 4 7 8
Received: 1 2 3 4 7 8 9
Received: 1 2 3 4 7 8 9 10
Received: 1 2 3 4 7 8 11
Received: 1 2 3 4 7 8 11 12
Received: 1 2 3 4 7 8 11 12 13
Received: 1 2 3 4 7 8 11 12 13 14
Received: 1 2 3 4 7 8 11 12 15
Received: 1 2 3 4 7 8 11 12 13 16
Received: 1 2 3 4 7 8 11 12 13 14 17
Received: 1 2 3 4 7 8 11 12 15 18
Received: 1 2 3 4 7 8 11 12 15 18 19
Received: 1 2 3 4 7 8 11 12 15 18 19 20
```

## Limitations

The Game class could benefit from an additional class like GameClient, which would handle communication with the two player clients independently of the server. Another option could be using a hybrid peer-to-peer setup.
