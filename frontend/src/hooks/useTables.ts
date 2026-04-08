import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getTables, createTable, updateTable } from '../api/tableApi';
import type { TableCreateRequest, TableUpdateRequest } from '../types/table';

export function useTables() {
  return useQuery({ queryKey: ['tables'], queryFn: getTables });
}

export function useCreateTable() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: TableCreateRequest) => createTable(data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['tables'] }),
  });
}

export function useUpdateTable() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ tableId, data }: { tableId: number; data: TableUpdateRequest }) =>
      updateTable(tableId, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['tables'] }),
  });
}
