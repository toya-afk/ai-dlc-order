export interface TableResponse {
  id: number;
  number: number;
  name: string;
  password: string;
}

export interface TableCreateRequest {
  number: number;
  name: string;
  password: string;
}

export interface TableUpdateRequest {
  name: string;
  password: string;
}

export interface OrderHistoryItem {
  orderId: number;
  orderNumber: string;
  items: { menuName: string; quantity: number; unitPrice: number }[];
  totalAmount: number;
  completedAt: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
}
