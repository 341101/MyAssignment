package com.emulator.utility;

public abstract class Wiring {
    private static final int intAllMask = ~0;
    private static final long longAllMask = ~0l;
    public static int maskInt(int l, int h) {
        return (intAllMask >>> (Integer.SIZE - h)) & (intAllMask << l);
    }
    
    public static long maskLong(int l, int h) {
        return (longAllMask >>> (Integer.SIZE - h)) & (longAllMask << l);
    }

    public static int getBitsAround(int x, int start, int end) {
        int forward = Integer.SIZE - end;
        return (x << forward) >>> (start + forward);
    }

    public static int unsignExtend(int x, int len) {
        return x & maskInt(0, len);
    }

    public static long unsignExtend(int x) {
        return x & maskLong(0, Integer.SIZE);
    }

    public static int signExtend(int x, int l) {
        return (x & maskInt(l - 1, l)) == 0 ? x : x | maskInt(l, 32);
    }

}
