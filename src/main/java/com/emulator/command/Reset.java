package com.emulator.command;

import com.emulator.BUS;

public class Reset extends Command {

    @Override
    public State run(BUS bus, String[] args) {
        resetReg(bus);
        return State.SUCCESSFUL;
    }

    private void resetReg(BUS bus) {
        for (int i = 0; i < bus.REG_FILE.size(); i++) {
            bus.REG_FILE.set(i, 0);
        }
    }
    
}
