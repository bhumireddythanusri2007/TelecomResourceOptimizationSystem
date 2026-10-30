import React, { useState, useMemo } from 'react';
import { Search, ChevronLeft, ChevronRight, Filter, Database } from 'lucide-react';

export interface Column<T> {
  header: string;
  accessor?: keyof T;
  render?: (item: T) => React.ReactNode;
  className?: string;
}

interface DataTableProps<T> {
  columns: Column<T>[];
  data: T[];
  searchPlaceholder?: string;
  searchFilter?: (item: T, query: string) => boolean;
  statusFilterKey?: keyof T;
  title?: string;
  subtitle?: string;
  actions?: React.ReactNode;
}

export function DataTable<T extends Record<string, any>>({
  columns,
  data,
  searchPlaceholder = 'Search records...',
  searchFilter,
  statusFilterKey,
  title,
  subtitle,
  actions,
}: DataTableProps<T>) {
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;

  // Extract distinct status values if statusFilterKey is provided
  const statusOptions = useMemo(() => {
    if (!statusFilterKey) return [];
    const set = new Set<string>();
    data.forEach(item => {
      const val = item[statusFilterKey];
      if (val) set.add(String(val));
    });
    return Array.from(set);
  }, [data, statusFilterKey]);

  // Filtered dataset
  const filteredData = useMemo(() => {
    return data.filter(item => {
      if (statusFilter !== 'ALL' && statusFilterKey) {
        if (String(item[statusFilterKey]) !== statusFilter) return false;
      }
      if (search && searchFilter) {
        return searchFilter(item, search.toLowerCase());
      }
      if (search) {
        return Object.values(item).some(val =>
          String(val).toLowerCase().includes(search.toLowerCase())
        );
      }
      return true;
    });
  }, [data, search, statusFilter, searchFilter, statusFilterKey]);

  // Pagination
  const totalPages = Math.ceil(filteredData.length / pageSize) || 1;
  const paginatedData = useMemo(() => {
    const start = (currentPage - 1) * pageSize;
    return filteredData.slice(start, start + pageSize);
  }, [filteredData, currentPage, pageSize]);

  return (
    <div className="bg-navy-900/80 border border-blue-900/30 rounded-xl overflow-hidden shadow-lg backdrop-blur-md">
      {/* Table Header Controls */}
      <div className="p-5 border-b border-navy-800/80 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          {title && <h3 className="text-base font-bold text-slate-100">{title}</h3>}
          {subtitle && <p className="text-xs text-slate-400 mt-0.5">{subtitle}</p>}
        </div>

        <div className="flex flex-wrap items-center gap-3">
          {/* Search Box */}
          <div className="relative min-w-[220px]">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={search}
              onChange={e => {
                setSearch(e.target.value);
                setCurrentPage(1);
              }}
              placeholder={searchPlaceholder}
              className="w-full pl-9 pr-3 py-1.5 bg-navy-950/80 border border-navy-800 rounded-lg text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-blue-500 transition"
            />
          </div>

          {/* Status Dropdown */}
          {statusFilterKey && statusOptions.length > 0 && (
            <div className="flex items-center gap-1.5 bg-navy-950/80 border border-navy-800 rounded-lg px-2.5 py-1 text-xs">
              <Filter className="w-3.5 h-3.5 text-slate-400" />
              <select
                value={statusFilter}
                onChange={e => {
                  setStatusFilter(e.target.value);
                  setCurrentPage(1);
                }}
                className="bg-transparent text-slate-200 focus:outline-none cursor-pointer"
              >
                <option value="ALL" className="bg-navy-900 text-slate-200">
                  All Statuses
                </option>
                {statusOptions.map(opt => (
                  <option key={opt} value={opt} className="bg-navy-900 text-slate-200">
                    {opt}
                  </option>
                ))}
              </select>
            </div>
          )}

          {actions}
        </div>
      </div>

      {/* Table Scrollable Container */}
      <div className="overflow-x-auto">
        <table className="w-full text-left text-xs">
          <thead className="bg-navy-950/90 text-slate-400 font-semibold uppercase tracking-wider border-b border-navy-800">
            <tr>
              {columns.map((col, idx) => (
                <th key={idx} className={`py-3.5 px-4 ${col.className || ''}`}>
                  {col.header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-navy-800/60 text-slate-300">
            {paginatedData.length > 0 ? (
              paginatedData.map((row, rowIdx) => (
                <tr
                  key={rowIdx}
                  className="hover:bg-blue-950/30 transition-colors duration-150"
                >
                  {columns.map((col, colIdx) => (
                    <td key={colIdx} className={`py-3.5 px-4 ${col.className || ''}`}>
                      {col.render
                        ? col.render(row)
                        : col.accessor
                        ? String(row[col.accessor] ?? '—')
                        : '—'}
                    </td>
                  ))}
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan={columns.length}
                  className="py-12 text-center text-slate-500 font-medium"
                >
                  <div className="flex flex-col items-center justify-center gap-2">
                    <Database className="w-8 h-8 text-slate-600" />
                    <span>No records found matching criteria</span>
                  </div>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination Footer */}
      <div className="p-4 border-t border-navy-800/80 flex items-center justify-between text-xs text-slate-400">
        <div>
          Showing{' '}
          <span className="font-semibold text-slate-200">
            {filteredData.length > 0 ? (currentPage - 1) * pageSize + 1 : 0}
          </span>{' '}
          to{' '}
          <span className="font-semibold text-slate-200">
            {Math.min(currentPage * pageSize, filteredData.length)}
          </span>{' '}
          of <span className="font-semibold text-slate-200">{filteredData.length}</span> entries
        </div>

        <div className="flex items-center gap-1.5">
          <button
            onClick={() => setCurrentPage(p => Math.max(p - 1, 1))}
            disabled={currentPage === 1}
            className="p-1.5 rounded bg-navy-950 border border-navy-800 text-slate-400 hover:text-white disabled:opacity-40 disabled:cursor-not-allowed transition"
          >
            <ChevronLeft className="w-4 h-4" />
          </button>
          <span className="px-2 font-medium text-slate-300">
            Page {currentPage} of {totalPages}
          </span>
          <button
            onClick={() => setCurrentPage(p => Math.min(p + 1, totalPages))}
            disabled={currentPage === totalPages}
            className="p-1.5 rounded bg-navy-950 border border-navy-800 text-slate-400 hover:text-white disabled:opacity-40 disabled:cursor-not-allowed transition"
          >
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
}
