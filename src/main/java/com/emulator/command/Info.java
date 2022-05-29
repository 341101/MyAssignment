package com.emulator.command;

import com.emulator.BUS;
import com.emulator.riscv32.RegFile;
import com.emulator.utility.Color;

public class Info extends Command {
    @Override
    public State run(BUS bus, String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "r":
                    registersDisplay(bus);
                    break;

                case "m":
                    if (i + 1 < args.length) {
                        int addr = (int) Long.parseLong(args[i + 1],16);
                        memoryDisplay(bus, addr, 128, 0);
                        i++;
                    }
                    break;
                default:
                    return State.FAIL;
            }
        }
        return State.SUCCESSFUL;
    }
    public void registersDisplay(BUS bus) {
        System.out.printf(Color.GREEN_B + "Registers info:" + Color.DEFAULT);
        for (int i = 0; i < bus.REG_FILE.size(); i++) {
            if (i % 8 == 0) {
                System.out.printf("\n");
            }
            System.out.printf(Color.BLUE_B + "%4s:\033[39m%08x ", RegFile.Name.values()[i], bus.REG_FILE.get(i));
        }
        System.out.printf("| \033[94mpc:\033[39m%08x\n", bus.STATE_REGS.pc);
    }

    public void memoryDisplay(BUS bus, int addr, int size, int mode) {
        System.out.printf(Color.GREEN_B + "Memory info:" + Color.DEFAULT);
        for (int i = addr; i < addr + size; i += 4) {
            if ((i - addr) % 16 == 0) {
                if ((i - addr) % 32 == 0) {
                    System.out.printf("\n");
                }
                System.out.printf(Color.BLUE_B + "%08x: " + Color.DEFAULT, i);
            }
            System.out.printf("%08x ", bus.DEVICE_CONTROLLER.load(i, 4));
        }
        System.out.printf("\n");
    }
}
