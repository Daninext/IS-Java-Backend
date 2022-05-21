package ru.itmo.kotiki.servicedata.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatBuffer {
    private List<CatTransfer> buffer = new ArrayList<>();
    private String reqRole = null;
    private Integer reqId = 0;

    public CatBuffer() {
    }

    public CatBuffer(List<CatTransfer> buffer, String role, Integer id) {
        this.buffer = new ArrayList<>(buffer);
        reqRole = role;
        reqId = id;
    }

    public CatBuffer(String role, Integer id, CatTransfer... buffer) {
        this.buffer = new ArrayList<>(Arrays.asList(buffer));
        reqRole = role;
        reqId = id;
    }

    public List<CatTransfer> getBuffer() {
        return buffer;
    }

    public String getReqRole() {
        return reqRole;
    }

    public Integer getReqId() {
        return reqId;
    }
}
