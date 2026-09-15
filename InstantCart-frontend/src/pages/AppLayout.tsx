import { Navigate, Outlet } from 'react-router-dom';
import Banner from '../components/Banner';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import CartSidebar from '../components/CartSidebar';
import Loading from '../components/Loading';
import { useAuth } from '../context/AuthContext';

function AppLayout() {

  const { user, loading } = useAuth();

  if (loading) {return <Loading />;}

  if (user?.role === "DELIVERY") { return <Navigate to="/delivery" replace />;}

  return (
    <>
      <Banner />
      <Navbar />
      <main className='min-h-screen'>
        <Outlet />
      </main>
      <Footer />
      <CartSidebar />
    </>
  )
}

export default AppLayout
