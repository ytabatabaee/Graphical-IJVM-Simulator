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

    // op = add | and | sub | neg | or | not
    // unary operations use data1
    public String operation(String data1, String data2, String op) {
        if (op.equals("not")) {
            String res = "";
            for (int i = 0; i < data1.length(); i++) {
                if (data1.charAt(i) == '0')
                    res += '1';
                else if (data1.charAt(i) == '1')
                    res += '0';
            }
            return res;
        } else if (op.equals("neg")) {
            String res = operation(data1, data2, "not");
            res = operation(res, "1", "add");
            return res;
        } else {
            int dataInt1 = binaryToInt(data1);
            int dataInt2 = binaryToInt(data2);
            int res = 0;
            if (op.equals("add"))
                res = dataInt1 + dataInt2;
            else if (op.equals("sub"))
                res = dataInt1 - dataInt2;
            else if (op.equals("and"))
                res = dataInt1 & dataInt2;
            else if (op.equals("or"))
                res = dataInt1 | dataInt2;
            return intToBinary(res);
        }
    }
}