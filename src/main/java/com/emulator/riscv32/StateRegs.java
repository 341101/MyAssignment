package com.emulator.riscv32;

import com.emulator.BUS;
import com.emulator.Config;

public class StateRegs {
    public int pc;
    public int npc; // 下一条指令地址
    public final BUS LINKED_BUS;
    public CPUState state;
    public PrivilegeMode pmr; // 当前特权级别
    public Decode i;

    public enum PrivilegeMode {
        U, S, H, M;
    }

    public enum CPUState {
        EXECUTE, EXIT;
    }

    public StateRegs(BUS bus) {
        this.pc = Config.BASE_ADDR;
        this.LINKED_BUS = bus;
    }

}
