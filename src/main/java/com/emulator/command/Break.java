package com.emulator.command;

import com.emulator.BUS;

public class Break extends Command {
    public State run(BUS bus, String[] args) {
        if (args.length < 1) {
            return State.FAIL;
        }
        int addr = Integer.parseInt(args[0], 16) & ~0b11;
        setBreakPoint(bus, addr);
        return State.SUCCESSFUL;
    }

    private void setBreakPoint(BUS bus, int addr) {
        bus.EXECUTE_UNIT.breakPoint = addr;
    }
}
