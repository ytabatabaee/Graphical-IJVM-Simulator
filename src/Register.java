public class Register {
    private String data_out;
    final private String default_value;
    private boolean LD;
    private boolean DEC;
    private boolean INC;
    private boolean reset;
    private boolean clk;

    public void LD() {

    }

    public void INC() {

    }

    public void DEC() {

    }

    public void reset() {

    }

    public Register(String data_in, String default_value) {
        this.data_out = data_in;
        this.default_value = default_value;
    }

    public Register(String data_in) {
        this.data_out = data_in;
        this.default_value = "00000000000000000000000000000000";
    }
}
