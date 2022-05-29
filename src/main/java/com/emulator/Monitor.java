package com.emulator;

import java.util.Scanner;

import com.emulator.command.Command;
import com.emulator.utility.Logger;

public class Monitor {

    public static final Logger log = Logger.getInstance();
    static int MainLoop(BUS bus) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            switch (commandHandler(scanner, bus)) {
                case SUCCESSFUL:
                    break;
                case FAIL:
                    log.info("The command execution failed.");
                    break;
                case NOT_FOUND:
                    log.info("The command was not found.");
                    break;
                case EXIT:
                    scanner.close();
                    return 0;
                default:
                    break;
            }
        }
    }

    static Command.State commandHandler(Scanner scanner, BUS bus) {
        System.out.print(">>");
        String str = scanner.nextLine().strip();
        int i = str.indexOf(' ');
        String commandString;
        String[] command_args = new String[0];
        if (i == -1) {
            commandString = str;
        } else {
            commandString = str.substring(0, i);
            command_args = str.substring(i, str.length()).strip().split("\\s+");
        }
        if (commandString.length() <= 0) {
            return Command.State.FAIL;
        }
        commandString = commandString.substring(0, 1).toUpperCase() + commandString.substring(1);
        try {
            Class<?> cls = Class.forName(Command.class.getPackageName() + '.' + commandString); // 反射获取命令所对应的对象
            Command command = (Command) cls.getDeclaredConstructor().newInstance();
            return command.run(bus, command_args);
        } catch (ClassNotFoundException e) {
            return Command.State.NOT_FOUND;
        } catch (Exception e) {
            return Command.State.FAIL;
        }
    }
    
}
