class CacheCell {
    private int data;
    private int tag;
    private int valid;
    private int dirty;
    private int index;

    public CacheCell(int data, int address, int cacheArch) {
        this.data = data;
        this.valid = 1;
        if (cacheArch == 0) {
            this.tag = address / 100;
            this.index = address % 100;
        } else if (cacheArch == 1) {
            this.tag = address / 50;
            this.index = address % 50;
        } else if (cacheArch == 2) {
            this.tag = address / 25;
            this.index = address % 25;
        }
    }

    public CacheCell() {

    }

    public int getData() {
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

    public void setData(int data) {
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