import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../../stores/useAuthStore';

export default function AdminAuthGuard() {
  const { isAuthenticated, role } = useAuthStore();

  if (!isAuthenticated || role !== 'ADMIN') {
    return <Navigate to="/admin/login" replace />;
  }

  return <Outlet />;
}
