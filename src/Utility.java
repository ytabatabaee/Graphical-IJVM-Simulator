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

    public String binaryToHex(String binary) {
        StringBuilder hex = new StringBuilder("0x");
        return hex.append(Integer.toHexString(binaryToInt(binary))).toString();
    }

    public String intToBinary(int dec) {
        return extend(Integer.toBinaryString(dec), dec < 0);
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

    public String codeToBinary(String codeLine) {
        String[] components = codeLine.split("\\s+");
        switch (components.length) {
            case 1:
                switch (components[0].toLowerCase()) {
                    case "iadd":
                        return "01100000";
                    case "isub":
                        return "01100100";
                    case "nop":
                        return "00000000";
                }
            case 2:
                switch (components[0].toLowerCase()) {
                    case "bipush":
                        return "00010000";
                    case "iload":
                        return "00010101";
                    case "istore":
                        return "00110110";
                }
            case 3:
                switch (components[0].toLowerCase()){
                    case "iinc":
                        return "10000100";
                    case "goto":
                        return "10100111";
                    case "ifeq":
                        return "10011001";
                    case "iflt":
                        return "10011011";
                    case "if_icmpeq":
                        return "10011111";
                }
        }
        return "";
    }


    public String extend(String data, boolean sign) {
        String res = "";
        int length = 32 - data.length();
        char c = sign ? data.charAt(0) : '0';
        for (int i = 0; i < length; i++)
            res += c;
        res += data;
        return res;
    }

    public String booleanToString(boolean bool) {
        if (bool)
            return "1";
        return "0";
    }

    public boolean stringToBoolean(String string) {
        if (string.equals("1"))
            return true;
        return false;
    }
}