public class Register {
    private String data_out;
    final private String default_value;

    public void signals(String data_in, boolean LD, boolean INC, boolean DEC, boolean INC2,
                        boolean INC4, boolean DEC4, boolean reset) {
        Utility utility = new Utility();
        if (reset)
            data_out = default_value;
        else if (LD)
            data_out = data_in;
        else if (INC)
            data_out = utility.operation(data_out, "1", "add");
        else if (DEC)
            data_out = utility.operation(data_out, "1", "sub");
        else if (INC2)
            data_out = utility.operation(data_out, "2", "add");
        else if (INC4)
            data_out = utility.operation(data_out, "4", "add");
        else if (DEC4)
            data_out = utility.operation(data_out, "4", "sub");
    }

    public Register(String data_in) {
        this.data_out = data_in;
        this.default_value = data_in;
    }

    public Register() {
        this.data_out = "00000000000000000000000000000000";
        this.default_value = "00000000000000000000000000000000";
    }

    public String getData_out() {
        return data_out;
    }
}
