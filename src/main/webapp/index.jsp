
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Telecom Resource Optimization System</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background-color: #f2f5f9;
            color: #222;
        }

        header {
            background-color: #123456;
            color: white;
            padding: 25px;
            text-align: center;
        }

        .navbar {
            background: #0d2946;
            padding: 14px 20px;
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 18px;
        }

        .navbar a {
            color: white;
            text-decoration: none;
            font-weight: bold;
        }

        .navbar a:hover {
            text-decoration: underline;
        }

        .container {
            padding: 30px;
            max-width: 1400px;
            margin: auto;
        }

        .modules {
            display: grid;
            grid-template-columns: repeat(
                auto-fit, minmax(220px, 1fr)
            );
            gap: 20px;
        }

        .card {
            background: white;
            padding: 22px;
            border-radius: 8px;
            box-shadow: 0 2px 8px #00000015;
            border: 1px solid #e2e8f0;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .card:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 14px #00000020;
        }

        .card h3 {
            color: #123456;
            margin-top: 0;
        }

        .card p {
            line-height: 1.5;
        }

        .card a {
            display: inline-block;
            margin-top: 8px;
            color: #1769aa;
            font-weight: bold;
            text-decoration: none;
        }

        .card a:hover {
            text-decoration: underline;
        }

        .status {
            color: #18794e;
            font-weight: bold;
        }

        footer {
            text-align: center;
            padding: 20px;
            color: #666;
        }

        @media (max-width: 600px) {
            .container {
                padding: 18px;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Telecom Resource Optimization System</h1>
    <p>Network Resource Management and Monitoring</p>
</header>

<nav class="navbar">
    <a href="/dashboard">Dashboard</a>
    <a href="/network-cells">Network Cells</a>
    <a href="/devices">Devices</a>
    <a href="/subscribers">Subscribers</a>
    <a href="/traffic-flows">Traffic</a>
    <a href="/resource-allocation">Resource Allocation</a>
    <a href="/network-slices">Network Slices</a>
    <a href="/edge-servers">Edge Servers</a>
    <a href="/optimization">Optimization</a>
    <a href="/kpi-monitoring">KPI Monitoring</a>
    <a href="/alerts">Alerts</a>
    <a href="/reports">Reports</a>
</nav>

<div class="container">
    <h2>Web Dashboard</h2>
    <p>Welcome to the Telecom Network Management System.</p>
    <p class="status">Web Application Status: Running</p>

    <div class="modules">

        <div class="card">
            <h3>User Management</h3>
            <p>Manage system users and their roles.</p>
            <a href="/dashboard">Open Module</a>
        </div>

        <div class="card">
            <h3>Network Cells</h3>
            <p>View and manage telecom base stations.</p>
            <a href="/network-cells">Open Module</a>
        </div>

        <div class="card">
            <h3>Device Management</h3>
            <p>Manage devices connected to the network.</p>
            <a href="/devices">Open Module</a>
        </div>

        <div class="card">
            <h3>Subscriber Management</h3>
            <p>View and manage subscriber information.</p>
            <a href="/subscribers">Open Module</a>
        </div>

        <div class="card">
            <h3>Network Slices</h3>
            <p>Organize network resources for different services.</p>
            <a href="/network-slices">Open Module</a>
        </div>

        <div class="card">
            <h3>Edge Computing</h3>
            <p>Monitor edge servers and their resources.</p>
            <a href="/edge-servers">Open Module</a>
        </div>

        <div class="card">
            <h3>Traffic Monitoring</h3>
            <p>Monitor network traffic and data usage.</p>
            <a href="/traffic-flows">Open Module</a>
        </div>

        <div class="card">
            <h3>Resource Allocation</h3>
            <p>Manage bandwidth and frequency allocation.</p>
            <a href="/resource-allocation">Open Module</a>
        </div>

        <div class="card">
            <h3>Optimization</h3>
            <p>View optimization jobs and their results.</p>
            <a href="/optimization">Open Module</a>
        </div>

        <div class="card">
            <h3>KPI Monitoring</h3>
            <p>View recorded network performance indicators.</p>
            <a href="/kpi-monitoring">Open Module</a>
        </div>

        <div class="card">
            <h3>Alerts</h3>
            <p>View network alerts and warnings.</p>
            <a href="/alerts">Open Module</a>
        </div>

        <div class="card">
            <h3>Reports</h3>
            <p>View network performance summaries.</p>
            <a href="/reports">Open Module</a>
        </div>

    </div>
</div>

<footer>
    <p>Java Enterprise Application | Telecom Network Optimization</p>
</footer>

</body>
</html>
