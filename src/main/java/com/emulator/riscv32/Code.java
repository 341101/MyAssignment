package com.emulator.riscv32;

public abstract class Code {
    // opcode
    public static final int ALU     = 0b0110011;
    public static final int ALUi    = 0b0010011;
    public static final int load    = 0b0000011;
    public static final int store   = 0b0100011;
    public static final int branch  = 0b1100011;
    public static final int jal     = 0b1101111;
    public static final int jalr    = 0b1100111;
    public static final int lui     = 0b0110111;
    public static final int auipc   = 0b0010111;
    public static final int intr    = 0b1110011;
    
    public static final int trap    = 0b1101011;
    // **子指令 funct3**
    public static final int lb      = 0b000;
    public static final int lh      = 0b001;
    public static final int lw      = 0b010;
    public static final int lbu     = 0b100;
    public static final int lhu     = 0b101;
    
    public static final int sb      = 0b000;
    public static final int sh      = 0b001;
    public static final int sw      = 0b010;

    public static final int beq     = 0b000;
    public static final int bne     = 0b001;
    public static final int blt     = 0b100;
    public static final int bge     = 0b101;
    public static final int bltu    = 0b110;
    public static final int bgeu    = 0b111;

    // alui
    public static final int addi    = 0b000;
    public static final int slti    = 0b010;
    public static final int sltui   = 0b011;
    public static final int xori    = 0b100;
    public static final int ori     = 0b110;
    public static final int andi    = 0b111;
    
    public static final int slli    = 0b0000000001;
    public static final int srli    = 0b0000000101;
    public static final int srai    = 0b0100000101;
    

    // funct7 << 3 |  funct3
    public static final int add     = 0b0000000000;
    public static final int sub     = 0b0100000000;
    public static final int sll     = 0b0000000001;
    public static final int slt     = 0b0000000010;
    public static final int sltu    = 0b0000000011;
    public static final int xor     = 0b0000000100;
    public static final int srl     = 0b0000000101;
    public static final int sra     = 0b0100000101;
    public static final int or      = 0b0000000110;
    public static final int and     = 0b0000000111;

    // imm12
    public static final int ecall   = 0b000000000000;
    public static final int ebreak  = 0b000000000001;

    // **extend**
    // funct7 << 3 | funct3
    public static final int mul     = 0b0000001000;
    public static final int mulh    = 0b0000001001;
    public static final int mulhsu  = 0b0000001010;
    public static final int mulhu   = 0b0000001011;
    public static final int div     = 0b0000001100;
    public static final int divu    = 0b0000001101;
    public static final int rem     = 0b0000001110;
    public static final int remu    = 0b0000001111;
}
