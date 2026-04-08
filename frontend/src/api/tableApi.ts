import apiClient from './client';
import type {
  TableResponse,
  TableCreateRequest,
  TableUpdateRequest,
  OrderHistoryItem,
  PagedResponse,
} from '../types/table';

export async function getTables(): Promise<TableResponse[]> {
  const response = await apiClient.get<TableResponse[]>('/tables');
  return response.data;
}

export async function createTable(data: TableCreateRequest): Promise<TableResponse> {
  const response = await apiClient.post<TableResponse>('/tables', data);
  return response.data;
}

export async function updateTable(tableId: number, data: TableUpdateRequest): Promise<TableResponse> {
  const response = await apiClient.put<TableResponse>(`/tables/${tableId}`, data);
  return response.data;
}

export async function getOrderHistory(
  tableId: number,
  date?: string,
  page = 0,
): Promise<PagedResponse<OrderHistoryItem>> {
  const params: Record<string, unknown> = { tableId, page, size: 20 };
  if (date) params.date = date;
  const response = await apiClient.get<PagedResponse<OrderHistoryItem>>('/orders/history', { params });
  return response.data;
}
