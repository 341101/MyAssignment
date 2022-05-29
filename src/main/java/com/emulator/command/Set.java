package com.emulator.command;

import com.emulator.BUS;
import com.emulator.riscv32.RegFile;

public class Set extends Command {

    @Override
    public State run(BUS bus, String[] args) {
        for (int i = 0; i < 2 * (args.length / 2); i += 2) {
            int value = (int) Long.parseLong(args[i + 1], 16);
            setReg(bus, args[i], value);
        }
        return State.SUCCESSFUL;
    }

    void setReg(BUS bus, String name, int value) {
        if (name.equals("pc")) {
            bus.STATE_REGS.pc = value;
            return;
        }
        int rd = RegFile.Name.valueOf(name).ordinal();
        bus.REG_FILE.set(rd, value);
    }

}
