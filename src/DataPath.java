public class DataPath {
    Decoder decoder;
    MUX mux;
    Register AR;
    Register WD;
    Register DR1;
    ALU alu;
    Register DR2;
    Register PC;
    Register CPP;
    Register LV;
    Register SP;
    Register IR;
    Extractor extractor;

    public DataPath() {
        this.decoder = new Decoder();
        this.mux = new MUX();
        this.AR = new Register();
        this.WD = new Register();
        this.DR1 = new Register();
        this.alu = new ALU();
        this.DR2 = new Register();
        this.PC = new Register();
        this.CPP = new Register();
        this.LV = new Register();
        this.SP = new Register();
        this.IR = new Register();
        this.extractor = new Extractor();
    }
}
