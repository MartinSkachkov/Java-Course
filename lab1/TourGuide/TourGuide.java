public class BestSightseeingPair {
    public static int getBestSightseeingPairScore(int[] places) {
        int maxRating = 0;
        int currentRating;

        for (int i = 0; i < places.length; i++) {
            for (int j = i + 1; j < places.length; j++) {
                currentRating = places[i] + places[j] + i - j;

                if (currentRating > maxRating) {
                    maxRating = currentRating;
                }
            }
        }

        return maxRating;
    }

    public static void main(String[] args) {
        System.out.println(getBestSightseeingPairScore(new int[] {8, 1, 5, 2, 6})); // 11
        System.out.println(getBestSightseeingPairScore(new int[] {1, 2})); // 2
        System.out.println(getBestSightseeingPairScore(new int[] {10, 7, 14})); // 22
        System.out.println(getBestSightseeingPairScore(new int[] {0, 1, 2, 3, 1})); // 4
        System.out.println(getBestSightseeingPairScore(new int[] {4, 4, 4, 4})); // 7
    }
}