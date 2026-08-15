import { Outlet } from 'react-router-dom';
import Banner from '../components/Banner';
import Navbar from '../components/Navbar';

function AppLayout() {
  return (
    <>
      <Banner />
      <Navbar />
      <main className='min-h-screen'>
        <Outlet />
      </main>
      <p>footer</p>
      <p>CartSidebar</p>
    </>
  )
}

export default AppLayout
