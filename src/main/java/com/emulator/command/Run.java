package com.emulator.command;

import com.emulator.BUS;

public class Run extends Command {
    @Override
    public State run(BUS bus, String[] args) {
        int step = 1;
        if (args.length > 0) {
            step = Integer.parseInt(args[0]);
        }
        exec(bus, step);
        return State.SUCCESSFUL;
    }

    private void exec(BUS bus, int step) {
        bus.EXECUTE_UNIT.execute(step);
    }

}
