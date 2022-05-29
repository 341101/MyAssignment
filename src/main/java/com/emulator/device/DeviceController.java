package com.emulator.device;

import com.emulator.BUS;
import com.emulator.Monitor;

public class DeviceController {
    private final Device[] DEVICE_LIST;
    public final BUS LINKED_BUS;

    public DeviceController(BUS bus) {
        this.LINKED_BUS = bus;
        this.DEVICE_LIST = new Device[] {
                new Memory(DeviceAddr.MEMORY_ADDR),
                new Serial(DeviceAddr.SERIAL_ADDR)
        };
    }
    
    public int load(int hostAddr, int len) {
        for (Device device : DEVICE_LIST) {
            if (device.access(hostAddr)) {
                return device.load(hostAddr, len);
            }
        }
        Monitor.log.warn(String.format("Segment fault: %08x", hostAddr));
        this.LINKED_BUS.EXECUTE_UNIT.stop();
        return 0;
    }

    public void write(int hostAddr, int len, int data) {
        for (Device device : DEVICE_LIST) {
            if (device.access(hostAddr)) {
                device.write(hostAddr, len, data);
                return;
            }
        }
        Monitor.log.warn(String.format("Segment fault: %08x", hostAddr));
        this.LINKED_BUS.EXECUTE_UNIT.stop();
    }
}
