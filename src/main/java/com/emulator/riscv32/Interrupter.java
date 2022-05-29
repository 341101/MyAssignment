package com.emulator.riscv32;

import java.util.Arrays;

import com.emulator.BUS;

public class Interrupter {
    public static final int SOURSES_NUMBER = 22;
    public int mcause; // 中断号
    public int mtvec; // 中断向量
    public int mepc; // 中断地址保存
    public StateRegs.PrivilegeMode MPP; // 特权等级
    public final BUS LINKED_BUS;
    // public int mstatus; // 状态
    public boolean MIE; // 允许中断
    public boolean MPIE;

    public final boolean[] mie; // 中断使能
    public final boolean[] mip; // 待处理的中断

    public Interrupter(BUS bus) {
        this.LINKED_BUS = bus;
        this.mie = new boolean[SOURSES_NUMBER]; // 22个中断源
        Arrays.fill(this.mie, true);
        this.mip = new boolean[SOURSES_NUMBER];
        this.MIE = true;
    }

    public void interruptHandler() {
        if (!this.MIE) {
            return;
        }
        int code = 0;
        // 按优先级处理中断请求
        while (!mip[code] && ++code < SOURSES_NUMBER);
        if (code >= SOURSES_NUMBER) {
            return;
        }
        mip[code] = false;
        this.mcause = code;

        // 保存被中断程序特权级别，设置特权级别为机器级
        this.MPP = this.LINKED_BUS.STATE_REGS.pmr;
        this.LINKED_BUS.STATE_REGS.pmr = StateRegs.PrivilegeMode.M;

        // 跳转至中断服务程序
        this.mepc = this.LINKED_BUS.STATE_REGS.pc;
        this.LINKED_BUS.STATE_REGS.pc = mtvec;

        // 关中断
        this.MPIE = this.MIE;
        this.MIE = false;
    }

    public void interruptReturn() {
        // 恢复中断前的特权级别
        this.LINKED_BUS.STATE_REGS.pmr = this.MPP;
        // 返回被打断的程序
        this.LINKED_BUS.STATE_REGS.pc = this.mepc;
        // 开中断
        this.MIE = this.MPIE;
        this.MPIE = false;
    }
}
