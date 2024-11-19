package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {
    private Set<UserProfile> users;
    private List<Post> posts;

    public SocialNetworkImpl() {
        this.users = new HashSet<>();
        this.posts = new ArrayList<>();
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        if (users.contains(userProfile)) {
            throw new UserRegistrationException("User is already registered");
        }

        users.add(userProfile);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("User is not registered");
        }

        Post post = new SocialFeedPost(userProfile, content);
        posts.add(post);
        return post;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableCollection(posts);
    }

    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        Set<UserProfile> reachedUsers = new HashSet<>();
        UserProfile author = post.getAuthor();

        Set<UserProfile> visited = new HashSet<>();
        visited.add(author);
        getReachedUsersRecursive(author, visited, reachedUsers, author);

        return reachedUsers;
    }

    private void getReachedUsersRecursive(UserProfile currentUser, Set<UserProfile> visited,
                                          Set<UserProfile> reachedUsers, UserProfile author) {

        for (UserProfile friend : currentUser.getFriends()) {
            if (!visited.contains(friend)) {
                visited.add(friend);

                if (hasCommonInterests(author, friend)) {
                    reachedUsers.add(friend);
                }

                getReachedUsersRecursive(friend, visited, reachedUsers, author);
            }
        }

    }

    private boolean hasCommonInterests(UserProfile user1, UserProfile user2) {
        Set<Interest> user1Interests = EnumSet.copyOf(user1.getInterests());
        user1Interests.retainAll(user2.getInterests());
        return !user1Interests.isEmpty();
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException {

        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("User profiles cannot be null");
        }

        if (!users.contains(userProfile1) || !users.contains(userProfile2)) {
            throw new UserRegistrationException("User is not registered");
        }

        Set<UserProfile> mutualFriends = new HashSet<>(userProfile1.getFriends());
        mutualFriends.retainAll(userProfile2.getFriends());
        return mutualFriends;

    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sortedProfiles = new TreeSet<>(new SortByFriendsCountComparator());
        sortedProfiles.addAll(users);
        return Collections.unmodifiableSortedSet(sortedProfiles);
    }
}
