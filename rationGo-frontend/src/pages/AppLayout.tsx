import { Outlet } from 'react-router-dom';

function AppLayout() {
  return (
    <>
      <p>Banner</p>
      <p>Navabr</p>
      <main className='min-h-screen'>
        <Outlet />
      </main>
      <p>footer</p>
      <p>CartSidebar</p>
    </>
  )
}

export default AppLayout
