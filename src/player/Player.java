package player;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

class Player {

    /*
     * Responsible for player actions like queueing, playing and handling 
     * notifications coming from the PlayerClient
     */

    private PlayerClient playerClient;
    private final int id;
    private volatile boolean turn;
    private final AtomicReference<Status> status;
    private volatile String prevMove;

    Player(int id, String serverAddress, int serverPort) {
        this.id = id;
        this.status = new AtomicReference<>(Status.IDLE);
        this.turn = false;
        this.prevMove = "";

        try {
            this.playerClient = new PlayerClient(serverAddress, serverPort, this);
            this.playerClient.start();
            while (!this.playerClient.isInit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void queue() {
        this.status.set(Status.IN_QUEUE);
        this.playerClient.queuePlayer();
    }

    synchronized void play(String move) {
        this.playerClient.play(move);
        this.turn = false;
    }

    synchronized void notifyGameStarted(boolean turn) {
        this.status.set(Status.IN_GAME);
        this.turn = turn;
    }

    synchronized void notifyGameOver() {
        this.status.set(Status.IDLE);
    }

    synchronized void notifyTurn(String prevMove) {
        this.turn = true;
        this.prevMove = prevMove;
    }

    boolean canPlay() {
        return this.inGame() && this.turn;
    }

    boolean inGame() {
        return this.status.get() == Status.IN_GAME;
    }

    int getId() {
        return this.id;
    }

    Status getStatus() {
        return this.status.get();
    }

    String getPrevMove() {
        return this.prevMove;
    }
}
