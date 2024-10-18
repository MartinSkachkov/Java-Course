public class TextJustifier {
 
    public static String[] justifyText(String[] words, int maxWidth) {
       int lineCount = 0;
       int currentWidth = 0;
 
       for (String word : words) {
           if (currentWidth + word.length() > maxWidth){
               lineCount++;
               currentWidth = word.length() + 1;
           }else{
               currentWidth += word.length() + 1;
 
           }
       }
       if (currentWidth > 0) {
           lineCount++;
       }
 
       String[] justifiedText = new String[lineCount];
       int position = 0;
       int lineLength, j, i = 0;
 
       while (i < words.length){
           j = i + 1;
           lineLength = words[i].length();
 
           while (j < words.length && lineLength + words[j].length() + 1 <= maxWidth) {
               lineLength += words[j].length() + 1;
               j++;
           }
 
           int wordsInLine = j - i;
           StringBuilder sb = new StringBuilder();
 
           if (wordsInLine == 1 || j == words.length) {
               // За редове с една дума или последния ред - ляво подравняване
               for (int k = i; k < j; k++) {
                   sb.append(words[k]);
                   if (k < j - 1) {
                       sb.append(" ");
                   }
               }
 
               while (sb.length() < maxWidth) {
                   sb.append(" ");
               }
           } else {
               // За останалите редове - разпределяме интервалите
                int totalSpaces = maxWidth - (lineLength - (wordsInLine - 1));
                int spacesPerGap = totalSpaces / (wordsInLine - 1);
                int extraSpaces = totalSpaces % (wordsInLine - 1);
 
                for (int k = i; k < j; k++) {
                    sb.append(words[k]);
                    if (k < j - 1) {
                        int spaces = spacesPerGap + (extraSpaces > 0 ? 1 : 0);
                        sb.append(" ".repeat(spaces));
                        extraSpaces--;
                    }
                }
           }
 
           justifiedText[position] = sb.toString();
           position++;
           i = j;
       }
 
       return justifiedText;
    }
 
    public static void main(String[] args) {
        String[] words1 = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."};
        int maxWidth1 = 11;
 
        String[] result1 = justifyText(words1, maxWidth1);
        for (String line : result1) {
            System.out.println('"' + line + '"');
        }
 
        System.out.println();
 
        String[] words2 = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        int maxWidth2 = 20;
 
        System.out.println("Пример 2:");
        String[] result2 = justifyText(words2, maxWidth2);
        for (String line : result2) {
            System.out.println('"' + line + '"');
        }
    }
}