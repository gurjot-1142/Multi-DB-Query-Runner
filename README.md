# Multi DB Query Runner

Multi DB Query Runner is a Spring Boot utility for executing the same SQL query across multiple SQL Server databases hosted on multiple machines.

It is useful for:

* Cross-client data verification
* Reporting
* Data audits
* Debugging production issues across multiple databases

---

## Features

* Execute a single SQL query across multiple client databases
* Supports multiple SQL Server machines
* Reuses machine connections for efficiency
* Logs execution details for every run
* Generates structured JSON output
* Captures failures without stopping execution
* Clears previous logs before every run

---

## Tech Stack

* Java 17 / 21
* Spring Boot
* JDBC
* SQL Server
* Maven
* Jackson

---

## Project Structure

```text
multi-db-query-runner/
├── config/
│   └── db-config.json
│
├── logs/
│   └── latest-run.log
│
├── results/
│   └── output.json
│
└── src/main/java/
    ├── model/
    ├── service/
    └── Application.java
```

---

## Configuration

Update database configuration in:

```text
config/db-config.json
```

Example:

```json
{
  "query": "SELECT TOP 5 * FROM USERS",
  "machines": [
    {
      "machineName": "Machine1",
      "ip": "10.10.1.1",
      "port": 1433,
      "username": "sa",
      "password": "password",
      "clients": [
        {
          "clientName": "Client A",
          "database": "client_a_db",
          "enabled": true
        }
      ]
    }
  ]
}
```

---

## How It Works

1. Reads machine and database configuration from JSON
2. Connects to each SQL Server machine
3. Switches to each configured client database
4. Executes the configured SQL query
5. Stores results in JSON format
6. Generates execution logs

---

## Running the Project

Using Maven:

```bash
mvn spring-boot:run
```

Or run the main application class from your IDE.

---

## Output

### Logs

Generated at:

```text
logs/latest-run.log
```

Contains:

* Start time
* Machine connection status
* Query execution logs
* Success/failure logs
* Execution summary

---

### Results

Generated at:

```text
results/output.json
```

Example:

```json
{
  "success": 79,
  "failed": 1,
  "executionTimeMs": 18342,
  "results": [
    {
      "clientName": "Client A",
      "database": "client_a_db",
      "status": "SUCCESS",
      "resultCount": 5,
      "rows": []
    }
  ]
}
```

---

## Notes

* Intended for SQL SELECT queries
* Recommended for small-to-medium result sets
* Connections are properly closed after execution
* Failed databases do not stop overall execution
