public class SequenceCounter {
    private String sc = "0000";

    public void signals(boolean sc_reset, boolean cnt_en, boolean aclr) {
        if (cnt_en && !sc_reset && !aclr) {
            Utility utility = new Utility();
            if (sc.equals("1111"))
                sc = "0000";
            else
                sc = utility.operation(sc, "1", "add").substring(28);
        } else if (sc_reset || aclr) {
            sc = "0000";
        }
    }

    public String getSc() {
        return sc;
    }
}
