package com.emulator.command;

import com.emulator.BUS;

public class Exit extends Command {
    @Override
    public State run(BUS bus, String[] args) {
        return State.EXIT;
    }
}
