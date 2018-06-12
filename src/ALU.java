public class ALU {

    public String data_out(String data1, String data2, String control) {
        if (control.equals("011000"))
            return data1;
        if (control.equals("010100"))
            return data2;
        if (control.equals("011010"))
            return operation(data1, data2, "not");
        if (control.equals("101100"))
            return operation(data2, data1, "not");
        if (control.equals("111100"))
            return operation(data1, data2, "add");
        if (control.equals("111101"))
            return operation("1", operation(data1, data2, "add"), "add");
        if (control.equals("111001"))
            return operation(data1, "1", "add");
        if (control.equals("110101"))
            return operation(data2, "1", "add");
        if (control.equals("111111"))
            return operation(data2, data1, "subtract");
        if (control.equals("110110"))
            return operation(data2, "1", "subtract");
        if (control.equals("111011"))
            return operation(data1, data2, "negate");
        if (control.equals("001100"))
            return operation(data1, data2, "and");
        if (control.equals("011100"))
            return operation(data1, data2, "or");
        if (control.equals("010000"))
            return "00000000000000000000000000000000";
        if (control.equals("110001"))
            return "00000000000000000000000000000001";
        if (control.equals("110010"))
            return "11111111111111111111111111111111";
        return "00000000000000000000000000000000";
    }

    public boolean N(String data) {
        return data.charAt(0) == '1';
    }

    public boolean Z(String data) {
        for (int i = 0; i < data.length(); i++)
            if (data.charAt(i) == '1')
                return false;
        return true;
    }

    // op = add | and | subtract | negate | or | not | sign extend
    // unary operations use data1
    private String operation(String data1, String data2, String op) {
        if (op.equals("not")) {
            String res = "";
            for (int i = 0; i < data1.length(); i++) {
                if (data1.charAt(i) == '0')
                    res += '1';
                else if (data1.charAt(i) == '1')
                    res += '0';
            }
            return res;
        } else if (op.equals("negate")) {
            String res = operation(data1, data2, "not");
            res = operation(res, "1", "add");
            return res;
        } else {
            Utility utility = new Utility();
            int dataInt1 = utility.binaryToInt(data1);
            int dataInt2 = utility.binaryToInt(data2);
            int res = 0;
            if (op.equals("add"))
                res = dataInt1 + dataInt2;
            else if (op.equals("subtract"))
                res = dataInt1 - dataInt2;
            else if (op.equals("and"))
                res = dataInt1 & dataInt2;
            else if (op.equals("or"))
                res = dataInt1 | dataInt2;
            return utility.intToBinary(res);
        }
    }
}
