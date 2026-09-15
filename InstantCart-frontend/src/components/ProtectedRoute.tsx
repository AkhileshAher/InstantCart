import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Loading from './Loading';

const ProtectedRoute = () => {

  const {user,loading} = useAuth();

  if(loading) return <Loading/>

  if(!user) return <Navigate to='/login' replace />

  if (user.role === "DELIVERY") {
    return <Navigate to="/delivery" replace />;
  }

  return (
    <>
      <Outlet />
    </>
  )
}

export default ProtectedRoute
