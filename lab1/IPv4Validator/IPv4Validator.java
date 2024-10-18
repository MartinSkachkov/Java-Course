
public class IPv4Validator {
    
    public static boolean validOctet(String octet) {
        if (octet.isBlank()) {
            return false;
        }

        if (octet.startsWith("0") && octet.length() > 1) {
            return false;
        }

        int intOctet;
        for (char c : octet.toCharArray()) {
            if (c < '0' || c > '9'){
                return false;
            }
        }
        intOctet = Integer.parseInt(octet);

        return intOctet < 256;
    }

    public static boolean validateIPv4Address(String ip) {
        String[] octets = ip.split("\.");

        if (octets.length != 4){
            return false;
        }

        for (String octet : octets) {
            if (!validOctet(octet)){
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(validateIPv4Address("192.168.1.1"));
        System.out.println(validateIPv4Address("192.168.1.0"));
        System.out.println(validateIPv4Address("192.168.1.00"));
        System.out.println(validateIPv4Address("192.168@1.1"));
    }
}
