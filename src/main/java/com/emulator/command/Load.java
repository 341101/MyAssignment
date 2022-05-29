package com.emulator.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.emulator.BUS;
import com.emulator.Config;

public class Load extends Command {

    @Override
    public State run(BUS bus, String[] args) {
        if (args.length == 2) {
            String filePath = Config.PROGRAM_PATH + args[0];
            int addr = (int) Long.parseLong(args[1], 16);
            loadFile(bus, filePath, addr);
        } else {
            return State.FAIL;
        }
        
        return State.SUCCESSFUL;
    }

    private void loadFile(BUS bus, String filePath, int addr) {
        FileInputStream fileReader;
        try {
            fileReader = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            return;
            //TODO: handle exception
        }
        
        byte[] word = new byte[4];
        try {
            while (fileReader.read(word) != -1) {
                for (byte b : word) {
                    bus.DEVICE_CONTROLLER.write(addr++, 1, b);
                }
            }
            fileReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
