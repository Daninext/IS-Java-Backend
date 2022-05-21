package ru.itmo.kotiki.servicedata.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OwnerBuffer {
    private List<OwnerTransfer> buffer = new ArrayList<>();

    public OwnerBuffer() {
    }

    public OwnerBuffer(OwnerTransfer... buffer) {
        this.buffer = new ArrayList<>(Arrays.asList(buffer));
    }

    public OwnerBuffer(List<OwnerTransfer> buffer) {
        this.buffer = new ArrayList<>(buffer);
    }

    public List<OwnerTransfer> getBuffer() {
        return buffer;
    }
}
