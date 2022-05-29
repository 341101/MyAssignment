package com.emulator.device;

import com.emulator.Config;

public class Memory extends Device {

    public Memory(int baseAddr) {
        super(baseAddr, Config.MEMORY_CAPACITY, true, true);
    }
    
}
