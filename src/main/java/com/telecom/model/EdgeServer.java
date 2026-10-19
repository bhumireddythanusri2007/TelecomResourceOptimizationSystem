package com.telecom.model;

public class EdgeServer {

    private int edgeServerId;
    private String serverName;
    private String location;
    private int cpuCapacity;
    private int memoryCapacity;
    private int storageCapacity;
    private int connectedUsers;
    private String status;

    public EdgeServer() {
    }

    public EdgeServer(
            int edgeServerId,
            String serverName,
            String location,
            int cpuCapacity,
            int memoryCapacity,
            int storageCapacity,
            int connectedUsers,
            String status) {

        this.edgeServerId = edgeServerId;
        this.serverName = serverName;
        this.location = location;
        this.cpuCapacity = cpuCapacity;
        this.memoryCapacity = memoryCapacity;
        this.storageCapacity = storageCapacity;
        this.connectedUsers = connectedUsers;
        this.status = status;
    }

    public int getEdgeServerId() {
        return edgeServerId;
    }

    public void setEdgeServerId(int edgeServerId) {
        this.edgeServerId = edgeServerId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCpuCapacity() {
        return cpuCapacity;
    }

    public void setCpuCapacity(int cpuCapacity) {
        this.cpuCapacity = cpuCapacity;
    }

    public int getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(int memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public int getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(int connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}