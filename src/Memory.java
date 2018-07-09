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

    public Memory() {
        for (int i = 0; i < cell.length; i++) {
            this.cell[i] = "00000000";
        }
    }

    public String signals(boolean reset, boolean start, boolean rwn, String address, String data_in) {
        if (reset) {
            for (int i = 0; i < cell.length; i++) {
                cell[i] = "00000000";
            }
        } else {
            if (start && !state) {
                ad_t = address.substring(16);
                rwn_t = rwn;
                data_t = data_in;
                counter = "" + address.charAt(30) + address.charAt(31);
                state = true;
                ready = false;
            } else if ((counter.charAt(1) == '1' || counter.charAt(0) == '1') && state) {
                String cnt = utility.operation(counter, "1", "sub");
                counter = "" + cnt.charAt(30) + cnt.charAt(31);
            } else if (state) {
                int ad = utility.binaryToInt(ad_t);
                if (rwn_t)
                    data_out = cell[ad + 3] + cell[ad + 2] + cell[ad + 1] + cell[ad];
                else {
                    cell[ad] = data_t.substring(24);
                    cell[ad + 1] = data_t.substring(16, 24);
                    cell[ad + 2] = data_t.substring(8, 16);
                    cell[ad + 3] = data_t.substring(0, 8);
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

    public String getCell(int index) {
        return cell[index];
    }

    public void setCell(String cell, int index) {
        this.cell[index] = cell;
    }

    public void setCell(String[] cell) {
        this.cell = cell;
    }

    public void setCell(String cell) {
        for (int i = 0; i < cell.length() / 8; i++)
            this.cell[i] = cell.substring(i * 8, (i + 1) * 8);
    }
}
