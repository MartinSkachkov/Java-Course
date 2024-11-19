package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {
    private String username;
    private Set<Interest> interests;
    private Set<UserProfile> friends;

    public DefaultUserProfile(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        this.username = username;
        this.interests = EnumSet.noneOf(Interest.class);
        this.friends = new HashSet<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }

        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }

        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        if (userProfile.equals(this)) {
            throw new IllegalArgumentException("Cannot add yourself as a friend");
        }

        if (friends.add(userProfile)) {
            userProfile.addFriend(this);
            return true;
        }

        return false;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        if (userProfile.equals(this)) {
            throw new IllegalArgumentException("Cannot remove yourself as a friend");
        }

        if (friends.remove(userProfile)) {
            userProfile.unfriend(this);
            return true;
        }

        return false;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        return friends.contains(userProfile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUserProfile that = (DefaultUserProfile) o;
        return Objects.equals(username, that.username); // Each user is guaranteed to have a unique username.
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
