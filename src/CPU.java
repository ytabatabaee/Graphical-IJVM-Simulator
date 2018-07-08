public class CPU {
    private ControlUnit controlUnit = new ControlUnit();
    private DataPath dataPath = new DataPath();
    private Memory memory = new Memory();
    private long clk = 0;
    private String address;
    private String wData;
    private String sc_value;
    private boolean start;
    private boolean rwn;

    public void fetch(boolean reset) {
        do {
            memory.signals(reset, controlUnit.read() | controlUnit.write(),
                    !controlUnit.write(), dataPath.getAR().getData_out(), dataPath.getDR().getData_out());
            clk++;
            boolean a = !(controlUnit.read() | controlUnit.write() | !memory.isReady());
            controlUnit.count(reset | a, memory.isReady(), reset);

        } while (!memory.isReady());
    }

//    public void signals(String data_in, boolean ready, boolean reset) {
//        address = dataPath.memory_address();
//        //wData = dataPath.memory_wdata();
//        //start = dataPath.start();
//        //rwn = dataPath.rwn();
//        sc_value = controlUnit.sc_val();
//        fetch();
//        controlUnit.time_signals();
//        controlUnit.instruction_decoding(dataPath.IR());
//        dataPath.signals(controlUnit.AR_LD(), controlUnit.WD_LD(ready), controlUnit.DR1_LD(ready),
//                controlUnit.DR2_LD(ready), controlUnit.PC_LD(dataPath.Z(), dataPath.N()), controlUnit.IR_LD(ready),
//                controlUnit.SP_SUB4(), controlUnit.SP_ADD4(), reset, controlUnit.ST_val(), controlUnit.RW_val(),
//                controlUnit.Z_en(), controlUnit.N_en(), controlUnit.PC_INC(),
//                controlUnit.bus_sel(dataPath.Z(), dataPath.N()), controlUnit.extractor_sel(), data_in,
//                controlUnit.ALU_D2sel(dataPath.Z(), dataPath.N()), controlUnit.ALU_control(dataPath.Z(), dataPath.N()));
//    }

    public ControlUnit getControlUnit() {
        return controlUnit;
    }

    public DataPath getDataPath() {
        return dataPath;
    }
}
