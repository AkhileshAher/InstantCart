import { ArrowUpRightIcon, BikeIcon, ChevronDownIcon, LogOutIcon, MapPinIcon, MenuIcon, PackageIcon, SearchIcon, ShieldIcon, ShoppingCartIcon, UserIcon, XIcon } from "lucide-react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";


function Navbar() {

    const user: any = { name: "John Doe", email: "john@example.com", isAdmin: true }

    const { cartCount, setIsCartOpen } = useCart();

    const [searchQuery, setSearchQuery] = useState("");
    const [userMenuOpen, setUserMenuOpen] = useState(false);
    const navigate = useNavigate();

    const handleSearch = (e: SubmitEvent) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            navigate(`/search?q=${encodeURIComponent(searchQuery.trim())}`)
            setSearchQuery("");
        }
    }

    const handleLogout = () => {
        setUserMenuOpen(false);
        navigate("/");
    }

    return (
        <nav className="bg-white sticky top-0 z-50 border-b border-app-border">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between h-16 gap-4">
                <Link to={"/"} className="flex items-center gap-2 text-[22px] font-medium shrink-0">
                    <img src="Logo.png" alt="InstantCartLogo" className="h-10 w-15" /> <span className="text-green-900 font-bold">InstantCart</span>
                </Link>

                <div className="w-full flex items-center justify-end gap-4 lg:gap-10">
                    <div className="hidden md:flex items-center gap-6 text-sm text-zinc-600">
                        <Link to={"/"} >Home</Link>
                        <Link to={"/products"} >Products</Link>
                        <Link to={"/deals"} className="text-orange-500" >Deals</Link>
                    </div>

                    <form onSubmit={handleSearch} className="hidden sm:flex flex-1 max-w-sm text-xs sm:text-sm">
                        <div className="relative w-full">
                            <SearchIcon className="absolute left-2.5 top-1/2 -translate-y-1/2 size-4 text-zinc-500" />
                            <input type="text" placeholder="Search for groceries..." value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)} className="w-full pl-8 p-2 bg-orange-50 rounded-full ring ring-orange-500/15 focus:ring-orange-500/30" />
                        </div>
                    </form>

                    <div className="flex items-center gap-3">
                        <button className="relative p-2 rounded-xl" onClick={() => setIsCartOpen(true)}>
                            <ShoppingCartIcon className="size-5 text-zinc-900" />
                            {cartCount > 0 && <span className="absolute -top-1 -right-1 size-4 bg-orange-300 text-white text-[10px] rounded-full flex items-center justify-center">{cartCount}</span>}
                        </button>

                        <div className="relative">
                            {user ? (
                                <button onClick={() => setUserMenuOpen(!userMenuOpen)} className="flex items-center gap-2 p-2">
                                    <div className="size-7 rounded-full bg-green-950 text-white flex items-center justify-center">
                                        {user.name.charAt(0).toUpperCase()}
                                    </div>
                                    <ChevronDownIcon className="size-3 text-zinc-500" />
                                </button>
                            ) : (
                                <div className="flex items-center justify-center gap-2">
                                    <Link to={"/login"} className="hidden md:flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-green-950 rounded-full hover:bg-green-950 transition-colors">
                                        <UserIcon size={16} /> Sign In
                                    </Link>
                                    {userMenuOpen ? <XIcon className="md:hidden" onClick={() => setUserMenuOpen(!userMenuOpen)} /> : <MenuIcon className="md:hidden" onClick={() => setUserMenuOpen(!userMenuOpen)} />}
                                </div>
                            )}

                            {userMenuOpen && (
                                <>
                                    <div className="fixed inset-0 z-40" onClick={() => setUserMenuOpen(false)} />
                                    <div className="absolute right-0 mt-2.5 w-56 bg-white rounded-xl shadow-lg border py-2 z-50 animate-fade-in">
                                        {user && (
                                            <div className="px-4 py-2 border-b">
                                                <p className="text-sm font-medium text-zinc-900">{user?.name}</p>
                                                <p className="text-xs text-zinc-500">{user?.email}</p>
                                            </div>
                                        )}
                                        <div onClick={() => setUserMenuOpen(false)}>
                                            {!user && <Link to='/login'
                                                className="dropdown-link" > <UserIcon size={16} />Sign In</Link>}

                                            {user && <Link to='/orders'
                                                className="dropdown-link"><PackageIcon size={16} />My Orders</Link>}

                                            {user && <Link to='/addresses'
                                                className="dropdown-link"><MapPinIcon size={16} />Addresses</Link>}

                                            {!user && <Link to='/products'
                                                className="dropdown-link md:hidden"><ArrowUpRightIcon size={16} />Products</Link>}

                                            {!user && <Link to='/deals'
                                                className="dropdown-link md:hidden"><ArrowUpRightIcon size={16} />Deals</Link>}
                                            {user?.isAdmin && (
                                                <Link to='/admin/products'
                                                    className="dropdown-link"><ShieldIcon size={16} className="text-orange-600" /><span className="text-orange-600">Admin Panel</span></Link>
                                            )}
                                            {user && (
                                                <div>
                                                    <button onClick={handleLogout} className="flex items-center gap-3 px-4 py-2.5 text-sm text-red-500 hover:bg-red-50 w-full transition-colors">
                                                        <LogOutIcon size={16} /> Logout
                                                    </button>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </>
                            )}
                        </div>

                    </div>

                </div>
            </div>
        </nav>
    )
}

export default Navbar
