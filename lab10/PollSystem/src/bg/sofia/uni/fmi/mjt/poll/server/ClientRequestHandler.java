package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientRequestHandler implements Runnable {
    private static final int MINIMUM_POLL_PARTS = 4;
    private static final int EXPECTED_SUBMIT_VOTE_PARTS = 3;

    private final Socket clientSocket;
    private final PollRepository pollRepository;

    public ClientRequestHandler(Socket socket, PollRepository repository) {
        this.clientSocket = socket;
        this.pollRepository = repository;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Client Request Handler for " + clientSocket.getRemoteSocketAddress());

        try (
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                String response = handleCommand(inputLine);
                writer.println(response);

                if (inputLine.equals("disconnect")) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error handling client request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private String handleCommand(String command) {
        String[] parts = command.split("\\s+");

        if (parts.length == 0) {
            return createErrorResponse("Invalid command");
        }

        return switch (parts[0]) {
            case "create-poll" -> handleCreatePoll(parts);
            case "list-polls" -> handleListPolls();
            case "submit-vote" -> handleSubmitVote(parts);
            case "disconnect" -> createSuccessResponse("Disconnecting");
            default -> createErrorResponse("Unknown command");
        };
    }

    private String handleCreatePoll(String[] parts) {
        if (parts.length < MINIMUM_POLL_PARTS) {
            return createErrorResponse("Usage: create-poll <question> <option-1> <option-2> [... <option-N>]");
        }

        String question = parts[1].trim();
        if (question.isBlank()) {
            return createErrorResponse("Question cannot be blank");
        }

        Map<String, Integer> options = new HashMap<>();

        for (int i = 2; i < parts.length; i++) {
            if (parts[i].isBlank()) {
                return createErrorResponse("Option cannot be blank");
            }

            options.put(parts[i].trim(), 0);
        }

        Poll poll = new Poll(question, options);
        int pollId = pollRepository.addPoll(poll);
        return createSuccessResponse("Poll " + pollId + " created successfully.");
    }

    private String handleListPolls() {
        Map<Integer, Poll> polls = pollRepository.getAllPolls();
        if (polls.isEmpty()) {
            return createErrorResponse("No active polls available.");
        }

        StringBuilder response = new StringBuilder();
        response.append("{\"status\":\"OK\",\"polls\":{");

        for (Map.Entry<Integer, Poll> entry : polls.entrySet()) {
            int pollId = entry.getKey();
            Poll poll = entry.getValue();

            response.append("\"").append(pollId).append("\":{\"question\":\"")
                    .append(poll.question()).append("\",\"options\":{");

            for (Map.Entry<String, Integer> option : poll.options().entrySet()) {
                response.append("\"").append(option.getKey()).append("\":").append(option.getValue()).append(",");
            }

            response.deleteCharAt(response.length() - 1);
            response.append("}},");
        }

        response.deleteCharAt(response.length() - 1);
        response.append("}}");

        return response.toString();
    }

    private String handleSubmitVote(String[] parts) {
        if (parts.length != EXPECTED_SUBMIT_VOTE_PARTS) {
            return createErrorResponse("Usage: submit-vote <poll-id> <option>");
        }

        try {
            int pollId = Integer.parseInt(parts[1]);
            String option = parts[2];

            Poll poll = pollRepository.getPoll(pollId);
            if (poll == null) {
                return createErrorResponse("Poll with ID " + pollId + " does not exist.");
            }

            Map<String, Integer> options = poll.options();
            if (!options.containsKey(option)) {
                return createErrorResponse("Invalid option. Option " + option + " does not exist.");
            }

            options.put(option, options.get(option) + 1);
            return createSuccessResponse("Vote submitted successfully for option: " + option);

        } catch (NumberFormatException e) {
            return createErrorResponse("Invalid poll ID format");
        }
    }

    private String createErrorResponse(String message) {
        return "{\"status\":\"ERROR\",\"message\":\"" + message + "\"}";
    }

    private String createSuccessResponse(String message) {
        return "{\"status\":\"OK\",\"message\":\"" + message + "\"}";
    }
}