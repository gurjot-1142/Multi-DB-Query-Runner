package com.project.MultiDbQueryRunner.model;

import java.util.List;

public class DbConfig {
    private String query;
    private List<MachineConfig> machines;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public List<MachineConfig> getMachines() { return machines; }
    public void setMachines(List<MachineConfig> machines) { this.machines = machines; }
}