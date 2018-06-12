public class SequenceCounter {
    private static String sc = "0000";
    private boolean sc_reset;
    private boolean clock;
    private boolean cnt_en;
    private boolean aclr;

    public void count() {
        Utility utility = new Utility();
        if (sc.equals("1111"))
            sc = "0000";
//        else
//            sc = utility.add
    }
}
