
public class JumpGame {

    public static boolean canWin(int[] array) {
        int maxReachable = 0;

        for (int i = 0; i < array.length; i++) {
            if (i > maxReachable) {
                return false;
            }

            maxReachable = Math.max(maxReachable, i + array[i]);

            if (maxReachable >= array.length - 1) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(canWin(new int[]{2, 3, 1, 1, 0}));
        System.out.println(canWin(new int[]{3, 2, 1, 0, 0})); 
    }
}
