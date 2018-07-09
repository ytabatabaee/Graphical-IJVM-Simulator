import java.util.Arrays;

public class ControlUnit {
    private boolean[] T = new boolean[16];
    private boolean[] D = new boolean[8];
    private boolean[] IR = new boolean[32];

    Utility utility = new Utility();
    private SequenceCounter sequenceCounter = new SequenceCounter();

    private int index(int x) {
        return 31 - x;
    }

    public void time_signals() {
        Decoder decoder = new Decoder();
        T = decoder.decode(sequenceCounter.getSc());
        System.out.println(Arrays.toString(D));
        System.out.println(Arrays.toString(IR));
        System.out.println(Arrays.toString(T));
        System.out.println("LD_AR " + AR_LD());
    }

    public String sc_val() {
        return sequenceCounter.getSc();
    }

    public void count(boolean sc_reset, boolean ready, boolean reset) {
        sequenceCounter.signals(sc_reset, ready, reset);
    }

    public void instruction_decoding(String IR) {
        System.out.println(IR);
        for (int i = 0; i < 32; i++) {
            this.IR[i] = (IR.charAt(index(i)) == '1');
        }
        Decoder decoder = new Decoder();
//        this.IR = decoder.decode(IR);
        String in = "" + IR.charAt(index(5)) + IR.charAt(index(2)) + IR.charAt(index(1));
        D = decoder.decode(in);
    }

    public boolean sc_reset(boolean ready) {
        return (!(IR[4]) & !(IR[7])
                & D[0] & T[3]) |
                (!(IR[4]) & !(IR[7])
                        & D[4] & T[7]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[10]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[8]) |
                (IR[7] & D[2] & T[10]) |
                (IR[7] & D[7] & T[9]);
    }

    public String bus_sel(boolean Z, boolean N) {
        boolean bus_tos = (!(IR[4]) & !(IR[7])
                & D[4] & T[5]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[5]);
        boolean bus_lv = ((IR[4]) & !(IR[7])
                & D[7] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[6]) |
                (IR[7] & D[2] & T[6]);
        boolean bus_cpp = false;
        boolean bus_sp = (!(IR[4]) & !(IR[7])
                & D[4] & T[4]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[8]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[7]);
        boolean bus_pc = (IR[7] & D[7] & T[9]);
        boolean bus_dr = (!(IR[4]) & !(IR[7])
                & D[4] & T[6]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[10]) |
                (IR[7] & D[2] & T[9]) |
                (IR[7] & D[7] & T[4]) |
                (IR[7] & D[7] & T[8]);
        boolean bus_ir = ((IR[4]) & !(IR[7])
                & D[0] & T[5]) | ((IR[4]) & !(IR[7])
                & D[7] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[3])|
                (IR[7] & D[2] & T[3]) |
                (IR[7] & D[2] & T[7]) |
                (IR[7] & D[7] & T[3]) |
                (IR[7] & D[7] & T[5]);
        if (bus_tos)
            return "001";
        if (bus_lv)
            return "010";
        if (bus_cpp)
            return "011";
        if (bus_sp)
            return "100";
        if (bus_pc)
            return "101";
        if (bus_dr)
            return "110";
        if (bus_ir)
            return "111";
        return "000";
    }

    private boolean[] ALU(boolean Z, boolean N) {
        boolean ALU_add = (!(IR[4]) & !(IR[7])
                & D[4] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[6]) |
                (IR[7] & D[2] & T[6]) |
                (IR[7] & D[2] & T[9]) |
                (IR[7] & D[7] & T[8]) |
                (IR[7] & D[7] & T[9]);
        boolean ALU_sub = (!(IR[4]) & !(IR[7])
                & D[6] & T[6]);
        boolean ALU_data2 = !(ALU_add | ALU_sub);
        return new boolean[]{ALU_add, ALU_sub, ALU_data2, false, false};
    }

    public String ALU_control(boolean Z, boolean N) {
        boolean[] ALU_s = ALU(Z, N);
        char[] c = {ALU_s[1] ? '1' : '0',
                ALU_s[1] ? '1' : '0',
                '1',
                !ALU_s[2] ? '1' : '0',
                '1',
                !ALU_s[2] ? '1' : '0'};
        String res = "" + c[5] + c[4] + c[3] + c[2] + c[1] + c[0];
        return res;
    }

    public String ALU_D2sel(boolean Z, boolean N) {
        boolean[] ALU_s = ALU(Z, N);
        char[] c = {ALU_s[3] ? '1' : '0', ALU_s[4] ? '1' : '0'};
        String res = "";
        res += c[1] + c[0];
        return res;
    }

    public boolean shifter_right() {
        return ((IR[4]) & !(IR[7])
                & D[0] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[4]) |
                (IR[7] & D[2] & T[8]) |
                (IR[7] & D[2] & T[4]) |
                (IR[7] & D[7] & T[4]) |
                (IR[7] & D[7] & T[5]) |
                (IR[7] & D[7] & T[7]);
    }

    public int shift_amt() {
        boolean shift24 = ((IR[4]) & !(IR[7])
                & D[0] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[4]) |
                (IR[7] & D[2] & T[4]) |
                (IR[7] & D[2] & T[8]) |
                (IR[7] & D[7] & T[4]) |
                (IR[7] & D[7] & T[6]);
        boolean shift16 = ((IR[4]) & !(IR[7])
                & D[0] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[3]) |
                (IR[7] & D[2] & T[3]) |
                (IR[7] & D[7] & T[7]);
        boolean shift2 = ((IR[4]) & !(IR[7])
                & D[2] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[5]) |
                (IR[7] & D[2] & T[5]);
        boolean shift8 = (IR[7] & D[2] & T[7]) |
                (IR[7] & D[7] & T[3]) |
                (IR[7] & D[7] & T[5]);
        if (shift24)
            return 24;
        if (shift16)
            return 16;
        if (shift2)
            return 2;
        if (shift8)
            return 8;
        return 0;
    }

    public boolean SP_SUB4() {
        return (!(IR[4]) & !(IR[7])
                & D[4] & T[3]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[6]) |
                (IR[7] & D[0] & T[3]) |
                (IR[7] & D[1] & T[3]) |
                (IR[7] & D[3] & T[3]) |
                (IR[7] & D[3] & T[4]);
    }

    public boolean SP_ADD4() {
        return ((IR[4]) & !(IR[7])
                & D[0] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[6]);
    }

    public boolean H_LD() {
        return (!(IR[4]) & !(IR[7])
                & D[4] & T[5]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[5]) |
                (IR[7] & D[2] & T[3]) |
                (IR[7] & D[2] & T[4]) |
                (IR[7] & D[2] & T[5]) |
                (IR[7] & D[2] & T[7]) |
                (IR[7] & D[2] & T[8]) |
                (IR[7] & D[7] & T[5]) |
                (IR[7] & D[7] & T[6]) |
                (IR[7] & D[7] & T[7]) |
                (IR[7] & D[7] & T[8]) |
                (IR[7] & D[0] & T[3]) |
                (IR[7] & D[1] & T[3]) |
                (IR[7] & D[3] & T[3]) |
                (IR[7] & D[3] & T[7]) |
                (IR[7] & D[3] & T[11]) |
                (IR[7] & D[3] & T[12]) |
                (IR[7] & D[3] & T[13]) |
                (IR[7] & D[3] & T[14]) |
                (IR[7] & D[1] & T[9]) |
                (IR[7] & D[1] & T[10]) |
                (IR[7] & D[1] & T[11]) |
                (IR[7] & D[1] & T[12]) |
                (IR[7] & D[0] & T[9]) |
                (IR[7] & D[0] & T[10]) |
                (IR[7] & D[0] & T[11]) |
                (IR[7] & D[0] & T[12]);
    }

    public boolean DR_LD() {
        return (!(IR[4]) & !(IR[7])
                & D[4] & T[5]) |
                (!(IR[4]) & !(IR[7])
                        & D[4] & T[6]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[7]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[6]) |
                (IR[7] & D[2] & T[7]) |
                (IR[7] & D[2] & T[9]) |
                (IR[7] & D[7] & T[3]) |
                (IR[7] & D[7] & T[4]);
    }

    public boolean AR_LD() {
        return T[0] |
                (!(IR[4]) & !(IR[7])
                        & D[4] & T[4]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[4]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[8]) |
                (IR[7] & D[2] & T[6]);
    }

    public boolean TOS_LD() {
        return (!(IR[4]) & !(IR[7])
                & D[4] & T[6]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[6]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[8]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[10]);
    }

    public boolean read() {
        return (!(IR[4]) & !(IR[7])
                & D[4] & T[5]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[5]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[9]) |
                (IR[7] & D[2] & T[7]);
    }

    public boolean write() {
        return (!(IR[4]) & !(IR[7])
                & D[4] & T[7]) |
                (!(IR[4]) & !(IR[7])
                        & D[6] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[7]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[8]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[8]) |
                (IR[7] & D[2] & T[10]);
    }

    public boolean fetch() {
        return T[0];
    }


    public boolean IR_LD() {
        return T[1];
    }

    public boolean PC_INC() {
        return T[1] |
                ((IR[4]) & !(IR[7])
                        & D[0] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[2] & T[3]) |
                ((IR[4]) & !(IR[7])
                        & D[7] & T[3]) |
                (IR[7] & D[2] & T[3]) |
                (IR[7] & D[2] & T[4]) |
                (IR[7] & D[7] & T[3]);
    }


    public boolean LV_LD() {
        return false;
    }

    public boolean CPP_LD() {
        return false;
    }

    public boolean SP_LD() {
        return false;
    }

    public boolean PC_LD() {
        return (IR[7] & D[7] & T[9]);
    }

    public boolean PC_INC2() {
        return false;
    }

    public String BSelect() {
        return "000";
    }

    public boolean[] getT() {
        return T;
    }

    public boolean[] getD() {
        return D;
    }

    public boolean[] getIR() {
        return IR;
    }
}
