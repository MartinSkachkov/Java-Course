package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPollRepository implements PollRepository {
    private final Map<Integer, Poll> polls = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public int addPoll(Poll poll) {
        int id = nextId.getAndIncrement();
        polls.put(id, poll);
        return id;
    }

    @Override
    public Poll getPoll(int pollId) {
        return polls.getOrDefault(pollId, null);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return new ConcurrentHashMap<>(polls);
    }

    @Override
    public void clearAllPolls() {
        polls.clear();
        nextId.set(1);
    }
}