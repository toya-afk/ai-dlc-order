import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../../stores/useAuthStore';

export default function CustomerAuthGuard() {
  const { isAuthenticated, role } = useAuthStore();

  if (!isAuthenticated || role !== 'TABLE') {
    return <Navigate to="/customer/setup" replace />;
  }

  return <Outlet />;
}
