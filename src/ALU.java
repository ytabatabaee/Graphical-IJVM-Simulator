public class ALU {
    private Utility utility = new Utility();

    public String data_out(String data1, String data2, String control) {
        if (control.equals("011000"))
            return data1;
        if (control.equals("010100"))
            return data2;
        if (control.equals("011010"))
            return utility.operation(data1, data2, "not");
        if (control.equals("101100"))
            return utility.operation(data2, data1, "not");
        if (control.equals("111100"))
            return utility.operation(data1, data2, "add");
        if (control.equals("111101"))
            return utility.operation("1", utility.operation(data1, data2, "add"), "add");
        if (control.equals("111001"))
            return utility.operation(data1, "1", "add");
        if (control.equals("110101"))
            return utility.operation(data2, "1", "add");
        if (control.equals("111111"))
            return utility.operation(data2, data1, "subtract");
        if (control.equals("110110"))
            return utility.operation(data2, "1", "subtract");
        if (control.equals("111011"))
            return utility.operation(data1, data2, "negate");
        if (control.equals("001100"))
            return utility.operation(data1, data2, "and");
        if (control.equals("011100"))
            return utility.operation(data1, data2, "or");
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

}
