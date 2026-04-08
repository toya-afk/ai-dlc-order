export interface OrderSummary {
  orderId: number;
  orderNumber: string;
  menuSummary: string;
  totalAmount: number;
  createdAt: string;
}

export interface TableDashboardResponse {
  tableId: number;
  tableNumber: number;
  totalAmount: number;
  recentOrders: OrderSummary[];
  hasNewOrder: boolean;
}

export type SseEventType = 'NEW_ORDER' | 'ORDER_STATUS_CHANGED' | 'ORDER_DELETED';
