public class DataPath {
    private Decoder decoder = new Decoder();
    private MUX mux = new MUX();
    private Register AR = new Register();
    private Register WD = new Register();
    private Register DR1 = new Register();
    private ALU alu = new ALU();
    private Register DR2 = new Register();
    private Register PC = new Register();
    private Register CPP = new Register();
    private Register LV = new Register();
    private Register SP = new Register();
    private Register IR = new Register();
    private Extractor extractor = new Extractor();
    private String bus;
    private DFF ST = new DFF();
    private DFF RW = new DFF();
    private DFF Z = new DFF();
    private DFF N = new DFF();
    private boolean ALU_Z;
    private boolean ALU_N;

    public void update(boolean AR_LD, boolean WD_LD, boolean DR1_LD, boolean DR2_LD, boolean PC_LD, boolean IR_LD,
                       boolean SP_SUB4, boolean SP_ADD4, boolean reset, boolean ST_val, boolean RW_val, boolean Z_en,
                       boolean N_en, String bus_sel, String ext_sel, String memory_data, String ALU_D2sel,
                       String ALU_control) {
        boolean[] bus_select = decoder.decode(bus_sel);
        String Lbus = bus;
        boolean LALU_Z = ALU_Z;
        boolean LALU_N = ALU_N;
        if (bus_select[0]) {
            bus = memory_data;
        } else if (bus_select[1]) {
            String[] in = {DR2.getData_out(), LV.getData_out(), PC.getData_out(), CPP.getData_out()};
            String res = mux.data_out(in, ALU_D2sel);
            bus = alu.data_out(DR1.getData_out(), res, ALU_control);
            ALU_Z = alu.Z(bus);
            ALU_N = alu.N(bus);
        } else if (bus_select[2]) {
            bus = SP.getData_out();
        } else if (bus_select[3]) {
            bus = PC.getData_out();
        } else if (bus_select[4]) {
            bus = extractor.data_out(IR.getData_out(), ext_sel);
        } else if (bus_select[5]) {
            bus = DR1.getData_out();
        } else if (bus_select[6]) {
            bus = DR2.getData_out();
        }
        AR.signals(Lbus, AR_LD, false, false, false, false, reset);
        WD.signals(Lbus, WD_LD, false, false, false, false, reset);
        DR1.signals(Lbus, DR1_LD, false, false, false, false, reset);
        DR2.signals(Lbus, DR2_LD, false, false, false, false, reset);
        PC.signals(Lbus, PC_LD, false, false, false, false, reset);
        CPP.signals(Lbus, false, false, false, false, false, reset);
        LV.signals(Lbus, false, false, false, false, false, reset);
        SP.signals(Lbus, false, false, false, SP_ADD4, SP_SUB4, reset);
        IR.signals(Lbus, IR_LD, false, false, false, false, reset);
        ST.signals(ST_val, true, reset);
        RW.signals(RW_val, true, reset);
        Z.signals(LALU_Z, Z_en, reset);
        N.signals(LALU_N, N_en, reset);
    }

    public String memory_address() {
        return AR.getData_out();
    }

    public String memory_wdata() {
        return WD.getData_out();
    }

    public boolean start() {
        return ST.isValue();
    }

    public boolean rwn() {
        return RW.isValue();
    }

    public String IR() {
        return IR.getData_out();
    }

    public boolean Z() {
        return Z.isValue();
    }

    public boolean N() {
        return N.isValue();
    }
    
}
