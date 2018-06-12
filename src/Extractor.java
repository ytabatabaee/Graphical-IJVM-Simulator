public class Extractor {
    public String data_out(String data_in, String select) {
        Utility utility = new Utility();
        String rawOffset = data_in.substring(8, 24);
        rawOffset = utility.extend(rawOffset, true);
        String offset = utility.operation(rawOffset, "11", "sub");
        String Const = data_in.substring(8, 16);
        Const = utility.extend(Const, true);
        String varOffset = data_in.substring(16, 24);
        varOffset += "00";
        varOffset = utility.extend(varOffset, false);
        String Byte = data_in.substring(16, 24);
        Byte = utility.extend(Byte, true);
        String[] mux_in = {offset, Const, varOffset, Byte};
        return mux_in[utility.binaryToInt(select)];
    }
}
