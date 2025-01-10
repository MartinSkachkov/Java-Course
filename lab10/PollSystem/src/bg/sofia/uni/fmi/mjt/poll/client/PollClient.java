package bg.sofia.uni.fmi.mjt.poll.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PollClient {
    private final String host;
    private final int port;

    public PollClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendCommand(String command) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            writer.println(command);
            return reader.readLine();
        }
    }
}