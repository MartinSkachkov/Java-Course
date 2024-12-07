import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String s = "Hello, World,";
        String[] parts = s.split(",");
        System.out.println(Arrays.toString(parts));
    }
}