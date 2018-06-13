public class DFF {
    private boolean value;

    public void signals(boolean value, boolean enable, boolean reset) {
        if (reset)
            this.value = false;
        else if (enable)
            this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
