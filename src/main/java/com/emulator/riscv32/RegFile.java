package com.emulator.riscv32;

import com.emulator.BUS;
import com.emulator.Config;

public final class RegFile {
    private final int[] REG_FILE;
    public final BUS LINKED_BUS;

    public RegFile(BUS bus) {
        this.LINKED_BUS = bus;
        this.REG_FILE = new int[Config.REG_FILE_SIZE - 1];
    }

    public int get(int i) {
        return i == 0 ? 0 : REG_FILE[i - 1];
    }

    public void set(int i, int value) {
        if (i == 0) {
            return;
        }
        REG_FILE[i - 1] = value;
    }

    public int size(){
        return Config.REG_FILE_SIZE;
    }

    public enum Name {
        zero, ra,  sp,  gp,  tp, t0,  t1,  t2,
        s0,   s1,  a0,  a1,  a2, a3,  a4,  a5,
        a6,   a7,  s2,  s3,  s4, s5,  s6,  s7,
        s8,   s9,  s10, s11, t3, t4,  t5,  t6;
    }
}
