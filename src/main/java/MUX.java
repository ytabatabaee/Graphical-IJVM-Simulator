public class MUX {
    // It has variable size
    public String data_out(String[] data_in, String select) {
        return data_in[new Utility().binaryToInt(select)];
    }
}
