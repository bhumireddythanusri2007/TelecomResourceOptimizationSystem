import {
  NetworkCell,
  Device,
  Subscriber,
  TrafficFlow,
  NetworkTraffic,
  ResourceAllocation,
  NetworkSlice,
  EdgeServer,
  OptimizationJob,
  OptimizationResult,
  NetworkKPI,
  UserSummary,
  Alert,
  DashboardStats,
  ReportSummary,
  SystemSnapshot
} from '../types';

export const DEMO_CELLS: NetworkCell[] = [
  { cellId: 101, cellName: 'Cell-Alpha-Metro-01', location: 'Downtown Central Station', totalCapacity: 1200, currentUsers: 940, status: 'Active' },
  { cellId: 102, cellName: 'Cell-Beta-Harbor-02', location: 'North Commercial Port', totalCapacity: 850, currentUsers: 620, status: 'Active' },
  { cellId: 103, cellName: 'Cell-Gamma-TechPark-03', location: 'Innovation District Building A', totalCapacity: 1500, currentUsers: 1420, status: 'Active' },
  { cellId: 104, cellName: 'Cell-Delta-Suburbs-04', location: 'West End Residential Area', totalCapacity: 600, currentUsers: 310, status: 'Active' },
  { cellId: 105, cellName: 'Cell-Epsilon-Airport-05', location: 'Terminal 2 Concourse C', totalCapacity: 2000, currentUsers: 1780, status: 'Active' },
  { cellId: 106, cellName: 'Cell-Zeta-Industrial-06', location: 'Eastern Logistics Hub', totalCapacity: 900, currentUsers: 150, status: 'Maintenance' },
  { cellId: 107, cellName: 'Cell-Eta-Stadium-07', location: 'Olympic Sports Arena', totalCapacity: 3500, currentUsers: 480, status: 'Inactive' },
  { cellId: 108, cellName: 'Cell-Theta-University-08', location: 'Central Campus Quad', totalCapacity: 1100, currentUsers: 890, status: 'Active' },
];

export const DEMO_DEVICES: Device[] = [
  { deviceId: 201, userId: 1, deviceName: 'BTS-Controller-Core-A1', deviceType: 'Base Station Controller', ipAddress: '10.240.12.15', connectionType: 'Fiber Backhaul', status: 'Online' },
  { deviceId: 202, userId: 2, deviceName: 'Edge-Gateway-Harbor-01', deviceType: 'Edge Router', ipAddress: '10.240.18.42', connectionType: '10G Fiber', status: 'Online' },
  { deviceId: 203, userId: 3, deviceName: 'Cell-Radio-RRU-TechPark', deviceType: 'Remote Radio Unit', ipAddress: '10.240.22.103', connectionType: 'CPRI Optical', status: 'Online' },
  { deviceId: 204, userId: 1, deviceName: 'IoT-Concentrator-Ind-01', deviceType: 'IoT Gateway', ipAddress: '10.240.35.88', connectionType: '4G LTE-M', status: 'Standby' },
  { deviceId: 205, userId: 4, deviceName: 'C-RAN-Aggregator-Airport', deviceType: 'BBU Pool Server', ipAddress: '10.240.50.21', connectionType: '40G WDM', status: 'Online' },
  { deviceId: 206, userId: 2, deviceName: 'Smart-Grid-Monitor-West', deviceType: 'Telemetry Sensor Hub', ipAddress: '10.240.66.12', connectionType: 'NB-IoT', status: 'Online' },
];

export const DEMO_SUBSCRIBERS: Subscriber[] = [
  { subscriberId: 301, subscriberName: 'TechCorp Enterprise Solutions', phoneNumber: '+1-555-019-2831', email: 'netops@techcorp.example.com', subscriptionType: 'Enterprise Dedicated', dataPlan: 'Unlimited 5G Priority', monthlyDataLimit: 5000, currentDataUsage: 3420.5, status: 'Active' },
  { subscriberId: 302, subscriberName: 'City Healthcare EMS Unit', phoneNumber: '+1-555-014-9823', email: 'dispatch@cityhealth.example.org', subscriptionType: 'Mission Critical', dataPlan: 'Ultra-Reliable Low-Latency', monthlyDataLimit: 2000, currentDataUsage: 890.2, status: 'Active' },
  { subscriberId: 303, subscriberName: 'Marcus Vance Logistics', phoneNumber: '+1-555-018-7744', email: 'mvance@logistics.example.net', subscriptionType: 'Commercial Fleet', dataPlan: 'IoT Telemetry Fleet 500GB', monthlyDataLimit: 500, currentDataUsage: 412.8, status: 'Active' },
  { subscriberId: 304, subscriberName: 'Elena Rostova', phoneNumber: '+1-555-012-3390', email: 'elena.rostova@example.com', subscriptionType: 'Consumer Premium', dataPlan: '5G Max 100GB', monthlyDataLimit: 100, currentDataUsage: 64.3, status: 'Active' },
  { subscriberId: 305, subscriberName: 'Metro Transit Autonomous Fleet', phoneNumber: '+1-555-017-6622', email: 'automation@transit.example.gov', subscriptionType: 'Enterprise V2X', dataPlan: 'C-V2X Guaranteed QoS', monthlyDataLimit: 8000, currentDataUsage: 6150.0, status: 'Active' },
  { subscriberId: 306, subscriberName: 'David K. Miller', phoneNumber: '+1-555-011-8841', email: 'dkmiller@example.org', subscriptionType: 'Consumer Standard', dataPlan: '4G LTE 50GB', monthlyDataLimit: 50, currentDataUsage: 49.8, status: 'Active' },
];

export const DEMO_TRAFFIC_FLOWS: TrafficFlow[] = [
  { flowId: 401, cellId: 101, sourceIp: '10.240.12.15', destinationIp: '198.51.100.22', protocol: 'TCP', dataVolume: 4280.5, flowDuration: 3600, packetCount: 1420800, flowStatus: 'Active' },
  { flowId: 402, cellId: 103, sourceIp: '10.240.22.103', destinationIp: '203.0.113.88', protocol: 'UDP', dataVolume: 8940.2, flowDuration: 1800, packetCount: 3890200, flowStatus: 'Active' },
  { flowId: 403, cellId: 105, sourceIp: '10.240.50.21', destinationIp: '198.51.100.14', protocol: 'TCP', dataVolume: 6120.0, flowDuration: 7200, packetCount: 2150000, flowStatus: 'Active' },
  { flowId: 404, cellId: 102, sourceIp: '10.240.18.42', destinationIp: '192.0.2.77', protocol: 'SCTP', dataVolume: 1240.8, flowDuration: 450, packetCount: 680000, flowStatus: 'Completed' },
  { flowId: 405, cellId: 104, sourceIp: '10.240.35.88', destinationIp: '198.51.100.5', protocol: 'UDP', dataVolume: 840.4, flowDuration: 900, packetCount: 320000, flowStatus: 'Active' },
  { flowId: 406, cellId: 108, sourceIp: '10.240.66.12', destinationIp: '203.0.113.41', protocol: 'TCP', dataVolume: 3450.1, flowDuration: 2400, packetCount: 1180000, flowStatus: 'Active' },
];

export const DEMO_NETWORK_TRAFFIC: NetworkTraffic[] = [
  { trafficId: 501, cellId: 101, dataUsage: 450.2, activeUsers: 940, trafficLoad: 78.3, recordedTime: '2026-10-30 11:45:00' },
  { trafficId: 502, cellId: 102, dataUsage: 310.8, activeUsers: 620, trafficLoad: 72.9, recordedTime: '2026-10-30 11:45:00' },
  { trafficId: 503, cellId: 103, dataUsage: 780.4, activeUsers: 1420, trafficLoad: 94.6, recordedTime: '2026-10-30 11:45:00' },
  { trafficId: 504, cellId: 104, dataUsage: 140.1, activeUsers: 310, trafficLoad: 51.6, recordedTime: '2026-10-30 11:45:00' },
  { trafficId: 505, cellId: 105, dataUsage: 920.6, activeUsers: 1780, trafficLoad: 89.0, recordedTime: '2026-10-30 11:45:00' },
  { trafficId: 506, cellId: 108, dataUsage: 410.5, activeUsers: 890, trafficLoad: 80.9, recordedTime: '2026-10-30 11:45:00' },
];

export const DEMO_RESOURCE_ALLOCATIONS: ResourceAllocation[] = [
  { allocationId: 601, cellId: 101, bandwidth: 100, frequency: 3500, allocatedUsers: 940 },
  { allocationId: 602, cellId: 102, bandwidth: 80, frequency: 2100, allocatedUsers: 620 },
  { allocationId: 603, cellId: 103, bandwidth: 120, frequency: 3500, allocatedUsers: 1420 },
  { allocationId: 604, cellId: 104, bandwidth: 60, frequency: 1800, allocatedUsers: 310 },
  { allocationId: 605, cellId: 105, bandwidth: 160, frequency: 3700, allocatedUsers: 1780 },
  { allocationId: 606, cellId: 108, bandwidth: 100, frequency: 2600, allocatedUsers: 890 },
];

export const DEMO_NETWORK_SLICES: NetworkSlice[] = [
  { sliceId: 701, sliceName: 'Slice-eMBB-HighThroughput', sliceType: 'Enhanced Mobile Broadband', bandwidth: 250, latencyRequirement: 12.0, connectedUsers: 2450, status: 'Active' },
  { sliceId: 702, sliceName: 'Slice-URLLC-AutonomousV2X', sliceType: 'Ultra-Reliable Low Latency', bandwidth: 80, latencyRequirement: 2.5, connectedUsers: 480, status: 'Active' },
  { sliceId: 703, sliceName: 'Slice-mMTC-SmartMetering', sliceType: 'Massive Machine Communications', bandwidth: 40, latencyRequirement: 60.0, connectedUsers: 14500, status: 'Active' },
  { sliceId: 704, sliceName: 'Slice-PublicSafety-Priority', sliceType: 'Mission Critical Voice/Video', bandwidth: 100, latencyRequirement: 5.0, connectedUsers: 160, status: 'Active' },
];

export const DEMO_EDGE_SERVERS: EdgeServer[] = [
  { edgeServerId: 801, serverName: 'Edge-Node-Downtown-MEC1', location: 'Central Exchange Facility', cpuCapacity: 128, memoryCapacity: 512, storageCapacity: 8000, connectedUsers: 890, status: 'Active' },
  { edgeServerId: 802, serverName: 'Edge-Node-Harbor-MEC2', location: 'Port Logistics Terminal', cpuCapacity: 64, memoryCapacity: 256, storageCapacity: 4000, connectedUsers: 340, status: 'Active' },
  { edgeServerId: 803, serverName: 'Edge-Node-Airport-MEC3', location: 'Aviation Telecom Center', cpuCapacity: 256, memoryCapacity: 1024, storageCapacity: 16000, connectedUsers: 1620, status: 'Active' },
  { edgeServerId: 804, serverName: 'Edge-Node-TechPark-MEC4', location: 'Innovation Quad Data Hub', cpuCapacity: 128, memoryCapacity: 512, storageCapacity: 8000, connectedUsers: 1240, status: 'Active' },
];

export const DEMO_OPTIMIZATION_JOBS: OptimizationJob[] = [
  { jobId: 901, jobName: 'Rule-Based Load Rebalance', optimizationType: 'RULE_BASED_SIMULATION', targetCellId: null, objective: 'Relative load reduction for active cells > 75%', inputLoad: 78.4, status: 'Completed', createdTime: '2026-10-30 11:30:15' },
  { jobId: 902, jobName: 'Peak Hours Congestion Relief', optimizationType: 'RULE_BASED_SIMULATION', targetCellId: 103, objective: 'Dynamic bandwidth boost for Cell-Gamma', inputLoad: 94.6, status: 'Completed', createdTime: '2026-10-30 10:15:22' },
  { jobId: 903, jobName: 'Airport Concourse Load Redistribution', optimizationType: 'RULE_BASED_SIMULATION', targetCellId: 105, objective: 'Prevent buffer congestion at Terminal 2', inputLoad: 89.0, status: 'Completed', createdTime: '2026-10-30 09:00:10' },
  { jobId: 904, jobName: 'Routine System Balanced Pass', optimizationType: 'RULE_BASED_SIMULATION', targetCellId: null, objective: 'Simulated 10% load reduction across network', inputLoad: 68.2, status: 'Completed', createdTime: '2026-10-30 08:00:00' },
];

export const DEMO_OPTIMIZATION_RESULTS: OptimizationResult[] = [
  { resultId: 1001, cellId: 101, previousLoad: 78.3, optimizedLoad: 70.3, allocatedBandwidth: 110, performanceScore: 88.5, optimizationTime: '2026-10-30 11:30:16' },
  { resultId: 1002, cellId: 102, previousLoad: 72.9, optimizedLoad: 64.9, allocatedBandwidth: 90, performanceScore: 85.2, optimizationTime: '2026-10-30 11:30:16' },
  { resultId: 1003, cellId: 103, previousLoad: 94.6, optimizedLoad: 82.6, allocatedBandwidth: 140, performanceScore: 92.4, optimizationTime: '2026-10-30 11:30:16' },
  { resultId: 1004, cellId: 105, previousLoad: 89.0, optimizedLoad: 77.0, allocatedBandwidth: 180, performanceScore: 90.1, optimizationTime: '2026-10-30 11:30:16' },
  { resultId: 1005, cellId: 108, previousLoad: 80.9, optimizedLoad: 68.9, allocatedBandwidth: 120, performanceScore: 87.8, optimizationTime: '2026-10-30 11:30:16' },
];

export const DEMO_KPIS: NetworkKPI[] = [
  { kpiId: 1101, cellId: 101, throughput: 485.4, latency: 14.2, packetLoss: 0.12, availability: 99.98, signalStrength: -78.4, recordedTime: '2026-10-30 11:45:00' },
  { kpiId: 1102, cellId: 102, throughput: 312.1, latency: 18.5, packetLoss: 0.35, availability: 99.85, signalStrength: -82.1, recordedTime: '2026-10-30 11:45:00' },
  { kpiId: 1103, cellId: 103, throughput: 890.7, latency: 11.8, packetLoss: 0.48, availability: 99.92, signalStrength: -72.6, recordedTime: '2026-10-30 11:45:00' },
  { kpiId: 1104, cellId: 104, throughput: 220.0, latency: 22.1, packetLoss: 0.08, availability: 99.99, signalStrength: -88.3, recordedTime: '2026-10-30 11:45:00' },
  { kpiId: 1105, cellId: 105, throughput: 960.3, latency: 9.4, packetLoss: 0.22, availability: 99.95, signalStrength: -69.2, recordedTime: '2026-10-30 11:45:00' },
  { kpiId: 1106, cellId: 108, throughput: 430.8, latency: 15.6, packetLoss: 0.18, availability: 99.90, signalStrength: -76.0, recordedTime: '2026-10-30 11:45:00' },
];

export const DEMO_USERS: UserSummary[] = [
  { userId: 1, username: 'admin_telecom', email: 'admin@telecom-ops.internal', role: 'ADMIN', status: 'Active', createdAt: '2026-08-15 08:00:00' },
  { userId: 2, username: 'net_engineer_alex', email: 'alex.chen@telecom-ops.internal', role: 'NETWORK_ENGINEER', status: 'Active', createdAt: '2026-08-20 09:30:00' },
  { userId: 3, username: 'opt_analyst_priya', email: 'priya.sharma@telecom-ops.internal', role: 'OPTIMIZATION_ANALYST', status: 'Active', createdAt: '2026-09-01 11:15:00' },
  { userId: 4, username: 'noc_monitor_john', email: 'john.smith@telecom-ops.internal', role: 'OPERATOR', status: 'Active', createdAt: '2026-09-10 14:00:00' },
  { userId: 5, username: 'audit_reviewer_elena', email: 'elena.r@telecom-ops.internal', role: 'AUDITOR', status: 'Active', createdAt: '2026-09-15 16:45:00' },
];

export const DEMO_ALERTS: Alert[] = [
  { alertId: 1201, cellId: 103, alertType: 'CONGESTION', alertMessage: 'Cell-Gamma-TechPark exceeds 94% capacity threshold', severity: 'CRITICAL', status: 'OPEN', createdTime: '2026-10-30 11:42:10' },
  { alertId: 1202, cellId: 105, alertType: 'HIGH_LATENCY', alertMessage: 'Spike in backhaul packet jitter observed at Airport Concourse C', severity: 'WARNING', status: 'OPEN', createdTime: '2026-10-30 11:20:44' },
  { alertId: 1203, cellId: 106, alertType: 'MAINTENANCE', alertMessage: 'Scheduled antenna azimuth recalibration underway', severity: 'INFO', status: 'INVESTIGATING', createdTime: '2026-10-30 10:00:00' },
  { alertId: 1204, cellId: 102, alertType: 'PACKET_LOSS', alertMessage: 'Intermittent signal degradation near Container Terminal 4', severity: 'WARNING', status: 'RESOLVED', createdTime: '2026-10-30 08:15:20' },
  { alertId: 1205, cellId: 107, alertType: 'NODE_OFFLINE', alertMessage: 'Stadium micro-cell inactive pending upcoming sporting event', severity: 'INFO', status: 'RESOLVED', createdTime: '2026-10-29 18:30:00' },
];

export const DEMO_STATS: DashboardStats = {
  networkCells: DEMO_CELLS.length,
  devices: DEMO_DEVICES.length,
  subscribers: DEMO_SUBSCRIBERS.length,
  trafficRecords: DEMO_NETWORK_TRAFFIC.length,
  alerts: DEMO_ALERTS.length,
  optimizationResults: DEMO_OPTIMIZATION_RESULTS.length,
  kpiRecords: DEMO_KPIS.length,
};

export const DEMO_REPORT: ReportSummary = {
  totalCells: DEMO_CELLS.length,
  trafficRecords: DEMO_NETWORK_TRAFFIC.length,
  resourceAllocations: DEMO_RESOURCE_ALLOCATIONS.length,
  optimizationResults: DEMO_OPTIMIZATION_RESULTS.length,
  kpiRecords: DEMO_KPIS.length,
  alerts: DEMO_ALERTS.length,
  openAlerts: DEMO_ALERTS.filter(a => a.status === 'OPEN').length,
  resolvedAlerts: DEMO_ALERTS.filter(a => a.status === 'RESOLVED').length,
  averageTrafficLoad: 77.88,
  averageLatency: 15.27,
  averageAvailability: 99.93,
};

export const DEMO_SNAPSHOT: SystemSnapshot = {
  timestamp: new Date().toISOString(),
  stats: DEMO_STATS,
  cells: DEMO_CELLS,
  devices: DEMO_DEVICES,
  subscribers: DEMO_SUBSCRIBERS,
  trafficFlows: DEMO_TRAFFIC_FLOWS,
  trafficRecords: DEMO_NETWORK_TRAFFIC,
  allocations: DEMO_RESOURCE_ALLOCATIONS,
  slices: DEMO_NETWORK_SLICES,
  edgeServers: DEMO_EDGE_SERVERS,
  jobs: DEMO_OPTIMIZATION_JOBS,
  results: DEMO_OPTIMIZATION_RESULTS,
  kpis: DEMO_KPIS,
  users: DEMO_USERS,
  alerts: DEMO_ALERTS,
  report: DEMO_REPORT,
};

export const EMPTY_SNAPSHOT: SystemSnapshot = {
  timestamp: new Date().toISOString(),
  stats: {
    networkCells: 0,
    devices: 0,
    subscribers: 0,
    trafficRecords: 0,
    alerts: 0,
    optimizationResults: 0,
    kpiRecords: 0,
  },
  cells: [],
  devices: [],
  subscribers: [],
  trafficFlows: [],
  trafficRecords: [],
  allocations: [],
  slices: [],
  edgeServers: [],
  jobs: [],
  results: [],
  kpis: [],
  users: [],
  alerts: [],
  report: {
    totalCells: 0,
    trafficRecords: 0,
    resourceAllocations: 0,
    optimizationResults: 0,
    kpiRecords: 0,
    alerts: 0,
    openAlerts: 0,
    resolvedAlerts: 0,
    averageTrafficLoad: 0,
    averageLatency: 0,
    averageAvailability: 0,
  },
};
