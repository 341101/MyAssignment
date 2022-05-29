package com.emulator;


public class Main {

    private static void welcome() {
        Monitor.log.info("Welcome to the RISC-V32 emulator.");
    }

    public static void main(String[] args) {
        welcome();
        for (String arg : args) {
            if (arg.equals("debug") || arg.equals("d")) {
                Config.debug = true;
            }
        }
        BUS bus = new BUS();
        if (Config.debug) {
            Monitor.MainLoop(bus);
        } else {
            bus.EXECUTE_UNIT.execute(-1);
        }
        
        System.exit(0);
    }

}
