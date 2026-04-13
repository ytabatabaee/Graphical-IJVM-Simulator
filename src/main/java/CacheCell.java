class CacheCell {
    private String data;
    private int tag;
    private int valid;
    private int dirty;
    private int index;

    public CacheCell(String data, int address, int cacheArch) {
        this.data = data;
        this.valid = 1;
        address = address / 4;
        if (cacheArch == 0) {
            this.tag = address / 8;
            this.index = address % 8;
        } else if (cacheArch == 1) {
            this.tag = address / 4;
            this.index = address % 4;
        } else if (cacheArch == 2) {
            this.tag = address / 2;
            this.index = address % 2;
        }
    }

    public CacheCell() {

    }

    public String getData() {
        return data;
    }

    public int getTag() {
        return tag;
    }

    public int getValid() {
        return valid;
    }

    public int getDirty() {
        return dirty;
    }

    public int getIndex() {
        return index;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}