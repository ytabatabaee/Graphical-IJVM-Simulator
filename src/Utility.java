public class Utility {
    public int binaryToInt(String binary) {
        String binaryDigits = "01";
        int length = binary.length();
        int dec = 0;
        for (int i = 0; i < length; i++) {
            char c = binary.charAt(i);
            int d = binaryDigits.indexOf(c);
            dec = 2 * dec + d;
        }
        return dec;
    }

    public String intToBinary(int dec) {
        String res = "";
        String binary = Integer.toBinaryString(dec);
        int length = 32 - binary.length();
        char c = dec < 0 ? '1' : '0';
        for (int i = 0; i < length; i++)
            res += c;
        res += binary;
        return res;
    }


}