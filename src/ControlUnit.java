public class ControlUnit {
    private boolean[] T = new boolean[16];
    private boolean[] L = new boolean[4];
    private boolean[] F = new boolean[8];
    private SequenceCounter sequenceCounter = new SequenceCounter();

    private int index(int x) {
        return 31 - x;
    }

    public void time_signals() {
        Decoder decoder = new Decoder();
        T = decoder.decode(sequenceCounter.getSc());
    }

    public String sc_val() {
        return sequenceCounter.getSc();
    }

    public void count(boolean sc_reset, boolean ready, boolean reset) {
        sequenceCounter.signals(sc_reset, ready, reset);
    }

    public void instruction_decoding(String IR) {
        if (T[3]) {
            boolean IR4 = IR.charAt(index(4)) == '1';
            boolean IR7 = IR.charAt(index(7)) == '1';
            L[1] = !(IR4 | IR7);
            L[2] = IR4 & !IR7;
            L[3] = IR7;

            String in = "";
            in += IR.charAt(index(5)) + IR.charAt(index(2)) + IR.charAt(index(1));
            Decoder decoder = new Decoder();
            F = decoder.decode(in);
        }

    }

    public boolean sc_reset(boolean ready) {
        return (L[1] & F[4] & T[12] & ready) |
                (L[1] & F[6] & T[12] & ready) |
                (L[1] & F[0] & T[4]) |
                (L[2] & F[0] & T[7] & ready) |
                (L[2] & F[2] & T[10] & ready) |
                (L[2] & F[7] & T[10] & ready) |
                (L[3] & F[2] & T[11] & ready) |
                (L[3] & F[7] & T[7]) |
                (L[3] & F[0] & T[9]) |
                (L[3] & F[1] & T[9]) |
                (L[3] & F[3] & T[12]);
    }

    public String bus_sel(boolean Z, boolean N) {
        boolean bus_ALU = (L[1] & F[4] & T[10]) |
                (L[1] & F[6] & T[10]) |
                (L[2] & F[2] & T[5]) |
                (L[2] & F[7] & T[8]) |
                (L[3] & F[2] & T[5]) |
                (L[3] & F[2] & T[9]) |
                (L[3] & F[7] & T[6]) |
                (L[3] & F[0] & T[8] & Z) |
                (L[3] & F[1] & T[8] & N) |
                (L[3] & F[3] & T[11] & Z);
        boolean bus_SP = (L[1] & F[4] & T[4]) |
                (L[1] & F[4] & T[7]) |
                (L[1] & F[6] & T[4]) |
                (L[1] & F[6] & T[7]) |
                (L[2] & F[0] & T[5]) |
                (L[2] & F[2] & T[8]) |
                (L[2] & F[7] & T[4]) |
                (L[3] & F[0] & T[4]) |
                (L[3] & F[1] & T[4]) |
                (L[3] & F[3] & T[4]) |
                (L[3] & F[3] & T[7]);
        boolean bus_PC = T[0];
        boolean bus_extractor = (L[2] & F[0] & T[4]) |
                (L[2] & F[2] & T[4]) |
                (L[2] & F[7] & T[7]) |
                (L[3] & F[2] & T[4]) |
                (L[3] & F[2] & T[8]) |
                (L[3] & F[7] & T[4]) |
                (L[3] & F[0] & T[7]) |
                (L[3] & F[1] & T[7]) |
                (L[3] & F[3] & T[10]);
        char bs0 = (bus_ALU | bus_PC) ? '1' : '0';
        char bs1 = (bus_SP | bus_PC) ? '1' : '0';
        char bs2 = bus_extractor ? '1' : '0';
        String bus_sel = "";
        bus_sel += bs2 + bs1 + bs0;
        return bus_sel;
    }

    private boolean[] ALU(boolean Z, boolean N) {
        boolean ALU_add = (L[1] & F[4] & T[10]) |
                (L[2] & F[2] & T[5]) |
                (L[2] & F[7] & T[8]) |
                (L[3] & F[2] & T[5]) |
                (L[3] & F[2] & T[9]) |
                (L[3] & F[7] & T[6]) |
                (L[3] & F[0] & T[8] & Z) |
                (L[3] & F[1] & T[8] & N) |
                (L[3] & F[3] & T[11] & Z);
        boolean ALU_sub = (L[1] & F[6] & T[10]) |
                (L[3] & F[3] & T[10]);
        boolean ALU_b = (L[3] & F[0] & T[7]) |
                (L[3] & F[1] & T[7]);
        boolean ALU_LV = (L[2] & F[2] & T[5]) |
                (L[2] & F[7] & T[8]) |
                (L[3] & F[2] & T[5]);
        boolean ALU_PC = (L[3] & F[7] & T[6]) |
                (L[3] & F[0] & T[8] & Z) |
                (L[3] & F[1] & T[8] & N) |
                (L[3] & F[3] & T[11] & Z);
        return new boolean[]{ALU_add, ALU_sub, ALU_b, ALU_LV, ALU_PC};
    }

    public String ALU_control(boolean Z, boolean N) {
        boolean[] ALU_s = ALU(Z, N);
        char[] c = {ALU_s[1] ? '1' : '0',
                ALU_s[1] ? '1' : '0',
                '1',
                !ALU_s[2] ? '1' : '0',
                '1',
                !ALU_s[2] ? '1' : '0'};
        String res = "";
        res += c[5] + c[4] + c[3] + c[2] + c[1] + c[0];
        return res;
    }

    public String ALU_D2sel(boolean Z, boolean N) {
        boolean[] ALU_s = ALU(Z, N);
        char[] c = {ALU_s[3] ? '1' : '0', ALU_s[4] ? '1' : '0'};
        String res = "";
        res += c[1] + c[0];
        return res;
    }

    public boolean Z_en() {
        return (L[3] & F[0] & T[7]) |
                (L[3] & F[3] & T[10]);
    }

    public boolean N_en() {
        return (L[3] & F[1] & T[7]);
    }

    public boolean SP_ADD4() {
        return (L[1] & F[4] & T[4]) |
                (L[1] & F[6] & T[4]) |
                (L[2] & F[7] & T[4]) |
                (L[3] & F[0] & T[4]) |
                (L[3] & F[1] & T[4]) |
                (L[3] & F[3] & T[4]) |
                (L[3] & F[3] & T[7]);

    }

    public boolean SP_SUB4() {
        return (L[2] & F[0] & T[4]) |
                (L[2] & F[2] & T[4]);

    }

    public boolean DR1_LD(boolean ready) {
        return (L[1] & F[4] & T[6] & ready) |
                (L[1] & F[6] & T[6] & ready) |
                (L[2] & F[2] & T[4]) |
                (L[2] & F[7] & T[7]) |
                (L[3] & F[2] & T[4]) |
                (L[3] & F[2] & T[7] & ready) |
                (L[3] & F[7] & T[4]) |
                (L[3] & F[0] & T[7]) |
                (L[3] & F[1] & T[7]) |
                (L[3] & F[3] & T[6] & ready) |
                (L[3] & F[3] & T[10]);
    }

    public boolean DR2_LD(boolean ready) {
        return (L[1] & F[4] & T[9] & ready) |
                (L[1] & F[6] & T[9] & ready) |
                (L[3] & F[2] & T[8]) |
                (L[3] & F[0] & T[6] & ready) |
                (L[3] & F[1] & T[6] & ready) |
                (L[3] & F[3] & T[9] & ready);
    }

    public boolean AR_LD() {
        return (T[0]) |
                (L[1] & F[4] & T[4]) |
                (L[1] & F[4] & T[7]) |
                (L[1] & F[6] & T[4]) |
                (L[1] & F[6] & T[7]) |
                (L[2] & F[0] & T[5]) |
                (L[2] & F[2] & T[5]) |
                (L[2] & F[2] & T[8]) |
                (L[2] & F[7] & T[4]) |
                (L[2] & F[7] & T[8]) |
                (L[3] & F[2] & T[5]) |
                (L[3] & F[0] & T[4]) |
                (L[3] & F[1] & T[4]) |
                (L[3] & F[3] & T[4]) |
                (L[3] & F[3] & T[7]);
    }

    public boolean WD_LD(boolean ready) {
        return (L[1] & F[4] & T[10]) |
                (L[1] & F[6] & T[10]) |
                (L[2] & F[0] & T[4]) |
                (L[2] & F[2] & T[7] & ready) |
                (L[2] & F[7] & T[6] & ready) |
                (L[3] & F[2] & T[9]);
    }

    public boolean ST_val() {
        return (T[0]) |
                (L[1] & F[4] & T[4]) |
                (L[1] & F[4] & T[7]) |
                (L[1] & F[4] & T[10]) |
                (L[1] & F[6] & T[4]) |
                (L[1] & F[6] & T[10]) |
                (L[1] & F[6] & T[7]) |
                (L[2] & F[0] & T[5]) |
                (L[2] & F[2] & T[5]) |
                (L[2] & F[2] & T[8]) |
                (L[2] & F[7] & T[4]) |
                (L[2] & F[7] & T[8]) |
                (L[3] & F[2] & T[5]) |
                (L[3] & F[2] & T[9]) |
                (L[3] & F[0] & T[4]) |
                (L[3] & F[1] & T[4]) |
                (L[3] & F[3] & T[4]) |
                (L[3] & F[3] & T[7]);
    }

    public boolean RW_val() {
        return (T[0]) |
                (L[1] & F[4] & T[4]) |
                (L[1] & F[4] & T[7]) |
                (L[1] & F[6] & T[4]) |
                (L[1] & F[6] & T[7]) |
                (L[2] & F[2] & T[5]) |
                (L[2] & F[7] & T[4]) |
                (L[3] & F[2] & T[5]) |
                (L[3] & F[0] & T[4]) |
                (L[3] & F[1] & T[4]) |
                (L[3] & F[3] & T[4]) |
                (L[3] & F[3] & T[7]);
    }

    public boolean PC_LD(boolean Z, boolean N) {
        return (L[3] & F[7] & T[6]) |
                (L[3] & F[0] & T[8] & Z) |
                (L[3] & F[1] & T[8] & N) |
                (L[3] & F[3] & T[11] & Z);
    }

    public boolean PC_INC() {
        return (T[0]) |
                (L[2] & T[4]) |
                (L[3] & T[4]) |
                (L[3] & T[5]);
    }

    public boolean IR_LD(boolean ready) {
        return (T[2] & ready);
    }

    public String extractor_sel() {
        boolean ext_const = L[3] & F[2] & T[8];
        boolean ext_varOffset = (L[2] & F[2] & T[4]) |
                (L[2] & F[7] & T[7]) |
                (L[3] & F[2] & T[4]);
        boolean ext_byte = L[2] & F[0] & T[4];
        boolean[] ext_sel = {(ext_const | ext_byte), (ext_varOffset | ext_byte)};
        char[] c = {ext_sel[0] ? '1' : '0', ext_sel[1] ? '1' : '0'};
        String res = "";
        res += c[1] + c[0];
        return res;
    }
}
