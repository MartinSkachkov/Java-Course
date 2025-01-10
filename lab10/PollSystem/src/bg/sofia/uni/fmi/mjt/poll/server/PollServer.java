package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PollServer {
    private static final int TIME_LIMIT = 60;
    private final int port;
    private final PollRepository pollRepository;
    private volatile boolean isRunning;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public PollServer(int port, PollRepository pollRepository) {
        this.port = port;
        this.pollRepository = pollRepository;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newCachedThreadPool();
            System.out.println("Server started and listening for connect requests");

            isRunning = true;
            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, pollRepository);
                    executorService.execute(clientHandler);
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }

    public void stop() {
        isRunning = false;

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }

            if (executorService != null) {
                executorService.shutdown();

                if (!executorService.awaitTermination(TIME_LIMIT, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while stopping server", e);
        }
    }
}