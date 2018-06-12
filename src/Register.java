public class Register {
    private String data_in;
    final private String default_value;
    private boolean LD;
    private boolean DEC;
    private boolean INC;
    private boolean reset;
    private boolean clk;

    public Register(String data_in, String default_value) {
        this.data_in = data_in;
        this.default_value = default_value;
    }

    public Register(String data_in) {
        this.data_in = data_in;
        this.default_value = "0";
    }

    public String getData_in() {
        return data_in;
    }

    public void setData_in(String data_in) {
        this.data_in = data_in;
    }

    public String getDefault_value() {
        return default_value;
    }

    public boolean isLD() {
        return LD;
    }

    public void setLD(boolean LD) {
        this.LD = LD;
    }

    public boolean isDEC() {
        return DEC;
    }

    public void setDEC(boolean DEC) {
        this.DEC = DEC;
    }

    public boolean isINC() {
        return INC;
    }

    public void setINC(boolean INC) {
        this.INC = INC;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isClk() {
        return clk;
    }

    public void setClk(boolean clk) {
        this.clk = clk;
    }
}
