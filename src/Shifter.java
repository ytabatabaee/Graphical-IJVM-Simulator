/**
 * Created by y.tabatabaee on 7/8/2018.
 */
public class Shifter {
    private int shift_amt;
    private boolean right;
    private String data_in;
    private String data_out;

    public Shifter(int shift_amt, boolean right, String data_in) {
        this.shift_amt = shift_amt;
        this.data_in = data_in;
        this.right = right;
    }

    public String getData_out() {
        Utility utility = new Utility();
        if (right)
            return utility.intToBinary(utility.binaryToInt(data_in )>> shift_amt);
        else
            return utility.intToBinary(utility.binaryToInt(data_in )<< shift_amt);
    }
}
