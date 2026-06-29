package com.project.MultiDbQueryRunner.model;

import java.util.List;

public class MachineConfig {
    private String machineName;
    private String ip;
    private int port;
    private String username;
    private String password;
    private List<ClientConfig> clients;

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<ClientConfig> getClients() { return clients; }
    public void setClients(List<ClientConfig> clients) { this.clients = clients; }
}