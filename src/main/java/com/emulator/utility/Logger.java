package com.emulator.utility;

public class Logger {
    private static Logger instance = null;

    private Logger() { }
    
    public static Logger getInstance() {
        if (Logger.instance == null) {
            Logger.instance = new Logger();
        }
        return Logger.instance;
    }

    public void info(String info) {
        System.out.printf("%s%s%s\n", Color.GREEN_B, info, Color.DEFAULT);
    }
    public void warn(String info) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        System.out.printf("%s[%s.%s():%d] %s%s\n", Color.YELLOW_B, caller.getClassName(), caller.getMethodName(), caller.getLineNumber(), info, Color.DEFAULT);
    }
    public void error(String info) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        System.out.printf("%s[%s.%s():%d] %s%s\n", Color.RED_B, caller.getClassName(), caller.getMethodName(), caller.getLineNumber(), info, Color.DEFAULT);
    }
}
