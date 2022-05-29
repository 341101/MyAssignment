package com.emulator;

import java.util.Timer;

import com.emulator.device.DeviceController;
import com.emulator.riscv32.ExecuteUnit;
import com.emulator.riscv32.RegFile;
import com.emulator.riscv32.StateRegs;

public class BUS {
    public final StateRegs STATE_REGS;
    public final DeviceController DEVICE_CONTROLLER;
    public final RegFile REG_FILE;
    public final ExecuteUnit EXECUTE_UNIT;
    // public final Interrupter INTERRUPTER;
    public final Timer TIMER_INTERRUPTER;

    public BUS() {
        this.STATE_REGS = new StateRegs(this);
        this.REG_FILE = new RegFile(this);
        this.EXECUTE_UNIT = new ExecuteUnit(this);
        this.DEVICE_CONTROLLER = new DeviceController(this);
        // this.INTERRUPTER = new Interrupter(this);
        this.TIMER_INTERRUPTER = new Timer();
    }
}
