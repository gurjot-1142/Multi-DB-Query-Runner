package com.project.MultiDbQueryRunner.model;

public class ClientConfig {
    private String clientName;
    private String database;
    private boolean enabled;

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}