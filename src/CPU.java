public class CPU {
    private ControlUnit controlUnit = new ControlUnit();
    private DataPath dataPath = new DataPath();
    private Memory memory = new Memory();
    private Cache cache = new Cache(256, 1, 1, 1, memory.getCells());
    private long clk = 0;


    public String readWriteFetch(boolean reset) {
        String res;
        do {
            res = memory.signals(reset, controlUnit.read() | controlUnit.write() | controlUnit.fetch(),
                    !controlUnit.write(), dataPath.getAR().getData_out(), dataPath.getDR().getData_out());
        } while (!memory.isReady());
        return res;
    }

    public void signals(boolean reset) {
        Utility utility = new Utility();
        if (controlUnit.read()) {
            cache.read(utility.binaryToInt(dataPath.getAR().getData_out()));
            dataPath.getDR().signals(readWriteFetch(reset),
                    controlUnit.DR_LD(utility.isZ(dataPath.getH()), utility.isN(dataPath.getH())),
                    false, false, false, false, false, reset);
        }
        if (controlUnit.write()) {
            cache.write(utility.binaryToInt(dataPath.getAR().getData_out()), dataPath.getDR().getData_out());
            readWriteFetch(reset);
        }
        dataPath.signals(controlUnit.read(), memory.isReady(), reset, memory.getData_out(),
                controlUnit.DR_LD(utility.isZ(dataPath.getH()), utility.isN(dataPath.getH())),
                controlUnit.IR_LD(), controlUnit.fetch(),
                controlUnit.TOS_LD(), controlUnit.ALU_control(dataPath.isZ(), dataPath.isN()),
                controlUnit.LV_LD(), controlUnit.shifter_right(),
                controlUnit.PC_INC(utility.isZ(dataPath.getH()), utility.isN(dataPath.getH())),
                controlUnit.PC_INC2(utility.isZ(dataPath.getH()), utility.isN(dataPath.getH())), controlUnit.PC_LD(),
                controlUnit.shift_amt(utility.isZ(dataPath.getH()), utility.isN(dataPath.getH())),
                controlUnit.AR_LD(), controlUnit.CPP_LD(),
                controlUnit.bus_sel(utility.isZ(dataPath.getH()), utility.isN(dataPath.getH())),
                controlUnit.SP_SUB4(), controlUnit.SP_ADD4(), controlUnit.SP_LD(), controlUnit.H_LD());
        clk++;
        controlUnit.count(controlUnit.sc_reset(utility.isZ(dataPath.getH()), utility.isN(dataPath.getH())), true, reset);
        controlUnit.time_signals();
    }

    public void signalsWithFetch(boolean reset) {
        Utility utility = new Utility();
        cache.read(utility.binaryToInt(dataPath.getPC().getData_out()));
        dataPath.getAR().signals(dataPath.getPC().getData_out(), controlUnit.AR_LD(), false, false, false,
                false, false, reset);
        controlUnit.time_signals();
        //
        cache.read(utility.binaryToInt(dataPath.getAR().getData_out()));
        String d = readWriteFetch(reset);
        clk++;
        controlUnit.count(controlUnit.sc_reset(dataPath.isZ(), dataPath.isN()), true, reset);
        controlUnit.time_signals();
        dataPath.getPC().signals("", controlUnit.PC_LD(), controlUnit.PC_INC(dataPath.isZ(), dataPath.isN()), false, controlUnit.PC_INC2(dataPath.isZ(), dataPath.isN()),
                false, false, reset);
        dataPath.getIR().signals(d, controlUnit.IR_LD(), false, false, false, false, false, reset);
        System.out.println("aaa  " + controlUnit.sc_val());
        clk++;
        controlUnit.count(controlUnit.sc_reset(dataPath.isZ(), dataPath.isN()), true, reset);
        controlUnit.time_signals();
        //
        controlUnit.instruction_decoding(dataPath.IR());
        clk++;
        controlUnit.count(controlUnit.sc_reset(dataPath.isZ(), dataPath.isN()), true, reset);
        controlUnit.time_signals();
    }

    public void runStep(boolean reset) {
        controlUnit.time_signals();
        if (controlUnit.fetch())
            signalsWithFetch(reset);
        else signals(reset);
    }

    public ControlUnit getControlUnit() {
        return controlUnit;
    }

    public DataPath getDataPath() {
        return dataPath;
    }

    public Memory getMemory() {
        return memory;
    }

    public long getClk() {
        return clk;
    }

    public int getSC() {
        Utility utility = new Utility();
        return utility.binaryToInt(controlUnit.sc_val());
    }

    public Shifter getShifter() {
        return dataPath.getShifter();
    }
}
