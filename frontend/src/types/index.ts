export type DataSourceMode = 'LIVE' | 'DEMO' | 'CACHED' | 'OFFLINE';

export type NavModule =
  | 'overview'
  | 'network-cells'
  | 'devices'
  | 'subscribers'
  | 'traffic'
  | 'resource-allocation'
  | 'network-slices'
  | 'edge-servers'
  | 'optimization'
  | 'kpi-monitoring'
  | 'users'
  | 'alerts'
  | 'reports';

export interface NetworkCell {
  cellId: number;
  cellName: string;
  location: string;
  totalCapacity: number;
  currentUsers: number;
  status: 'Active' | 'Inactive' | 'Maintenance';
}

export interface Device {
  deviceId: number;
  userId: number;
  deviceName: string;
  deviceType: string;
  ipAddress: string;
  connectionType: string;
  status: string;
}

export interface Subscriber {
  subscriberId: number;
  subscriberName: string;
  phoneNumber: string;
  email: string;
  subscriptionType: string;
  dataPlan: string;
  monthlyDataLimit: number;
  currentDataUsage: number;
  status: string;
}

export interface NetworkTraffic {
  trafficId: number;
  cellId: number;
  dataUsage: number;
  activeUsers: number;
  trafficLoad: number;
  recordedTime: string;
}

export interface TrafficFlow {
  flowId: number;
  cellId: number;
  sourceIp: string;
  destinationIp: string;
  protocol: string;
  dataVolume: number;
  flowDuration: number;
  packetCount: number;
  flowStatus: string;
}

export interface ResourceAllocation {
  allocationId: number;
  cellId: number;
  bandwidth: number;
  frequency: number;
  allocatedUsers: number;
}

export interface NetworkSlice {
  sliceId: number;
  sliceName: string;
  sliceType: string;
  bandwidth: number;
  latencyRequirement: number;
  connectedUsers: number;
  status: string;
}

export interface EdgeServer {
  edgeServerId: number;
  serverName: string;
  location: string;
  cpuCapacity: number;
  memoryCapacity: number;
  storageCapacity: number;
  connectedUsers: number;
  status: string;
}

export interface OptimizationJob {
  jobId: number;
  jobName: string;
  optimizationType: string;
  targetCellId: number | null;
  objective: string;
  inputLoad: number;
  status: string;
  createdTime: string;
}

export interface OptimizationResult {
  resultId: number;
  cellId: number;
  previousLoad: number;
  optimizedLoad: number;
  allocatedBandwidth: number;
  performanceScore: number;
  optimizationTime: string;
}

export interface NetworkKPI {
  kpiId: number;
  cellId: number;
  throughput: number;
  latency: number;
  packetLoss: number;
  availability: number;
  signalStrength: number;
  recordedTime: string;
}

export interface UserSummary {
  userId: number;
  username: string;
  email: string;
  role: string;
  status: string;
  createdAt: string;
}

export interface Alert {
  alertId: number;
  cellId: number;
  alertType: string;
  alertMessage: string;
  severity: 'CRITICAL' | 'WARNING' | 'INFO';
  status: 'OPEN' | 'RESOLVED' | 'INVESTIGATING';
  createdTime: string;
}

export interface DashboardStats {
  networkCells: number;
  devices: number;
  subscribers: number;
  trafficRecords: number;
  alerts: number;
  optimizationResults: number;
  kpiRecords: number;
}

export interface ReportSummary {
  totalCells: number;
  trafficRecords: number;
  resourceAllocations: number;
  optimizationResults: number;
  kpiRecords: number;
  alerts: number;
  openAlerts: number;
  resolvedAlerts: number;
  averageTrafficLoad: number;
  averageLatency: number;
  averageAvailability: number;
}

export interface SystemSnapshot {
  timestamp: string;
  stats: DashboardStats;
  cells: NetworkCell[];
  devices: Device[];
  subscribers: Subscriber[];
  trafficFlows: TrafficFlow[];
  trafficRecords: NetworkTraffic[];
  allocations: ResourceAllocation[];
  slices: NetworkSlice[];
  edgeServers: EdgeServer[];
  jobs: OptimizationJob[];
  results: OptimizationResult[];
  kpis: NetworkKPI[];
  users: UserSummary[];
  alerts: Alert[];
  report: ReportSummary;
}
