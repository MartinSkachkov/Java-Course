package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SocialFeedPost implements Post {
    private UserProfile author;
    private String content;
    private UUID uniqueID;
    private LocalDateTime publishedDate;
    private Map<UserProfile, ReactionType> userProfileToReaction;
    private Map<ReactionType, Set<UserProfile>> reactionToUserProfiles;

    public SocialFeedPost(UserProfile author, String content) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }

        this.author = author;
        this.content = content;
        this.uniqueID = UUID.randomUUID();
        this.publishedDate = LocalDateTime.now();
        this.userProfileToReaction = new HashMap<>();
        this.reactionToUserProfiles = new EnumMap<>(ReactionType.class);

        for (ReactionType reaction : ReactionType.values()) {
            reactionToUserProfiles.put(reaction, new HashSet<>());
        }
    }

    @Override
    public String getUniqueId() {
        return uniqueID.toString();
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedDate;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("UserProfile cannot be null");
        }

        if (reactionType == null) {
            throw new IllegalArgumentException("ReactionType cannot be null");
        }

        ReactionType hasPreviousReaction = userProfileToReaction.get(userProfile);

        if (hasPreviousReaction != null) {
            reactionToUserProfiles.get(hasPreviousReaction).remove(userProfile);
        }

        reactionToUserProfiles.get(reactionType).add(userProfile);
        userProfileToReaction.put(userProfile, reactionType);

        return hasPreviousReaction == null;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("UserProfile cannot be null");
        }

        ReactionType hasPreviousReaction = userProfileToReaction.get(userProfile);

        if (hasPreviousReaction != null) {
            reactionToUserProfiles.get(hasPreviousReaction).remove(userProfile);
            userProfileToReaction.remove(userProfile);
            return true;
        }

        return false;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        Map<ReactionType, Set<UserProfile>> nonEmptyReactions = new EnumMap<>(ReactionType.class);

        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactionToUserProfiles.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                nonEmptyReactions.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
            }
        }

        return Collections.unmodifiableMap(nonEmptyReactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("ReactionType cannot be null");
        }

        Set<UserProfile> reactionsSet = reactionToUserProfiles.get(reactionType);
        return reactionsSet != null ? reactionsSet.size() : 0;
    }

    @Override
    public int totalReactionsCount() {
        int totalCount = 0;

        for (Set<UserProfile> users : reactionToUserProfiles.values()) {
            totalCount += users.size();
        }

        return totalCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialFeedPost that = (SocialFeedPost) o;
        return Objects.equals(uniqueID, that.uniqueID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueID);
    }
}
