'use client'
import { useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";

const ForgotPasswordPage: React.FC = () => {
  const [email, setEmail] = useState("");
  const [token, setToken] = useState("");
  const [isTokenSent, setIsTokenSent] = useState(false);
  const [isResending, setIsResending] = useState(false);
  const [isVerifying, setIsVerifying] = useState(false);

  const router = useRouter();
  const apiBaseUrl = "/identity/api/v1/oauth2";

  const apiBaseUrl2 = "/identity/api/v1/auth/passcode-verification";

  const handleSendToken = async () => {
    try {
      await axios.post(`${apiBaseUrl}/forgot-password`, { username: email });
      setIsTokenSent(true);
      alert("Verification token sent! Check your email.");
    } catch (error: any) {
      console.error("Error sending token:", error);
      alert(
        error.response?.data?.message ||
        "An error occurred while requesting the token"
      );
    }
  };

  const handleResend = async () => {
    setIsResending(true);
    try {
      await axios.post(`${apiBaseUrl2}/token`, { username: email });
      alert("New verification token sent!");
    } catch (error: any) {
      console.error("Error resending token:", error);
      alert(
        error.response?.data?.message ||
        "An error occurred while resending the token"
      );
    } finally {
      setIsResending(false);
    }
  };

  const handleVerifyToken = async () => {
    if (token.length !== 6) {
      alert("Please enter a valid 6-digit token.");
      return;
    }

    setIsVerifying(true);
    try {
      await axios.post(`${apiBaseUrl2}`, {
        username: email,
        token,
      });
      alert("Token verified successfully!");
      router.push(`/reset-forgot-password?email=${encodeURIComponent(email)}&token=${encodeURIComponent(token)}`);
    } catch (error: any) {
      console.error("Error verifying token:", error);
      alert(
        error.response?.data?.message ||
        "An error occurred while verifying the token"
      );
    } finally {
      setIsVerifying(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="w-full max-w-lg bg-white rounded-lg shadow-lg p-6 relative">
        {/* Back Button */}
        {isTokenSent && (
          <button
            onClick={() => setIsTokenSent(false)} // Navigate back to email input step
            className="absolute top-4 left-4 text-gray-500 hover:text-gray-800"
          >
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
                d="M15.75 19.5L8.25 12l7.5-7.5"
              />
            </svg>
          </button>
        )}
        <h1 className="text-2xl font-bold text-gray-800 text-center mb-6">
          Forgot Password
        </h1>
        {!isTokenSent ? (
          <div className="space-y-4">
            <label className="block text-sm font-medium text-black">
              Email
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="h-[40px] w-full p-2 bg-slate-100 rounded text-black"
              placeholder="Enter your email"
              required
              autoComplete="email"
            />
            <button
              onClick={handleSendToken}
              className="w-full h-[40px] text-white bg-blue-500 rounded hover:bg-blue-600"
            >
              Send Verification Token
            </button>
          </div>
        ) : (
          <div className="space-y-4">
            {/* Email as Label */}
            <label className="block text-sm font-medium text-gray-600">
              Verification sent to: <span className="font-semibold">{email}</span>
            </label>
            <input
              type="text"
              value={token}
              onChange={(e) => setToken(e.target.value)}
              className="h-[40px] text-black w-full p-2 bg-slate-100 rounded"
              placeholder="Enter the 6-digit token"
              maxLength={6}
              required
            />
            <div className="flex justify-between items-center">
              <button
                onClick={handleResend}
                disabled={isResending}
                className={`h-[40px] px-4 text-white rounded ${
                  isResending
                    ? "bg-gray-400 cursor-not-allowed"
                    : "bg-blue-500 hover:bg-blue-600"
                }`}
              >
                {isResending ? "Resending..." : "Resend Token"}
              </button>
              <button
                onClick={handleVerifyToken}
                disabled={isVerifying}
                className={`h-[40px] px-4 text-white rounded ${
                  isVerifying
                    ? "bg-gray-400 cursor-not-allowed"
                    : "bg-green-500 hover:bg-green-600"
                }`}
              >
                {isVerifying ? "Verifying..." : "Verify Token"}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ForgotPasswordPage;
