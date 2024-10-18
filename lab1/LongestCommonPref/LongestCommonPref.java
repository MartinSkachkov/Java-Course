public class LongestCommonPrefix {
    public static String getLongestCommonPrefix(String[] words) {
        if (words == null || words.length == 0 || words[0].isEmpty()) {
            return "";
        }

        if (words.length == 1) {
            return words[0];
        }

        StringBuilder prefix = new StringBuilder();

        for (int index = 0; index < words[0].length(); index++) {
            char commonChar = words[0].charAt(index);

            for(String word : words) {
                if (word == null || word.isEmpty() || word.length() == index || word.charAt(index) != commonChar ) {
                    return prefix.toString();
                }
            }

            prefix.append(commonChar);
        }

        return prefix.toString();
    }

    public static void main(String[] args) {
        System.out.println(getLongestCommonPrefix(new String[] {"flower", "flow", "flight"}));
        System.out.println(getLongestCommonPrefix(new String[] {"dog", "racecar", "car"}));
        System.out.println(getLongestCommonPrefix(new String[] {"cat"}));
        System.out.println(getLongestCommonPrefix(new String[] {"protein", "protest", "protozoa"}));
    }
}