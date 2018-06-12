import java.util.Arrays;

public class Decoder {
    // It decodes every binary number and has variable size
    public boolean[] decode(String data) {
        int length = data.length();
        Utility utility = new Utility();
        int dataInt = utility.binaryToInt(data);
        int size = 1;
        for (int i = 0; i < length; i++)
            size *= 2;
        boolean[] res = new boolean[size];
        Arrays.fill(res, false);
        res[dataInt] = true;
        return res;
    }
}
