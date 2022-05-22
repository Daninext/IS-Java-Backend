package ru.itmo.kotiki.servicedata.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OwnerBuffer {
    private List<OwnerTransfer> owners = new ArrayList<>();

    public OwnerBuffer() {
    }

    public OwnerBuffer(OwnerTransfer... owners) {
        this.owners = new ArrayList<>(Arrays.asList(owners));
    }

    public OwnerBuffer(List<OwnerTransfer> owners) {
        this.owners = new ArrayList<>(owners);
    }

    public List<OwnerTransfer> getOwners() {
        return owners;
    }
}
