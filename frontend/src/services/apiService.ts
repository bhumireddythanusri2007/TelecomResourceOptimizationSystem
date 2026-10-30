import {
  DashboardStats,
  NetworkCell,
  Device,
  Subscriber,
  TrafficFlow,
  ResourceAllocation,
  NetworkSlice,
  EdgeServer,
  OptimizationJob,
  OptimizationResult,
  NetworkKPI,
  UserSummary,
  Alert,
  ReportSummary,
  SystemSnapshot,
  DataSourceMode,
  NavModule
} from '../types';

const CACHE_KEY = 'telecom_system_cached_snapshot_v1';
const API_BASE = (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '');
const REQUEST_TIMEOUT_MS = 8000;

export class TelecomApiService {
  private static parseText(el: Element | null | undefined): string {
    if (!el) return '';
    const input = el.querySelector('input');
    if (input) {
      const val = input.getAttribute('value') ?? (input as HTMLInputElement).value;
      if (val !== undefined && val !== null && val.trim() !== '') {
        return val.trim();
      }
    }
    const select = el.querySelector('select');
    if (select) {
      const selected = select.querySelector('option[selected]') as HTMLOptionElement | null;
      if (selected) {
        return (selected.getAttribute('value') || selected.value || selected.text || '').trim();
      }
      return (select.value || select.options[select.selectedIndex]?.text || '').trim();
    }
    return el.textContent?.trim() || '';
  }

  private static parseNumber(text: string, defaultValue: number = 0): number {
    const cleaned = text.replace(/[^0-9.-]/g, '');
    const num = parseFloat(cleaned);
    return isNaN(num) ? defaultValue : num;
  }

  // Parse HTML string using DOMParser in the browser
  private static getDoc(html: string): Document {
    const parser = new DOMParser();
    return parser.parseFromString(html, 'text/html');
  }

  // Check if servlet returned a database connection error
  public static checkDatabaseError(doc: Document): string | null {
    const alertBanner = doc.querySelector('.alert-banner, .error, .message.error');
    if (alertBanner) {
      const text = alertBanner.textContent || '';
      if (text.includes('database') || text.includes('Database') ||
          text.includes('Check the database connection') ||
          text.includes('DB_PASSWORD') ||
          text.includes('Unable to load') ||
          text.includes('Access denied') ||
          text.includes('Communications link failure')) {
        return text.trim();
      }
    }
    const bodyText = doc.body?.textContent || '';
    if (bodyText.includes('DB_PASSWORD environment variable is not configured')) {
      return 'DB_PASSWORD environment variable is not configured. Please set DB_PASSWORD in .env or system environment.';
    }
    if (bodyText.includes('Access denied for user')) {
      const match = bodyText.match(/Access denied for user '[^']+'/);
      return match ? match[0] + ' (Check DB_USER / DB_PASSWORD)' : 'MySQL Access Denied (Check DB credentials)';
    }
    if (bodyText.includes('Communications link failure') || bodyText.includes('Connection refused')) {
      return 'MySQL connection refused. Verify MySQL server is running on the configured host & port.';
    }
    if (bodyText.includes('Unable to load records. Check the database connection')) {
      return 'Unable to load records: MySQL database connection failed. Check host, port, credentials, and SSL settings.';
    }
    return null;
  }

  // Robust fetch with timeout and base URL support
  private static async fetchText(endpoint: string): Promise<string> {
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), REQUEST_TIMEOUT_MS);
    const url = `${API_BASE}${endpoint}`;

    try {
      const res = await fetch(url, {
        cache: 'no-cache',
        signal: controller.signal,
      });

      if (!res.ok) {
        throw new Error(`HTTP ${res.status} (${res.statusText}) from ${endpoint}`);
      }
      return await res.text();
    } catch (e: any) {
      if (e.name === 'AbortError') {
        throw new Error(`Request to ${endpoint} timed out after ${REQUEST_TIMEOUT_MS / 1000}s`);
      }
      throw e;
    } finally {
      clearTimeout(timeout);
    }
  }

  // --- HTML Parsers for Existing Servlets ---

  public static parseDashboard(html: string): { stats: DashboardStats; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);

    const cards = doc.querySelectorAll('.card');
    const stats: DashboardStats = {
      networkCells: 0,
      devices: 0,
      subscribers: 0,
      trafficRecords: 0,
      alerts: 0,
      optimizationResults: 0,
      kpiRecords: 0,
    };

    cards.forEach(card => {
      const title = card.querySelector('.card-title')?.textContent?.trim() || '';
      const valEl = card.querySelector('.card-value');
      const val = this.parseNumber(valEl?.textContent || '0');

      if (title.includes('Network Cells')) stats.networkCells = val;
      else if (title.includes('Devices')) stats.devices = val;
      else if (title.includes('Subscribers')) stats.subscribers = val;
      else if (title.includes('Traffic Records')) stats.trafficRecords = val;
      else if (title.includes('Alerts')) stats.alerts = val;
      else if (title.includes('Optimization Results')) stats.optimizationResults = val;
      else if (title.includes('KPI Records')) stats.kpiRecords = val;
    });

    return { stats, error };
  }

  public static parseCells(html: string): { cells: NetworkCell[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr');
    const cells: NetworkCell[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 6) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          cells.push({
            cellId: id,
            cellName: this.parseText(tds[1]),
            location: this.parseText(tds[2]),
            totalCapacity: this.parseNumber(this.parseText(tds[3])),
            currentUsers: this.parseNumber(this.parseText(tds[4])),
            status: (this.parseText(tds[5]) as any) || 'Active',
          });
        }
      }
    });

    return { cells, error };
  }

  public static parseDevices(html: string): { devices: Device[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr');
    const devices: Device[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 7) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          devices.push({
            deviceId: id,
            deviceName: this.parseText(tds[1]),
            deviceType: this.parseText(tds[2]),
            ipAddress: this.parseText(tds[3]),
            connectionType: this.parseText(tds[4]),
            status: this.parseText(tds[5]),
            userId: this.parseNumber(this.parseText(tds[6])),
          });
        }
      }
    });

    return { devices, error };
  }

  public static parseSubscribers(html: string): { subscribers: Subscriber[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr');
    const subscribers: Subscriber[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 9) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          subscribers.push({
            subscriberId: id,
            subscriberName: this.parseText(tds[1]),
            phoneNumber: this.parseText(tds[2]),
            email: this.parseText(tds[3]),
            subscriptionType: this.parseText(tds[4]),
            dataPlan: this.parseText(tds[5]),
            monthlyDataLimit: this.parseNumber(this.parseText(tds[6])),
            currentDataUsage: this.parseNumber(this.parseText(tds[7])),
            status: this.parseText(tds[8]),
          });
        }
      }
    });

    return { subscribers, error };
  }

  public static parseTrafficFlows(html: string): { flows: TrafficFlow[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr');
    const flows: TrafficFlow[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 8) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          flows.push({
            flowId: id,
            cellId: this.parseNumber(this.parseText(tds[1])),
            sourceIp: this.parseText(tds[2]),
            destinationIp: this.parseText(tds[3]),
            protocol: this.parseText(tds[4]),
            dataVolume: this.parseNumber(this.parseText(tds[5])),
            flowDuration: this.parseNumber(this.parseText(tds[6])),
            packetCount: this.parseNumber(this.parseText(tds[7])),
            flowStatus: tds.length > 8 ? this.parseText(tds[8]) : 'Active',
          });
        }
      }
    });

    return { flows, error };
  }

  public static parseResourceAllocations(html: string): { allocations: ResourceAllocation[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr, table tr');
    const allocations: ResourceAllocation[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 5) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          allocations.push({
            allocationId: id,
            cellId: this.parseNumber(this.parseText(tds[1])),
            bandwidth: this.parseNumber(this.parseText(tds[2])),
            frequency: this.parseNumber(this.parseText(tds[3])),
            allocatedUsers: this.parseNumber(this.parseText(tds[4])),
          });
        }
      }
    });

    return { allocations, error };
  }

  public static parseNetworkSlices(html: string): { slices: NetworkSlice[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr, table tr');
    const slices: NetworkSlice[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 7) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          slices.push({
            sliceId: id,
            sliceName: this.parseText(tds[1]),
            sliceType: this.parseText(tds[2]),
            bandwidth: this.parseNumber(this.parseText(tds[3])),
            connectedUsers: this.parseNumber(this.parseText(tds[4])),
            latencyRequirement: this.parseNumber(this.parseText(tds[5])),
            status: this.parseText(tds[6]),
          });
        }
      }
    });

    return { slices, error };
  }

  public static parseEdgeServers(html: string): { servers: EdgeServer[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr, table tr');
    const servers: EdgeServer[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 8) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          servers.push({
            edgeServerId: id,
            serverName: this.parseText(tds[1]),
            location: this.parseText(tds[2]),
            cpuCapacity: this.parseNumber(this.parseText(tds[3])),
            memoryCapacity: this.parseNumber(this.parseText(tds[4])),
            storageCapacity: this.parseNumber(this.parseText(tds[5])),
            connectedUsers: this.parseNumber(this.parseText(tds[6])),
            status: this.parseText(tds[7]),
          });
        }
      }
    });

    return { servers, error };
  }

  public static parseOptimization(html: string): {
    results: OptimizationResult[];
    jobs: OptimizationJob[];
    error: string | null;
  } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const tables = doc.querySelectorAll('table');

    const results: OptimizationResult[] = [];
    const jobs: OptimizationJob[] = [];

    if (tables.length > 0) {
      const resRows = tables[0].querySelectorAll('tr');
      resRows.forEach(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length >= 7) {
          const id = this.parseNumber(this.parseText(tds[0]));
          if (id > 0) {
            results.push({
              resultId: id,
              cellId: this.parseNumber(this.parseText(tds[1])),
              previousLoad: this.parseNumber(this.parseText(tds[2])),
              optimizedLoad: this.parseNumber(this.parseText(tds[3])),
              allocatedBandwidth: this.parseNumber(this.parseText(tds[4])),
              performanceScore: this.parseNumber(this.parseText(tds[5])),
              optimizationTime: this.parseText(tds[6]),
            });
          }
        }
      });
    }

    if (tables.length > 1) {
      const jobRows = tables[1].querySelectorAll('tr');
      jobRows.forEach(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length >= 7) {
          const id = this.parseNumber(this.parseText(tds[0]));
          if (id > 0) {
            jobs.push({
              jobId: id,
              jobName: this.parseText(tds[1]),
              optimizationType: this.parseText(tds[2]),
              targetCellId: this.parseNumber(this.parseText(tds[3])),
              objective: 'Load Rebalance',
              inputLoad: this.parseNumber(this.parseText(tds[4])),
              status: this.parseText(tds[5]),
              createdTime: this.parseText(tds[6]),
            });
          }
        }
      });
    }

    return { results, jobs, error };
  }

  public static parseKpis(html: string): { kpis: NetworkKPI[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr, table tr');
    const kpis: NetworkKPI[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 8) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          kpis.push({
            kpiId: id,
            cellId: this.parseNumber(this.parseText(tds[1])),
            throughput: this.parseNumber(this.parseText(tds[2])),
            latency: this.parseNumber(this.parseText(tds[3])),
            packetLoss: this.parseNumber(this.parseText(tds[4])),
            availability: this.parseNumber(this.parseText(tds[5])),
            signalStrength: this.parseNumber(this.parseText(tds[6])),
            recordedTime: this.parseText(tds[7]),
          });
        }
      }
    });

    return { kpis, error };
  }

  public static parseUsers(html: string): { users: UserSummary[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tbody tr, table tr');
    const users: UserSummary[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 5) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          users.push({
            userId: id,
            username: this.parseText(tds[1]),
            email: this.parseText(tds[2]),
            role: this.parseText(tds[3]),
            status: this.parseText(tds[4]),
            createdAt: tds.length > 5 ? this.parseText(tds[5]) : 'N/A',
          });
        }
      }
    });

    return { users, error };
  }

  public static parseAlerts(html: string): { alerts: Alert[]; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);
    const rows = doc.querySelectorAll('table tr');
    const alerts: Alert[] = [];

    rows.forEach(tr => {
      const tds = tr.querySelectorAll('td');
      if (tds.length >= 7) {
        const id = this.parseNumber(this.parseText(tds[0]));
        if (id > 0) {
          alerts.push({
            alertId: id,
            cellId: this.parseNumber(this.parseText(tds[1])),
            alertType: this.parseText(tds[2]),
            alertMessage: this.parseText(tds[3]),
            severity: (this.parseText(tds[4]).toUpperCase() as any) || 'INFO',
            status: (this.parseText(tds[5]).toUpperCase() as any) || 'OPEN',
            createdTime: this.parseText(tds[6]),
          });
        }
      }
    });

    return { alerts, error };
  }

  public static parseReports(html: string): { report: ReportSummary; error: string | null } {
    const doc = this.getDoc(html);
    const error = this.checkDatabaseError(doc);

    const report: ReportSummary = {
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
    };

    const cards = doc.querySelectorAll('.card');
    cards.forEach(card => {
      const title = card.querySelector('.title')?.textContent?.trim().toLowerCase() || '';
      const valText = card.querySelector('.value')?.textContent?.trim() || '';
      const val = parseFloat(valText.replace(/[^0-9.-]/g, '')) || 0;

      if (title.includes('total network cells') || title === 'network cells') report.totalCells = val;
      else if (title.includes('traffic records')) report.trafficRecords = val;
      else if (title.includes('resource allocations')) report.resourceAllocations = val;
      else if (title.includes('optimization results')) report.optimizationResults = val;
      else if (title.includes('kpi records')) report.kpiRecords = val;
      else if (title.includes('total alerts') || title === 'alerts') report.alerts = val;
      else if (title.includes('open alerts')) report.openAlerts = val;
      else if (title.includes('resolved alerts')) report.resolvedAlerts = val;
      else if (title.includes('traffic load')) report.averageTrafficLoad = val;
      else if (title.includes('latency')) report.averageLatency = val;
      else if (title.includes('availability')) report.averageAvailability = val;
    });

    return { report, error };
  }

  // --- Snapshot Caching ---
  public static saveSnapshot(snapshot: SystemSnapshot) {
    try {
      localStorage.setItem(CACHE_KEY, JSON.stringify(snapshot));
    } catch (e) {
      console.warn('Unable to persist snapshot to localStorage', e);
    }
  }

  public static saveCachedSnapshot(snapshot: SystemSnapshot) {
    this.saveSnapshot(snapshot);
  }

  public static loadCachedSnapshot(): SystemSnapshot | null {
    try {
      const raw = localStorage.getItem(CACHE_KEY);
      if (raw) {
        return JSON.parse(raw);
      }
    } catch (e) {
      console.warn('Unable to read cached snapshot', e);
    }
    return null;
  }

  // --- Fast Initial Dashboard Fetch (Overview + Alerts + Cells + Slices + KPIs for overview charts) ---
  public static async fetchDashboardOverview(): Promise<{
    stats: DashboardStats | null;
    alerts: Alert[];
    cells: NetworkCell[];
    slices: NetworkSlice[];
    kpis: NetworkKPI[];
    error: string | null;
  }> {
    try {
      const [dashText, alertsText, cellsText, slicesText, kpisText] = await Promise.all([
        this.fetchText('/dashboard'),
        this.fetchText('/alerts').catch(() => ''),
        this.fetchText('/network-cells').catch(() => ''),
        this.fetchText('/network-slices').catch(() => ''),
        this.fetchText('/kpi-monitoring').catch(() => ''),
      ]);

      const { stats, error: dashError } = this.parseDashboard(dashText);
      if (dashError) {
        return { stats: null, alerts: [], cells: [], slices: [], kpis: [], error: dashError };
      }

      const alerts = alertsText ? this.parseAlerts(alertsText).alerts : [];
      const cells = cellsText ? this.parseCells(cellsText).cells : [];
      const slices = slicesText ? this.parseNetworkSlices(slicesText).slices : [];
      const kpis = kpisText ? this.parseKpis(kpisText).kpis : [];

      return {
        stats,
        alerts,
        cells,
        slices,
        kpis,
        error: null
      };
    } catch (err: any) {
      return {
        stats: null,
        alerts: [],
        cells: [],
        slices: [],
        kpis: [],
        error: `Backend unreachable: ${err.message || 'Connection failed'}`
      };
    }
  }

  // --- On-Demand Fetcher for Specific Modules ---
  public static async fetchModuleData(module: NavModule): Promise<{
    data: any;
    error: string | null;
  }> {
    try {
      switch (module) {
        case 'network-cells': {
          const html = await this.fetchText('/network-cells');
          const { cells, error } = this.parseCells(html);
          return { data: cells, error };
        }
        case 'devices': {
          const html = await this.fetchText('/devices');
          const { devices, error } = this.parseDevices(html);
          return { data: devices, error };
        }
        case 'subscribers': {
          const html = await this.fetchText('/subscribers');
          const { subscribers, error } = this.parseSubscribers(html);
          return { data: subscribers, error };
        }
        case 'traffic': {
          const html = await this.fetchText('/traffic-flows');
          const { flows, error } = this.parseTrafficFlows(html);
          return { data: flows, error };
        }
        case 'resource-allocation': {
          const html = await this.fetchText('/resource-allocation');
          const { allocations, error } = this.parseResourceAllocations(html);
          return { data: allocations, error };
        }
        case 'network-slices': {
          const html = await this.fetchText('/network-slices');
          const { slices, error } = this.parseNetworkSlices(html);
          return { data: slices, error };
        }
        case 'edge-servers': {
          const html = await this.fetchText('/edge-servers');
          const { servers, error } = this.parseEdgeServers(html);
          return { data: servers, error };
        }
        case 'optimization': {
          const html = await this.fetchText('/optimization');
          const { results, jobs, error } = this.parseOptimization(html);
          return { data: { results, jobs }, error };
        }
        case 'kpi-monitoring': {
          const html = await this.fetchText('/kpi-monitoring');
          const { kpis, error } = this.parseKpis(html);
          return { data: kpis, error };
        }
        case 'users': {
          const html = await this.fetchText('/users');
          const { users, error } = this.parseUsers(html);
          return { data: users, error };
        }
        case 'alerts': {
          const html = await this.fetchText('/alerts');
          const { alerts, error } = this.parseAlerts(html);
          return { data: alerts, error };
        }
        case 'reports': {
          const html = await this.fetchText('/reports');
          const { report, error } = this.parseReports(html);
          return { data: report, error };
        }
        default:
          return { data: null, error: null };
      }
    } catch (e: any) {
      return { data: null, error: e.message || 'Fetch failed' };
    }
  }

  // --- Trigger Rule-Based Optimization Action via POST to /optimization ---
  public static async runOptimizationSimulation(mode: DataSourceMode): Promise<{ success: boolean; message: string }> {
    if (mode === 'DEMO') {
      return {
        success: true,
        message: 'Simulation executed in DEMO MODE. Rebalanced cell load and estimated 10% reduction.'
      };
    }

    try {
      const url = `${API_BASE}/optimization`;
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({ action: 'run' }).toString(),
      });

      if (response.ok || response.redirected) {
        return {
          success: true,
          message: 'Optimization job dispatched successfully to Java backend.'
        };
      } else {
        return {
          success: false,
          message: `Backend returned HTTP status ${response.status}.`
        };
      }
    } catch (e: any) {
      return {
        success: false,
        message: `Failed to trigger optimization: ${e.message}`
      };
    }
  }
}
