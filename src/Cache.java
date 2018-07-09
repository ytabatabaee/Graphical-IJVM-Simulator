import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

class Cache {

    private int memorySize = 256;
    private int cacheSize = 32;
    private int cacheArch = -1;
    private long numOfHits = 0;
    private long numOfMisses = 0;
    private int evictionMode = -1;
    private int writeMode = -1;
    private int length = 1;
    private CacheCell[] cache = new CacheCell[cacheSize];
    private int[] mainMemory = new int[memorySize];
    private int[] delayList = new int[cacheSize];

    public Cache(int memorySize, int cacheArch, int evictionMode, int writeMode) {
        this.memorySize = memorySize;
        this.cacheSize = 32;
        this.cacheArch = cacheArch;
        this.numOfHits = 0;
        this.numOfMisses = 0;
        this.evictionMode = evictionMode;
        this.writeMode = writeMode;
        this.cache = new CacheCell[this.cacheSize];
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

    public Cache() {

    }

    int cacheAddressMap(int address) {
        return (address % (cacheSize / length)) * length;
    }

    int isInCache(int address) {
        int index = cacheAddressMap(address);
        int tag = address / (cacheSize / length);

        for (int i = index; i < index + length; i++)
            if (tag == cache[i].getTag())
                return i;
        return -1;
    }

    int read(int address) {
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

    void write(int address, int data) {
        if (writeMode == 0) {
            read(address);
            cache[isInCache(address)].setData(data);
            cache[isInCache(address)].setDirty(1);
        } else {
            mainMemory[address] = data;
        }
    }

    int notRandomEviction(int index) {
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

    int evictionLRU(int index) {
        return notRandomEviction(index);
    }

    int evictionMRU(int index) {
        return notRandomEviction(index);
    }

    int evictionFIFO(int index) {
        return notRandomEviction(index);
    }

    int evictionRandom(int index) {
        Random rand = new Random();
        int s = rand.nextInt(length);
        int i = index + s;
        if (cache[i].getDirty() == 1) {
            mainMemory[(cache[i].getTag() * (cacheSize / length)) + cache[i].getIndex()] = cache[i].getData();
            cache[i].setDirty(0);
        }
        return i;
    }

    int evictionLIP(int index) {
        return notRandomEviction(index);
    }

    int eviction(int index) {
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

    void updateDelayList(int address, int mode) {
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

    void handleCache() {
        for (int i = 0; i < cacheSize; i++) {
            if (cache[i].getDirty() == 1) {
                mainMemory[(cache[i].getTag() * (cacheSize / length)) + cache[i].getIndex()] = cache[i].getData();
                cache[i].setDirty(0);
            }
        }
    }


//    public static void main(String[] args) {
//        long ht, ms;
//        for (int i = 0; i < 3; i++) { //cacheArch modes
//            cacheArch = i;
//            init();
//            for (int j = 0; j < 2; j++) { //write modes
//                writeMode = j;
//                for (int k = 0; k < 5; k++) { //eviction modes
//                    evictionMode = k;
//                    ht = 0;
//                    ms = 0;
//                    numOfHits = 0;
//                    numOfMisses = 0;
//                    initMemory();
//                    mergeSort();
//                    handleCache();
//                    System.out.println("Merge Sort      -> CacheArch: " + i + "  Write Mode: " + j +
//                            "  Eviction Mode: " + k + "|  NumOfHits: " + numOfHits + "  NumOfMisses: " + numOfMisses +
//                            " Hit Rate: " + (numOfHits * 1.0 / (numOfHits + numOfMisses)));
//                    ht += numOfHits;
//                    ms += numOfMisses;
//                    numOfHits = 0;
//                    numOfMisses = 0;
//                    initMemory();
//                    bubbleSort();
//                    handleCache();
//                    System.out.println("Bubble Sort     -> CacheArch: " + i + "  Write Mode: " + j +
//                            "  Eviction Mode: " + k + "|  NumOfHits: " + numOfHits + "  NumOfMisses: " + numOfMisses +
//                            " Hit Rate: " + (numOfHits * 1.0 / (numOfHits + numOfMisses)));
//                    ht += numOfHits;
//                    ms += numOfMisses;
//                    numOfHits = 0;
//                    numOfMisses = 0;
//                    initMemory();
//                    insertionSort();
//                    handleCache();
//                    System.out.println("Insertion Sort  -> CacheArch: " + i + "  Write Mode: " + j +
//                            "  Eviction Mode: " + k + "|  NumOfHits: " + numOfHits + "  NumOfMisses: " + numOfMisses +
//                            " Hit Rate: " + (numOfHits * 1.0 / (numOfHits + numOfMisses)));
//                    ht += numOfHits;
//                    ms += numOfMisses;
//                    numOfHits = 0;
//                    numOfMisses = 0;
//                    initMemory();
//                    quickSort();
//                    handleCache();
//                    System.out.println("Quick Sort      -> CacheArch: " + i + "  Write Mode: " + j +
//                            "  Eviction Mode: " + k + "|  NumOfHits: " + numOfHits + "  NumOfMisses: " + numOfMisses +
//                            " Hit Rate: " + (numOfHits * 1.0 / (numOfHits + numOfMisses)));
//                    ht += numOfHits;
//                    ms += numOfMisses;
//                    System.out.println("Sorts           -> CacheArch: " + i + "  Write Mode: " + j +
//                            "  Eviction Mode: " + k + "|  NumOfHits: " + ht + "  NumOfMisses: " + ms +
//                            " Hit Rate: " + (ht * 1.0 / (ht + ms)));
//                    System.out.println("-------------------------------------------------------------------------" +
//                            "------------------------------------------------");
//                    System.out.println();
//
//                }
//            }
//        }
//    }
}

