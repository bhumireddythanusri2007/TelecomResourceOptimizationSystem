# Telecom Network Resource Management & Monitoring System

**Design and Development of a Telecom Network Resource Management and Monitoring System using Java Enterprise Technologies**

## Overview

The Telecom Network Resource Management & Monitoring System is a web-based application developed to provide a centralized view of telecom network infrastructure and its performance.

The project brings together network infrastructure monitoring, traffic analysis, resource allocation, 5G network slice information, edge server monitoring, and performance reporting in a single dashboard.

It uses a React-based frontend connected to a Java enterprise backend, with MySQL for storing and retrieving network-related information. The application also includes a rule-based optimization module that evaluates network load and records simulated resource optimization results.

## Objectives

- Provide a centralized interface for monitoring telecom network infrastructure.
- Track the status and utilization of network cells, devices, and subscribers.
- Monitor traffic flows and resource allocation.
- Display information about 5G network slices and edge servers.
- Analyze network performance using key performance indicators.
- Identify and track network alerts and incidents.
- Evaluate resource utilization through rule-based optimization.
- Generate network performance reports and audit summaries.
- Organize system users through role-based access control.

## Technology Stack

| Layer | Technologies |
|---|---|
| Frontend | React, TypeScript, Vite |
| UI and Styling | Tailwind CSS, Lucide React |
| Charts | Recharts |
| Backend | Java, Jakarta EE 10, Servlets |
| Server | Jetty 12 |
| Database Connectivity | JDBC |
| Database | MySQL |
| Build Tool | Maven |
| Frontend Deployment | Vercel |
| Backend Deployment | Render |
| Database Hosting | Aiven |
| Version Control | Git and GitHub |

## Application Modules

The application is organized into 13 modules.

| Module | Description |
|---|---|
| Network Overview | Displays a consolidated view of network status and operational metrics. |
| Network Cells | Provides information about base stations, cell status, capacity, and network load. |
| Devices | Displays connected devices, device types, network connectivity, and status. |
| Subscribers | Maintains subscriber information, service plans, and data usage. |
| Traffic Flows | Displays traffic records, active sessions, traffic volume, and protocol information. |
| Resource Allocation | Presents bandwidth allocation and resource utilization information. |
| Network Slices | Displays slice configurations, bandwidth, latency requirements, and connected users. |
| Edge Servers | Provides information about edge computing resources, including CPU, memory, storage, and offloaded sessions. |
| Optimization Engine | Evaluates network load using predefined rules and generates simulated optimization results. |
| KPI Monitoring | Displays throughput, latency, packet loss, network availability, and signal strength. |
| Network Alerts | Tracks network incidents, severity levels, diagnostic messages, and resolution status. |
| Reports & Audits | Presents consolidated performance summaries and supports PDF export. |
| User Directory | Displays system accounts, user roles, account status, and access-control information. |

## System Architecture

The application follows a three-tier architecture.

**Frontend Layer**

The frontend is developed using React and TypeScript. It provides the dashboard, navigation between modules, performance visualizations, and interaction with backend services.

**Backend Layer**

The backend uses Java with Jakarta EE 10 Servlets and Jetty 12. It handles application requests, processes operations, and communicates with the database through JDBC.

**Database Layer**

MySQL stores the application's network records, including cells, devices, subscribers, traffic flows, resource allocations, network slices, edge servers, alerts, KPI records, and optimization results.

### Deployment Architecture

```text
          User
           |
           v
   React Frontend
      (Vercel)
           |
           v
    Java Backend
   (Render / Jetty)
           |
           v
     JDBC Connection
           |
           v
     MySQL Database
        (Aiven)
```

## Resource Optimization

The Optimization Engine uses a rule-based simulation approach to evaluate network cell load.

Based on predefined conditions, the engine generates simulated resource allocation adjustments and records the resulting optimization information.

The dashboard presents optimization runs, simulated load changes, optimized load values, and performance scores.

The current implementation is a simulation-based approach. It does not use a quantum computer, a QUBO solver, or direct control of physical telecom infrastructure.

## Running the Application Locally

### Requirements

- Java Development Kit (JDK)
- Maven
- Node.js and npm
- MySQL Server

### Frontend

Navigate to the frontend folder:

```bash
cd frontend
```

Install the required packages:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend is available at:

```text
http://localhost:5173
```

### Backend

The Java backend is configured to run using the project's Maven and Jetty setup.

Configure the database connection using the required environment variables:

```text
DB_URL=your_mysql_jdbc_url
DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password
```

Build and run the backend using the Maven configuration provided in the project.

The local backend runs at:

```text
http://localhost:8080
```

### Database Configuration

Create or configure a MySQL database and provide the corresponding connection details through environment variables.

Do not commit database passwords, `.env` files, or other confidential configuration details to the repository.

## Deployment

The application is deployed across three services:

- **Vercel:** Hosts the React frontend.
- **Render:** Hosts the Java backend.
- **Aiven:** Provides the hosted MySQL database.

**Live Application:**  
https://telecom-resource-optimization-syste-five.vercel.app/

## Future Scope

- Integration with real-time network telemetry.
- Predictive analysis of network performance.
- Improvements to resource allocation algorithms.
- More detailed monitoring of network infrastructure.
- Enhanced user and access management.
- Integration with physical telecom network systems.

## Project Details

**Project:** Design and Development of a Telecom Network Resource Management and Monitoring System using Java Enterprise Technologies

**Project Area:** Telecom Network Management, Resource Monitoring, Enterprise Java Technologies

**Project Type:** Academic Design and Development Project

---

*Note: This application is developed for academic and demonstration purposes. Some network records and optimization results are simulated and should not be considered actual measurements from a telecom operator's live infrastructure.*
