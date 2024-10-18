
public class BrokenKeyboard {
    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        String[] words = message.split(" ");
        char[] keys = brokenKeys.toCharArray();
        boolean canWrtie;
        int counter = 0;

        for (String word : words) {
            canWrtie = true;

            for (char c : keys) {
                if (word.contains(Character.toString(c)) || word.isEmpty()) {
                    canWrtie = false;
                    break;
                }
            }

            if (canWrtie) {
                counter++;
            }
        }

        return counter;
    }

    public static void main(String[] args) {
        System.out.println(calculateFullyTypedWords("i love mjt", "qsf3o"));
        System.out.println(calculateFullyTypedWords("secret      message info      ", "sms"));
        System.out.println(calculateFullyTypedWords("dve po 2 4isto novi beli kecove", "o2sf"));
        System.out.println(calculateFullyTypedWords("     ", "asd"));
        System.out.println(calculateFullyTypedWords(" - 1 @ - 4", "s"));
    }
}
