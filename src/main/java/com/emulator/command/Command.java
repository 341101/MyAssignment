package com.emulator.command;

import java.io.FileNotFoundException;

import com.emulator.BUS;

public class Command {
    public enum State {
        SUCCESSFUL,
        FAIL,
        NOT_FOUND,
        EXIT;
    }
    public State run(BUS bus, String[] args) throws FileNotFoundException {
        return State.SUCCESSFUL;
    }
}
