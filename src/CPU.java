public class CPU {
    private ControlUnit controlUnit = new ControlUnit();
    private DataPath dataPath = new DataPath();
    private Memory memory = new Memory();
    private long clk = 0;


    public String read(boolean reset) {
        String res;
        do {
            res = memory.signals(reset, controlUnit.read() | controlUnit.write() | controlUnit.fetch(),
                    !controlUnit.write(), dataPath.getAR().getData_out(), dataPath.getDR().getData_out());
            boolean a = !(controlUnit.read() | controlUnit.write() | !memory.isReady());
            clk++;
            controlUnit.count(controlUnit.sc_reset(memory.isReady()), a, reset);
            controlUnit.time_signals();

        } while (!memory.isReady());
        return res;
    }

    public void signals(boolean reset) {
        boolean a = !(controlUnit.read() | controlUnit.write() | !memory.isReady());
        dataPath.signals(controlUnit.read(), memory.isReady(), reset, memory.getData_out(), controlUnit.DR_LD(),
                controlUnit.IR_LD(), controlUnit.fetch(),
                controlUnit.TOS_LD(), controlUnit.ALU_control(dataPath.isZ(), dataPath.isN()),
                controlUnit.LV_LD(), controlUnit.shifter_right(), controlUnit.PC_INC(),
                controlUnit.PC_INC2(), controlUnit.PC_LD(),
                controlUnit.shift_amt(), controlUnit.AR_LD(), controlUnit.CPP_LD(),
                controlUnit.bus_sel(dataPath.isZ(), dataPath.isN()),
                controlUnit.SP_SUB4(), controlUnit.SP_ADD4(), controlUnit.SP_LD(), controlUnit.H_LD());
        clk++;
        controlUnit.count(controlUnit.sc_reset(memory.isReady()), a, reset);
        controlUnit.time_signals();
    }

    public void signalsWithFetch(boolean reset) {
        dataPath.getAR().signals(dataPath.getPC().getData_out(), controlUnit.AR_LD(), false, false, false,
                false, false, reset);
        String d = read(reset);
        dataPath.getPC().signals("", controlUnit.PC_LD(), controlUnit.PC_INC(), false, controlUnit.PC_INC2(),
                false, false, reset);
        boolean a = !(controlUnit.read() | controlUnit.write() | !memory.isReady());
        dataPath.getIR().signals(d, controlUnit.IR_LD(), false, false, false, false, false, reset);
        System.out.println("aaa  " + controlUnit.sc_val());
        clk++;
        controlUnit.count(controlUnit.sc_reset(memory.isReady()), a, reset);
        controlUnit.time_signals();
        controlUnit.instruction_decoding(dataPath.IR());
        clk++;
        controlUnit.count(controlUnit.sc_reset(memory.isReady()), a, reset);
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
}
