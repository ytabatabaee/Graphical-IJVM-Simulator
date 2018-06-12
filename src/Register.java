public class Register {
    private String data_out;
    final private String default_value;

    public void signals(String data_in, boolean LD, boolean INC, boolean DEC, boolean reset) {
        Utility utility = new Utility();
        if (LD)
            data_out = data_in;
        else if (INC)
            data_out = utility.operation(data_out, "1", "add");
        else if (DEC)
            data_out = utility.operation(data_out, "1", "sub");
        else if (reset)
            data_out = default_value;
    }

    public Register(String data_in, String default_value) {
        this.data_out = data_in;
        this.default_value = default_value;
    }

    public Register(String data_in) {
        this.data_out = data_in;
        this.default_value = "00000000000000000000000000000000";
    }

    public String getData_out() {
        return data_out;
    }
}
