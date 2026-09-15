import { Link } from "react-router-dom";


interface NotFoundProps {
  homePath?: string;
}

const NotFound = ({homePath = "/",}: NotFoundProps) => {
  return (
    <section className="min-h-screen bg-white px-5 py-10 font-serif">
      <div className="mx-auto max-w-7xl">
        <div className="text-center">
          <div
            className="mx-auto flex h-[400px] max-w-4xl items-center justify-center bg-center bg-no-repeat"
            style={{
              backgroundImage:
                "url('https://cdn.dribbble.com/users/285475/screenshots/2083086/dribbble_1.gif')",
              backgroundSize: "cover",
            }}
          >
            <h1 className="text-[80px] font-bold text-gray-900">
              404
            </h1>
          </div>

          <div className="-mt-12">
            <h3 className="text-3xl font-semibold text-gray-800">
              Look like you're lost
            </h3>

            <p className="mt-3 text-base text-gray-500">
              The page you are looking for is not available!
            </p>

            <Link
              to={homePath}
              className="mt-5 inline-block rounded-md bg-green-600 px-5 py-2.5 font-medium text-white transition hover:bg-green-700"
            >
              Go to Home
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
};

export default NotFound;
