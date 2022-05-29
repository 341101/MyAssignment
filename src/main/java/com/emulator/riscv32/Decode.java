package com.emulator.riscv32;

import com.emulator.utility.Wiring;

public class Decode {
    public int opcode;
    public int rd;
    public int rs1;
    public int rs2;
    public int funct3;
    public int funct7;

    public int sImm; // unsign extend
    public int iImm; // sign extend
    public int uImm; // sign extend
    public int bImm; // Sign extend
    public int jImm; // Sign extend

    public Decode(int inst) {
        this.opcode     = Wiring.getBitsAround(inst, 0, 7);
        this.rd         = Wiring.getBitsAround(inst, 7, 12);
        this.rs1        = Wiring.getBitsAround(inst, 15, 20);
        this.rs2        = Wiring.getBitsAround(inst, 20, 25);
        this.funct3     = Wiring.getBitsAround(inst, 12, 15);
        this.funct7     = Wiring.getBitsAround(inst, 25, 32);

        this.iImm       = Wiring.signExtend(Wiring.getBitsAround(inst, 20, 32), 12);
        this.uImm       = Wiring.getBitsAround(inst, 12, 32) << 12;

        int sImm0_5     = Wiring.getBitsAround(inst, 7, 12);
        int sImm5_12    = Wiring.getBitsAround(inst, 25, 32) << 5;
        this.sImm       = Wiring.signExtend(sImm0_5 | sImm5_12, 12);

        int bImm1_5     = Wiring.getBitsAround(inst, 8, 12)  << 1;
        int bImm5_11    = Wiring.getBitsAround(inst, 25, 31) << 5;
        int bImm11_12   = Wiring.getBitsAround(inst, 7, 8)   << 11;
        int bImm12_13   = Wiring.getBitsAround(inst, 31, 32) << 12;
        this.bImm       = Wiring.signExtend(bImm1_5 | bImm5_11 | bImm11_12 | bImm12_13, 13);

        int jImm1_11    = Wiring.getBitsAround(inst, 21, 31) << 1;
        int jImm11_12   = Wiring.getBitsAround(inst, 20, 21) << 11;
        int jImm12_20   = Wiring.getBitsAround(inst, 12, 20) << 12;
        int jImm20_21   = Wiring.getBitsAround(inst, 31, 32) << 20;
        this.jImm       = Wiring.signExtend(jImm1_11 | jImm11_12 | jImm12_20 | jImm20_21, 21);
    }

}
