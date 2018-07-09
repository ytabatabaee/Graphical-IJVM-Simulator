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
            clk++;
            boolean a = !(controlUnit.read() | controlUnit.write() | !memory.isReady());
            controlUnit.count(reset | a | controlUnit.sc_reset(memory.isReady()), memory.isReady(), reset);

        } while (!memory.isReady());
        return res;
    }

    public void signals(boolean reset) {
        boolean a = !(controlUnit.read() | controlUnit.write() | !memory.isReady());
        dataPath.signals(controlUnit.read(), memory.isReady(), reset, memory.getData_out(), controlUnit.DR_LD(),
                controlUnit.IR_LD(), controlUnit.fetch(),
                controlUnit.TOS_LD(), controlUnit.ALU_control(dataPath.isZ(), dataPath.isN()),
                controlUnit.LV_LD(), controlUnit.shifter_right(), controlUnit.PC_INC(), controlUnit.PC_INC2(), controlUnit.PC_LD(),
                controlUnit.shift_amt(), controlUnit.AR_LD(), controlUnit.CPP_LD(), controlUnit.BSelect(),
                controlUnit.SP_SUB4(), controlUnit.SP_ADD4(), controlUnit.SP_LD(), controlUnit.H_LD());
        clk++;
        controlUnit.count(reset | a | controlUnit.sc_reset(memory.isReady()), memory.isReady(), reset);
        controlUnit.time_signals();
    }

    public void signalsWithFetch(boolean reset) {
        dataPath.getIR().setData_out(read(reset));
        controlUnit.time_signals();
        controlUnit.instruction_decoding(dataPath.IR());
        boolean a = !(controlUnit.read() | controlUnit.write() | !memory.isReady());
        clk++;
        controlUnit.count(reset | a | controlUnit.sc_reset(memory.isReady()), memory.isReady(), reset);
        controlUnit.time_signals();
        signals(reset);
    }

    public void runStep(boolean reset) {
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
