'use client'

import { useRouter } from "next/navigation";

export default function Home() {
    const router = useRouter();

    const handleLogin = () => {
        router.push("/oauth2/authorization/nextjs");
    };

    const handleForgotPassword = () => {
        router.push("/forgot-password");
    };

    const handleChangePassword = () => {
        router.push("/change-password");
    };

    const handleLogout = () => {
        router.push("/identity/logout");
    };

    return (
        <div className="w-full h-screen flex items-center justify-center bg-gray-100">
            <div className="p-8 bg-white shadow-lg rounded-lg flex flex-col gap-6 items-center max-w-[500px] w-full">
                <h1 className="text-3xl font-bold text-gray-800">
                    Welcome to <span className="text-blue-600">Khotixs</span>
                </h1>
                <p className="text-gray-600 text-lg text-center">
                    Please select an option below to continue.
                </p>
                <div className="w-full flex flex-col gap-4">
                    {/* Login Button */}
                    <button
                        onClick={handleLogin}
                        className="w-full flex items-center justify-center text-lg font-medium text-white rounded-lg p-3 transition hover:bg-blue-700 bg-blue-600"
                    >
                        Login
                    </button>

                    {/* Forgot Password Button */}
                    <button
                        onClick={handleForgotPassword}
                        className="w-full flex items-center justify-center text-lg font-medium text-white rounded-lg p-3 transition hover:bg-green-700 bg-green-600"
                    >
                        Forgot Password
                    </button>

                    {/* Change Password Button */}
                    <button
                        onClick={handleChangePassword}
                        className="w-full flex items-center justify-center text-lg font-medium text-white rounded-lg p-3 transition hover:bg-gray-700 bg-gray-600"
                    >
                        Change Password
                    </button>

                    {/* Logout Button */}
                    <button
                        onClick={handleLogout}
                        className="w-full flex items-center justify-center text-lg font-medium text-white rounded-lg p-3 transition hover:bg-red-700 bg-red-600"
                    >
                        Logout
                    </button>
                </div>
            </div>
        </div>
    );
}
