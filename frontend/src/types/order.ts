export interface OrderItemRequest {
  menuId: number;
  quantity: number;
}

export interface OrderCreateRequest {
  tableId: number;
  items: OrderItemRequest[];
}

export interface OrderItemResponse {
  menuId: number;
  menuName: string;
  quantity: number;
  unitPrice: number;
}

export interface OrderResponse {
  orderId: number;
  orderNumber: string;
  items: OrderItemResponse[];
  totalAmount: number;
  createdAt: string;
}
