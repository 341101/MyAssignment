package com.emulator.device;


public class Serial extends Device {
    public Serial(int baseAddr) {
        super(baseAddr, 8, true, true);
    }

    @Override
    public void write(int hostAddr, int len, int data) {
        super.write(hostAddr, len, data);
        update();
    }

    @Override
    protected void update() {
        for (int i = 0; i < BASE_ADDR - END_ADDR; i++) {
            System.out.print((char)loadByte(i));
        }
    }
}
