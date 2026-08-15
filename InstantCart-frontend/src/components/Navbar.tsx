import { useState } from "react";
import { useNavigate } from "react-router-dom";


function Navbar() {

    const user: any = { name: "John Doe", email: "john@example.com", isAdmin: true }

    const { cartCount, setIsCartOpen } = {
        cartCount: 5,
        setIsCartOpen: (_data: any) => { }
    };

    const [searchquery, setSearchQuery] = useState("");
    const [userMenuOpen, setUserMenuOpen] = useState(false);
    const navigate = useNavigate();

    return (
        <nav className="bg-white sticky top-0 z-50 border-b border-app-border">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between h-16 gap-4">

            </div>
        </nav>
    )
}

export default Navbar
