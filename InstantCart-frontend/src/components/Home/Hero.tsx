import { ArrowRightIcon, LeafIcon } from "lucide-react"
import { Link } from "react-router-dom"
import basketimg from "../../assets/basket.png";


function Hero() {
    return (
        <section className="relative overflow-hidden min-h-130 mb-10 rounded-3xl flex items-center flex-row">
            <div className="absolute inset-0 green-bg" />

            <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 w-full">
                <div className="max-w-xl xl:pl-10">
                    <span className="inline-flex items-center gap-1.5 px-4 py-1.5 text-xs font-semibold text-orange-300 bg-orange-300/10 rounded-full mb-5">
                        <LeafIcon className="size-3" /> Farm-Fresh & Organic
                    </span>

                    <h1 className="font-serif text-4xl sm:text-5xl lg:text-6xl text-white leading-tight mb-5 font-bold">
                        Nourish your home with
                        <span className="text-orange-300"> Earth's finest</span>
                    </h1>
                    <p className="text-base text-white/70 leading-relaxed mb-8 max-w-md">
                        Everything you need, delivered fresh to your doorstep. Shop easily, eat better, and enjoy more convenience.
                    </p>
                    <div className="flex flex-wrap gap-3">
                        <Link to='/products' className="px-7 py-3 bg-orange-400 text-white font-semibold rounded-full hover:bg-orange-500 transition-all flex items-center justify-center gap-2 active:scale-[0.98]">
                            Shop Now <ArrowRightIcon />
                        </Link>
                        <Link to='/products' className="px-7 py-3 bg-white/10 text-white font-semibold rounded-full hover:bg-white/20 transition-all border border-white/20">
                            Browse Categories
                        </Link>
                    </div>
                </div>
            </div>

            {/* Right Side Image */}
            <div className="text-white relative w-1/2 md:block hidden">
                <img src={basketimg} alt="BasketImage" className="w-full h-1/2" />
            </div>
        </section>

    )
}

export default Hero
