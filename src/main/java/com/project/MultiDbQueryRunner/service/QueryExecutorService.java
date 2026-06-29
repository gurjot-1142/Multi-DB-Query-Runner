package com.project.MultiDbQueryRunner.service;

import com.project.MultiDbQueryRunner.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.MultiDbQueryRunner.model.ClientConfig;
import com.project.MultiDbQueryRunner.model.DbConfig;
import com.project.MultiDbQueryRunner.model.MachineConfig;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Service
public class QueryExecutorService {

    private static final String LOG_FILE = "logs/latest-run.log";

    public void execute() throws Exception {

        clearOldLogs();

        ObjectMapper mapper = new ObjectMapper();
        DbConfig config = mapper.readValue(new File("config/db-config.json"), DbConfig.class);

        List<Map<String, Object>> finalResults = new ArrayList<>();

        int success = 0;
        int failed = 0;

        long start = System.currentTimeMillis();

        log("========== SCRIPT STARTED ==========");

        for (MachineConfig machine : config.getMachines()) {

            String url = "jdbc:sqlserver://" + machine.getIp()
                    + ":" + machine.getPort()
                    + ";databaseName=master"
                    + ";encrypt=false;trustServerCertificate=true";

            log("Connecting to " + machine.getMachineName());

            try (Connection conn = DriverManager.getConnection(
                    url,
                    machine.getUsername(),
                    machine.getPassword())) {

                log("Connected to " + machine.getMachineName());

                for (ClientConfig client : machine.getClients()) {

                    if (!client.isEnabled()) continue;

                    Map<String, Object> result = new HashMap<>();
                    result.put("clientName", client.getClientName());
                    result.put("database", client.getDatabase());

                    try (Statement stmt = conn.createStatement()) {

                        stmt.setQueryTimeout(30);

                        stmt.execute("USE [" + client.getDatabase() + "]");

                        try (ResultSet rs = stmt.executeQuery(config.getQuery())) {

                            List<Map<String, Object>> rows = extractRows(rs);

                            result.put("status", "SUCCESS");
                            result.put("resultCount", rows.size());
                            result.put("rows", rows);

                            success++;

                            log("SUCCESS -> " + client.getClientName()
                                    + " rows=" + rows.size());
                        }

                    } catch (Exception ex) {

                        result.put("status", "FAILED");
                        result.put("error", ex.getMessage());

                        failed++;

                        log("FAILED -> " + client.getClientName()
                                + " error=" + ex.getMessage());
                    }

                    finalResults.add(result);
                }

            } catch (Exception ex) {
                log("Machine connection failed: " + machine.getMachineName());
            }

            log("Disconnected from " + machine.getMachineName());
        }

        long end = System.currentTimeMillis();

        Map<String, Object> output = new HashMap<>();
        output.put("success", success);
        output.put("failed", failed);
        output.put("executionTimeMs", end - start);
        output.put("results", finalResults);

        new File("results").mkdirs();

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("results/output.json"), output);

        log("Success Count: " + success);
        log("Failure Count: " + failed);
        log("Execution Time: " + (end - start) + " ms");
        log("========== SCRIPT COMPLETED ==========");
    }

    private List<Map<String, Object>> extractRows(ResultSet rs) throws Exception {
        List<Map<String, Object>> rows = new ArrayList<>();

        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();

            for (int i = 1; i <= cols; i++) {
                row.put(meta.getColumnName(i), rs.getObject(i));
            }

            rows.add(row);
        }

        return rows;
    }

    private void clearOldLogs() throws Exception {
        File file = new File(LOG_FILE);
        file.getParentFile().mkdirs();
        new PrintWriter(file).close();
    }

    private void log(String msg) throws Exception {
        Files.write(
                Paths.get(LOG_FILE),
                (new Date() + " | " + msg + "\n").getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );

        System.out.println(msg);
    }
}