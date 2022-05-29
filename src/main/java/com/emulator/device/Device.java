package com.emulator.device;

import com.emulator.Monitor;
import com.emulator.utility.Wiring;

public abstract class Device {
    public final int BASE_ADDR;
    public final int END_ADDR;
    protected final int[] MEMORY;

    public final boolean readable;
    public final boolean writeable;

    public Device(int baseAddr, int capacity, boolean readable, boolean writeable) {
        this.BASE_ADDR = baseAddr;
        this.END_ADDR = baseAddr + capacity;
        this.MEMORY = new int[capacity >> 2 + 1];
        this.readable = readable;
        this.writeable = writeable;
    }

    public boolean access(int hostAddr) {
        return this.BASE_ADDR <= hostAddr && hostAddr < this.END_ADDR;
    }

    protected void update() { }

    protected int alignedRead(int addr) {
        return MEMORY[addr >>> 2];
    }

    protected void alignedWrite(int addr, int data) {
        MEMORY[addr >>> 2] = data;
    }

    protected int host2PAddr(int addr) {
        return addr - BASE_ADDR;
    }

    public int load(int hostAddr, int len) {
        int addr = host2PAddr(hostAddr);
        switch (len) {
            case 1:
                return loadByte(addr);
            case 2:
                return loadHalfWord(addr);
            case 4:
                return loadWord(addr);
            default:
                Monitor.log.warn(String.format("Wrong access byte length: %d", len));
                return 0;
        }
    }

    protected int loadByte(int addr) {
        return (byte) (alignedRead(addr) >>> (Byte.SIZE * (addr & 0b11)));
    }

    protected int loadHalfWord(int addr) {
        return (short) (alignedRead(addr) >>> (Byte.SIZE * (addr & 0b10)));
    }

    protected int loadWord(int addr) {
        return alignedRead(addr);
    }
    
    public void write(int hostAddr, int len, int data) {
        int addr = host2PAddr(hostAddr);
        switch (len) {
            case 1:
                writeByte(addr, data);
                break;
            case 2:
                writeHalfWord(addr, data);
                break;
            case 4:
                writeWord(addr, data);
                break;
            default:
                Monitor.log.warn(String.format("Wrong access byte length: %d", len));
                break;
        }
           
    }
    
    protected void writeByte(int addr, int data) {
        int i = Byte.SIZE * (addr & 0b11);
        int mask = Wiring.maskInt(i, i + Byte.SIZE);
        alignedWrite(addr, (alignedRead(addr) & ~mask) | ((data << i) & mask));
    }

    protected void writeHalfWord(int addr, int data) {
        int i = Byte.SIZE * (addr & 0b10);
        int mask = Wiring.maskInt(i, i + Byte.SIZE);
        alignedWrite(addr, (alignedRead(addr) & ~mask) | (data << i));
    }

    protected void writeWord(int addr, int data) {
        alignedWrite(addr, data);
    }

}
