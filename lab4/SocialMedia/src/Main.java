import bg.sofia.uni.fmi.mjt.socialnetwork.SocialNetworkImpl;
import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Set;

public class Main {
    public static void main(String[] args) throws UserRegistrationException {
        SocialNetworkImpl socialNetworkk = new SocialNetworkImpl();

        UserProfile user1 = new DefaultUserProfile("user1");
        user1.addInterest(Interest.SPORTS);
        user1.addInterest(Interest.TRAVEL);

        UserProfile user2 = new DefaultUserProfile("user2");
        user2.addInterest(Interest.SPORTS);
        user2.addInterest(Interest.FOOD);

        UserProfile user3 = new DefaultUserProfile("user3");
        user3.addInterest(Interest.TRAVEL);
        user3.addInterest(Interest.FOOD);

        UserProfile user4 = new DefaultUserProfile("user4");
        user4.addInterest(Interest.TRAVEL);
        user4.addInterest(Interest.GAMES);

        try {
            socialNetworkk.registerUser(user1);
            socialNetworkk.registerUser(user2);
            socialNetworkk.registerUser(user3);
            socialNetworkk.registerUser(user4);
        } catch (UserRegistrationException e) {
            System.out.println(e.getMessage());
        }

        // Add some friends
        user1.addFriend(user2);
        user1.addFriend(user3);
        user2.addFriend(user4);

        // Post something

        Post post = socialNetworkk.post(user1, "Enjoying the outdoors!");

        // Get the users reached by the post
        Set<UserProfile> reachedUsers = socialNetworkk.getReachedUsers(post);

        // Print the reached users
        System.out.println("Users reached by the post:");
        for (UserProfile reachedUser : reachedUsers) {
            System.out.println(reachedUser.getUsername());
        }
    }
}