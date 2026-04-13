/**
 * Created by y.tabatabaee on 7/8/2018.
 */
public class Shifter {
    int shift_amt = 0;
    boolean right;
    public String shift(int shift_amt, boolean right, String data_in) {
        this.shift_amt = shift_amt;
        this.right = right;
        Utility utility = new Utility();
        if (right)
            return utility.intToBinary(utility.binaryToInt(data_in )>>> shift_amt);
        else
            return utility.intToBinary(utility.binaryToInt(data_in )<< shift_amt);
    }

}
