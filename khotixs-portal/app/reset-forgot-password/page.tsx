'use client'
import { useState } from "react";
import axios from "axios";
import { useRouter, useSearchParams } from "next/navigation";

const ResetForgotPasswordPage: React.FC = () => {
  const searchParams = useSearchParams();
  const email = searchParams.get("email");
  const token = searchParams.get("token");

  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const router = useRouter();
  const apiBaseUrl = "/identity/api/v1/oauth2";

  const handleResetPassword = async () => {
    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    try {
      await axios.post(`${apiBaseUrl}/change-forgot-password`, {
        username: email,
        token,
        password,
        confirmPassword,
      });
      alert("Password reset successfully! Redirecting to login...");
      router.push("/login");
    } catch (error: any) {
      console.error("Error resetting password:", error);
      alert(
        error.response?.data?.message || "An error occurred while resetting the password"
      );
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="w-full max-w-lg bg-white rounded-lg shadow-lg p-6">
        <h1 className="text-2xl font-bold text-gray-800 text-center mb-6">
          Reset Password
        </h1>
        <div className="space-y-4">
          {/* New Password Field */}
          <label className="block text-sm font-medium text-gray-600">
            New Password
          </label>
          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="h-[40px] w-full p-2 border border-gray-300 text-black rounded pr-10"
              placeholder="Enter your new password"
              required
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-700"
            >
              {showPassword ? (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M3.98 8.33a10.926 10.926 0 0116.04 0M21 12a10.954 10.954 0 01-3.98 8.33M9.34 15.66a3 3 0 115.32-5.32"
                  />
                </svg>
              ) : (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M3.98 8.33a10.926 10.926 0 0116.04 0M21 12a10.954 10.954 0 01-3.98 8.33M12 15a3 3 0 000-6"
                  />
                </svg>
              )}
            </button>
          </div>

          {/* Confirm Password Field */}
          <label className="block text-sm font-medium text-gray-600">
            Confirm Password
          </label>
          <div className="relative">
            <input
              type={showConfirmPassword ? "text" : "password"}
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="h-[40px] w-full p-2 border border-gray-300 text-black rounded pr-10"
              placeholder="Confirm your new password"
              required
            />
            <button
              type="button"
              onClick={() => setShowConfirmPassword(!showConfirmPassword)}
              className="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-700"
            >
              {showConfirmPassword ? (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M3.98 8.33a10.926 10.926 0 0116.04 0M21 12a10.954 10.954 0 01-3.98 8.33M12 15a3 3 0 000-6"
                  />
                </svg>
              ) : (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M3.98 8.33a10.926 10.926 0 0116.04 0M21 12a10.954 10.954 0 01-3.98 8.33M12 15a3 3 0 000-6"
                  />
                </svg>
              )}
            </button>
          </div>

          {/* Reset Password Button */}
          <button
            onClick={handleResetPassword}
            className="w-full h-[40px] text-white bg-blue-500 rounded hover:bg-blue-600"
          >
            Reset Password
          </button>
        </div>
      </div>
    </div>
  );
};

export default ResetForgotPasswordPage;
