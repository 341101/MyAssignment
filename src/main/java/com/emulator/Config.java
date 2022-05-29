package com.emulator;

public final class Config {
    public static final int REG_FILE_SIZE = 32;
    public static final int MEMORY_CAPACITY = 1 << 18; // 256kb
    public static final int BASE_ADDR = 0x80000000;
    public static final String SYSTEM_PATH = System.getProperty("user.dir") + "/system/";
    public static final String PROGRAM_PATH = System.getProperty("user.dir") + "/program/";
    public static boolean debug = true;
}
