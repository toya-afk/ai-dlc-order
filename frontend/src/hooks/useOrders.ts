import { useQuery } from '@tanstack/react-query';
import apiClient from '../api/client';
import type { OrderResponse } from '../types/order';
import type { TableDashboardResponse } from '../types/dashboard';

export function useSessionOrders(tableId: number | null) {
  return useQuery({
    queryKey: ['orders', tableId],
    queryFn: async () => {
      const response = await apiClient.get<OrderResponse[]>('/orders', {
        params: { tableId },
      });
      return response.data;
    },
    enabled: !!tableId,
  });
}

export function useDashboard() {
  return useQuery({
    queryKey: ['dashboard'],
    queryFn: async () => {
      const response = await apiClient.get<TableDashboardResponse[]>('/orders/dashboard');
      return response.data;
    },
  });
}

export function useTableOrders(tableId: number) {
  return useQuery({
    queryKey: ['orders', 'table', tableId],
    queryFn: async () => {
      const response = await apiClient.get<OrderResponse[]>('/orders', {
        params: { tableId },
      });
      return response.data;
    },
  });
}
