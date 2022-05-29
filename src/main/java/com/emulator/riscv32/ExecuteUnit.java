package com.emulator.riscv32;

import com.emulator.BUS;
import com.emulator.Monitor;
import com.emulator.utility.Wiring;

public class ExecuteUnit {

    public final BUS LINKED_BUS;

    public int breakPoint;

    public ExecuteUnit(BUS bus) {
        this.LINKED_BUS = bus;
    }

    public int execute(int step) {
        long stepl = step;
        while (stepl-- != 0) {
            // LINKED_BUS.INTERRUPTER.interruptHandler(); // 中断处理
            fetchInstr();
            handleInstr();
            this.LINKED_BUS.STATE_REGS.pc = this.LINKED_BUS.STATE_REGS.npc;
            if (this.LINKED_BUS.STATE_REGS.state == StateRegs.CPUState.EXIT || this.LINKED_BUS.STATE_REGS.pc == this.breakPoint) {
                break;
            }
        }
        this.LINKED_BUS.STATE_REGS.state = StateRegs.CPUState.EXECUTE;
        return 0;
    }
    
    public void stop() {
        this.LINKED_BUS.STATE_REGS.state = StateRegs.CPUState.EXIT;
    }

    public void fetchInstr() {
        this.LINKED_BUS.STATE_REGS.i = new Decode(this.LINKED_BUS.DEVICE_CONTROLLER.load(this.LINKED_BUS.STATE_REGS.pc, 4));
        this.LINKED_BUS.STATE_REGS.npc = this.LINKED_BUS.STATE_REGS.pc + 4;
    }
    
    public void loadSrc() {
        
    }

    public void handleInstr() {
        Decode i = this.LINKED_BUS.STATE_REGS.i;
        switch (i.opcode) {
            case Code.ALU: // ALU寄存器数运算指令
                this.LINKED_BUS.REG_FILE.set(i.rd, handleALU(this.LINKED_BUS.REG_FILE.get(i.rs1), this.LINKED_BUS.REG_FILE.get(i.rs2), i.funct3 | (i.funct7 << 3)));
                break;
            case Code.ALUi: // ALU立即数运算指令
                this.LINKED_BUS.REG_FILE.set(i.rd, handleALUi(this.LINKED_BUS.REG_FILE.get(i.rs1), i.iImm, i.rs2, i.funct3, i.funct7));
                break;
            case Code.load: // 内存读指令
                this.LINKED_BUS.REG_FILE.set(i.rd, handleLoad(this.LINKED_BUS.REG_FILE.get(i.rs1) + i.iImm, i.funct3));
                break;
            case Code.store: // 内存写指令
                handleStore(this.LINKED_BUS.REG_FILE.get(i.rs1) + i.sImm, this.LINKED_BUS.REG_FILE.get(i.rs2), i.funct3);
                break;
            case Code.branch: // 分支指令
                if (handleBranch(this.LINKED_BUS.REG_FILE.get(i.rs1), this.LINKED_BUS.REG_FILE.get(i.rs2), i.funct3)) {
                    this.LINKED_BUS.STATE_REGS.npc = this.LINKED_BUS.STATE_REGS.pc + i.bImm;
                }
                break;
            case Code.jal: // jal 跳转链接
                this.LINKED_BUS.REG_FILE.set(i.rd, this.LINKED_BUS.STATE_REGS.pc + 4);
                this.LINKED_BUS.STATE_REGS.npc = this.LINKED_BUS.STATE_REGS.pc + i.jImm;
                break;
            case Code.jalr:
                this.LINKED_BUS.STATE_REGS.npc = (this.LINKED_BUS.REG_FILE.get(i.rs1) + i.iImm) & ~0b1;
                this.LINKED_BUS.REG_FILE.set(i.rd, this.LINKED_BUS.STATE_REGS.pc + 4);
                break;
            case Code.lui:
                this.LINKED_BUS.REG_FILE.set(i.rd, i.uImm);
                break;
            case Code.auipc:
                this.LINKED_BUS.REG_FILE.set(i.rd, this.LINKED_BUS.STATE_REGS.pc + i.uImm);
                break;
            case Code.intr:
                handleInterrupt(i.iImm, i.rs1, i.rs2);
                break;
            case Code.trap:
                Monitor.log.info(String.format("Trap at pc=%08x", this.LINKED_BUS.STATE_REGS.pc));
                stop();
                break;
            default:
                Monitor.log.error(String.format("Unknown Instruction: %s", Integer.toBinaryString(i.opcode)));
                stop();
                break;
        }
        
    }

    private int handleALU(int src1, int src2, int funct3_7) {
        switch (funct3_7) {
            case Code.add: // add, sub
                return src1 + src2;
            case Code.sub: // add, sub
                return src1 - src2;
            case Code.sll: // sll
                return src1 << src2;
            case Code.slt: // slt
                return src1 < src2 ? 1 : 0;
            case Code.sltu: // sltu
                return Wiring.unsignExtend(src1) < Wiring.unsignExtend(src2) ? 1 : 0;
            case Code.xor: // xor
                return src1 ^ src2;
            case Code.srl: // srl
                return src1 >>> src2;
            case Code.sra: // sra
                return src1 >> src2;
            case Code.or: // or
                return src1 | src2;
            case Code.and: // and
                return src1 & src2;

            // RV32M
            case Code.mul:
                return src1 * src2;
            case Code.mulh:
                return (int) (((long) src1 * (long) src2) >> Integer.SIZE);
            case Code.mulhsu:
                return (int) (((long) src1 * Wiring.unsignExtend(src2)) >> Integer.SIZE);
            case Code.mulhu:
                return (int) ((Wiring.unsignExtend(src1) * Wiring.unsignExtend(src2)) >> Integer.SIZE);
            case Code.div:
                return src1 / src2;
            case Code.divu:
                return (int) ((Wiring.unsignExtend(src1) / Wiring.unsignExtend(src2)));
            case Code.rem:
                return src1 % src2;
            case Code.remu:
                return (int) ((Wiring.unsignExtend(src1) % Wiring.unsignExtend(src2)));
            default:
                return 0;
        }
    }

    private int handleALUi(int src1, int imm12, int shamt, int funct3, int funct7) {
        switch (funct3) {
            case Code.addi: // add, sub
                return src1 + imm12;
            case Code.slti: // slt
                return src1 < imm12 ? 1 : 0;
            case Code.sltui: // sltu
                return Wiring.unsignExtend(src1) < Wiring.unsignExtend(imm12, 12) ? 1 : 0;
            case Code.xori: // xor
                return src1 ^ imm12;
            case Code.ori: // or
                return src1 | imm12;
            case Code.andi: // and
                return src1 & imm12;
        }
        switch (funct3 | (funct7 << 3)) {
            case Code.slli: // sll
                return src1 << shamt;
            case Code.srli: // srl
                return src1 >>> shamt;
            case Code.srai:
                return src1 >> shamt;
        }
        return 0;
    }

    private int handleLoad(int addr, int opcode) {
        switch (opcode) {
            case Code.lb:
                return Wiring.signExtend(this.LINKED_BUS.DEVICE_CONTROLLER.load(addr, 1), Byte.SIZE);
            case Code.lh:
                return Wiring.signExtend(this.LINKED_BUS.DEVICE_CONTROLLER.load(addr, 2), Short.SIZE);
            case Code.lw:
                return this.LINKED_BUS.DEVICE_CONTROLLER.load(addr, 4);
            case Code.lbu:
                return this.LINKED_BUS.DEVICE_CONTROLLER.load(addr, 1);
            case Code.lhu:
                return this.LINKED_BUS.DEVICE_CONTROLLER.load(addr, 2);
            default:
                return 0;
        }
    }

    private void handleStore(int addr, int data, int opcode) {
        switch (opcode) {
            case Code.sb:
                this.LINKED_BUS.DEVICE_CONTROLLER.write(addr, 1, data);
                break;
            case Code.sh:
                this.LINKED_BUS.DEVICE_CONTROLLER.write(addr, 2, data);
                break;
            case Code.sw:
                this.LINKED_BUS.DEVICE_CONTROLLER.write(addr, 4, data);
                break;
            default:
                break;
        }
    }

    private boolean handleBranch(int src1, int src2, int opcode) {
        switch (opcode) {
            case Code.beq: // beq
                return src1 == src2;
            case Code.bne: // bne
                return src1 != src2;
            case Code.blt: // blt
                return src1 < src2;
            case Code.bge: // bge
                return src1 >= src2;
            case Code.bltu: // bltu
                return Wiring.unsignExtend(src1) < Wiring.unsignExtend(src2);
            case Code.bgeu: // bgeu
                return Wiring.unsignExtend(src1) >= Wiring.unsignExtend(src2);
        }
        return false;
    }

    private void handleInterrupt(int opcode, int src1, int src2) {
        switch (opcode) {
            case Code.ecall: // ecall #a7 ret: a0, a1
                // 提出中断请求
                // this.LINKED_BUS.INTERRUPTER.mip[8 + this.LINKED_BUS.INTERRUPTER.MPP.ordinal()] = true;
                break;
            case Code.ebreak:
                // this.LINKED_BUS.INTERRUPTER.mip[3] = true;
                break;

            // 特权指令
            // case 0b0001000: // sret
            // break;
            // case 0b0011000: // mret
            // break;
            default:
                break;
        }
    }
}
