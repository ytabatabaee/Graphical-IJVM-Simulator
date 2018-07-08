public class DataPath {
    private Decoder decoder = new Decoder();
    private MUX mux = new MUX();
    private ALU alu = new ALU();
    private Shifter shifter = new Shifter();
    private Register AR = new Register();
    private Register DR = new Register();
    private Register IR = new Register();
    private Register PC = new Register();
    private Register SP = new Register("00000000000000000000000001000000");
    private Register LV = new Register("00000000000000000000000010000000");
    private Register CPP = new Register("00000000000000000000000011000000");
    private Register TOS = new Register();
    private Register H = new Register();
    private String B;
    private String C;
    private DFF D1 = new DFF();
    private DFF D2 = new DFF();
    private DFF D3 = new DFF();
    private boolean Z;
    private boolean N;

    public void signals(boolean read, boolean ready, boolean reset, String mem, boolean LD_DR, boolean Reset_CPP,
                        boolean LD_IR, boolean fetch, boolean LD_TOS, String ALUControll, boolean LD_LV, boolean LR,
                        boolean INC_PC, boolean INC2_PC, boolean LD_PC, String ShSelect, boolean LD_AR, boolean LD_CPP,
                        String BSelect, boolean DEC4_SP, boolean INC4_SP, boolean LD_SP, boolean LD_H) {
        String x = alu.data_out(H.getData_out(), B, ALUControll);
        Z = alu.Z(x);
        N = alu.N(x);
        Utility utility = new Utility();
        B = mux.data_out(new String[]{H.getData_out(), TOS.getData_out(), LV.getData_out(), CPP.getData_out(),
                SP.getData_out(), PC.getData_out(), DR.getData_out(), IR.getData_out()}, BSelect);
        C = shifter.shift(utility.binaryToInt(mux.data_out(new String[]{"00000", "00010", "01000", "10000", "11000",
                "00100", "11001"}, ShSelect)), LR, x);

        String DR_WData = mux.data_out(new String[]{C, mem}, utility.booleanToString(D2.isValue()));
        boolean DR_WEnable = D2.isValue() | LD_DR;
        DR.signals(DR_WData, DR_WEnable, false, false, false, false, false, reset);
        String IR_WData = mux.data_out(new String[]{C, mem}, utility.booleanToString(D3.isValue()));
        boolean IR_WEnable = D3.isValue() | LD_IR;
        IR.signals(IR_WData, IR_WEnable, false, false, false, false, false, reset);
        D2.setValue(utility.stringToBoolean(mux.data_out(new String[]
                        {utility.booleanToString(!(!D1.isValue() & ready)), utility.booleanToString(read)},
                utility.booleanToString(D2.isValue()))));
        D3.setValue(utility.stringToBoolean(mux.data_out(new String[]
                        {utility.booleanToString(!(!D1.isValue() & ready)), utility.booleanToString(fetch)},
                utility.booleanToString(D3.isValue()))));
        D1.setValue(ready);
        TOS.signals(C, LD_TOS, false, false, false, false, false, reset);
        LV.signals(C, LD_LV, false, false, false, false, false, reset);
        PC.signals(C, LD_PC, INC_PC, false, INC2_PC, false, false, reset);
        AR.signals(C, LD_AR, false, false, false, false, false, reset);
        CPP.signals(C, LD_CPP, false, false, false, false, false, reset | Reset_CPP);
        SP.signals(C, LD_SP, false, false, false, INC4_SP, DEC4_SP, reset);
        H.signals(C, LD_H, false, false, false, false, false, reset);
    }


    public String memory_address() {
        return AR.getData_out();
    }

    public String IR() {
        return IR.getData_out();
    }

    public Register getAR() {
        return AR;
    }

    public Register getPC() {
        return PC;
    }

    public Register getCPP() {
        return CPP;
    }

    public Register getLV() {
        return LV;
    }

    public Register getSP() {
        return SP;
    }

    public Register getIR() {
        return IR;
    }

    public String getB() {
        return B;
    }

    public Register getDR() {
        return DR;
    }

    public Register getTOS() {
        return TOS;
    }

    public Register getH() {
        return H;
    }

    public boolean isN() {
        return N;
    }

    public boolean isZ() {
        return Z;
    }
}
