import java.util.Random;

class Cache {

    private int memorySize = 256;
    private int cacheSize = 8;
    private int cacheArch = -1;
    private long numOfHits = 0;

    public CacheCell[] getCache() {
        return cache;
    }

    private long numOfMisses = 0;
    private int evictionMode = -1;
    private int writeMode = -1;
    private int length = 1;
    private CacheCell[] cache = new CacheCell[cacheSize];
    private String[] mainMemory = new String[memorySize];
    private int[] delayList = new int[cacheSize];

    public Cache(int memorySize, int cacheArch, int evictionMode, int writeMode, String[] mainMemory) {
        this.memorySize = memorySize;
        this.cacheSize = 8;
        this.cacheArch = cacheArch;
        this.numOfHits = 0;
        this.numOfMisses = 0;
        this.evictionMode = evictionMode;
        this.writeMode = writeMode;
        this.cache = new CacheCell[this.cacheSize];
        this.mainMemory = mainMemory;
        this.delayList = new int[this.cacheSize];
        if (cacheArch == 0)
            this.length = 1;
        else if (cacheArch == 1)
            this.length = 2;
        else if (cacheArch == 2)
            this.length = 4;
        for (int i = 0; i < this.cacheSize; i++)
            this.delayList[i] = i % this.length;

        for (int i = 0; i < this.cacheSize; i++)
            this.cache[i] = new CacheCell();
    }

//    public Cache() {
//
//    }

    public int cacheAddressMap(int address) {
        return (address % (cacheSize / length)) * length;
    }

    public int isInCache(int address) {
        int index = cacheAddressMap(address);
        int tag = address / (cacheSize / length);

        for (int i = index; i < index + length; i++)
            if (tag == cache[i].getTag())
                return i;
        return -1;
    }

    public String read(int address) {
        if (isInCache(address) != -1) {
            numOfHits++;
            updateDelayList(isInCache(address), 1);
            return mainMemory[address];
        }
        numOfMisses++;
        int index = cacheAddressMap(address);
        for (int i = index; i < index + length; i++)
            if (cache[i].getValid() == 0) {
                cache[i] = new CacheCell(mainMemory[address], address, cacheArch);
                updateDelayList(i, 0);
                return mainMemory[address];
            }
        int insertAddress = eviction(index);
        cache[insertAddress] = new CacheCell(mainMemory[address], address, cacheArch);
        updateDelayList(insertAddress, 0);
        return mainMemory[address];
    }

    public void write(int address, String data) {
        if (writeMode == 0) {
            read(address);
            cache[isInCache(address)].setData(data);
            cache[isInCache(address)].setDirty(1);
        } else {
            mainMemory[address] = data;
        }
    }

    public int notRandomEviction(int index) {
        for (int i = index; i < index + length; i++) {
            if (delayList[i] == 0) {
                if (cache[i].getDirty() == 1) {
                    mainMemory[(cache[i].getTag() * (cacheSize / length)) + cache[i].getIndex()] = cache[i].getData();
                    cache[i].setDirty(0);
                }
                return i;
            }
        }
        return -1;
    }

    public int evictionLRU(int index) {
        return notRandomEviction(index);
    }

    public int evictionMRU(int index) {
        return notRandomEviction(index);
    }

    public int evictionFIFO(int index) {
        return notRandomEviction(index);
    }

    public int evictionRandom(int index) {
        Random rand = new Random();
        int s = rand.nextInt(length);
        int i = index + s;
        if (cache[i].getDirty() == 1) {
            mainMemory[(cache[i].getTag() * (cacheSize / length)) + cache[i].getIndex()] = cache[i].getData();
            cache[i].setDirty(0);
        }
        return i;
    }

    public int evictionLIP(int index) {
        return notRandomEviction(index);
    }

    public int eviction(int index) {
        if (evictionMode == 0)
            return evictionFIFO(index);
        else if (evictionMode == 1)
            return evictionLRU(index);
        else if (evictionMode == 2)
            return evictionMRU(index);
        else if (evictionMode == 3)
            return evictionRandom(index);
        else if (evictionMode == 4)
            return evictionLIP(index);
        return -1;
    }

    public void updateDelayList(int address, int mode) {
        int index = (address / length) * length;

        if ((evictionMode == 0 && mode == 0) || evictionMode == 1 ||
                (evictionMode == 4 && mode == 1)) {
            for (int i = index; i < index + length; i++)
                if (delayList[i] > delayList[address])
                    delayList[i]--;
            delayList[address] = length - 1;
        } else if (evictionMode == 2) {
            for (int i = index; i < index + length; i++)
                if (delayList[i] < delayList[address])
                    delayList[i]++;
            delayList[address] = 0;
        }
    }

    public double hitRate() {
        return (1.0 * numOfHits) / (numOfHits + numOfMisses);
    }

    public long getNumOfHits() {
        return numOfHits;
    }

    public long getNumOfMisses() {
        return numOfMisses;
    }

    //    void handleCache() {
//        for (int i = 0; i < cacheSize; i++) {
//            if (cache[i].getDirty() == 1) {
//                mainMemory[(cache[i].getTag() * (cacheSize / length)) + cache[i].getIndex()] = cache[i].getData();
//                cache[i].setDirty(0);
//            }
//        }
//    }
}

