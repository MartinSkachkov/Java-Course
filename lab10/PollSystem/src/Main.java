import bg.sofia.uni.fmi.mjt.poll.client.PollClient;
import bg.sofia.uni.fmi.mjt.poll.server.PollServer;
import bg.sofia.uni.fmi.mjt.poll.server.repository.InMemoryPollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

public class Main {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        // Start server in a separate thread
        PollRepository repository = new InMemoryPollRepository();
        PollServer server = new PollServer(PORT, repository);
        Thread serverThread = new Thread(() -> server.start());
        serverThread.start();

        try {
            PollClient client = new PollClient(HOST, PORT);

            // Create a poll
            System.out.println(client.sendCommand("create-poll What-is-your-favourite-xmas-movie? Home-Alone Die-Hard Elf The-Grinch"));

            // List all polls
            System.out.println(client.sendCommand("list-polls"));

            // Submit some votes
            System.out.println(client.sendCommand("submit-vote 1 Home-Alone"));
            System.out.println(client.sendCommand("submit-vote 1 Home-Alone"));
            System.out.println(client.sendCommand("submit-vote 1 Elf"));

            // Check results
            System.out.println(client.sendCommand("list-polls"));

            // Clean disconnect
            System.out.println(client.sendCommand("disconnect"));

            // Stop server
            server.stop();

        } catch (Exception e) {
            System.err.println("Error in client operations: " + e.getMessage());
        }
    }
}