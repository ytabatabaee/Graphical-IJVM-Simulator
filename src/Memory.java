public class Memory {
    private String[] cell = new String[256];
    private boolean state = false;
    private String ad_t = "";
    private boolean rwn_t = false;
    private String data_t;
    private String counter = "";
    private Utility utility = new Utility();
    private String data_out = "";
    private boolean ready = true;


    public String signals(boolean reset, boolean start, boolean rwn, String address, String data_in) {
        if (reset) {
            for (int i = 0; i < cell.length; i++) {
                cell[i] = "00000000";
            }
        } else {
            if (start && !state) {
                for (int i = 15; i >= 0; i--)
                    ad_t += address.charAt(i);
                rwn_t = rwn;
                data_t = data_in;
                counter = "" + address.charAt(1) + address.charAt(0);
                state = true;
                ready = false;
            } else if ((counter.charAt(1) == '1' || counter.charAt(0) == '1') && state) {
                String cnt = utility.operation(counter, "1", "sub");
                counter = "" + cnt.charAt(1) + cnt.charAt(0);
            } else if (state) {
                int ad = utility.binaryToInt(ad_t);
                if (rwn_t)
                    data_out = cell[ad + 3] + cell[ad + 2] + cell[ad + 1] + cell[ad];
                else {
                    for (int i = 0; i < 4; i++)
                        cell[ad + i] = "";
                    for (int i = 7; i >= 0; i--) {
                        cell[ad] += data_t.charAt(i);
                        cell[ad + 1] += data_t.charAt(i + 8);
                        cell[ad + 2] += data_t.charAt(i + 16);
                        cell[ad + 3] += data_t.charAt(i + 24);
                    }
                }
                state = false;
                ready = true;
            }
        }
        return data_out;
    }

    public boolean isReady() {
        return ready;
    }

    public String getData_out() {
        return data_out;
    }

    public void setCell(String cell, int index) {
        this.cell[index] = cell;
    }

    public void setCell(String[] cell) {
        this.cell = cell;
    }
}
