'use client'
import { useState } from "react";
import axios from "axios";
import { useRouter, useSearchParams } from "next/navigation";

interface FormData {
  token: string;
}

const EmailVerificationPage: React.FC = () => {
  const searchParams = useSearchParams();
  const email = searchParams.get("email") || "example@gmail.com"; // Get email from URL params

  const [formData, setFormData] = useState<FormData>({
    token: "",
  });
  const [isResending, setIsResending] = useState(false); // State to handle resend button status

  const router = useRouter();
  const apiBaseUrl = "/identity/api/v1/auth/email-verification";

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        apiBaseUrl,
        { ...formData, username: email }, // Send the email and token to verify
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      alert("Email has been verified successfully!");
      // Redirect after successful email verification
      router.push("/dashboard");
    } catch (error: any) {
      console.error("Error during email verification:", error);
      alert(
        error.response?.data?.message || "An error occurred during verification"
      );
    }
  };

  const handleResend = async () => {
    setIsResending(true); // Disable the button to prevent multiple clicks
    try {
      await axios.post(
        `${apiBaseUrl}/token`, // Endpoint for resending token
        { username: email }, // Send the email as the username
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      alert("New confirmation link has been sent, check your emails!");
    } catch (error: any) {
      console.error("Error during token resend:", error);
      alert(
        error.response?.data?.message ||
          "An error occurred while resending the token"
      );
    } finally {
      setIsResending(false); // Re-enable the button
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="w-full max-w-lg bg-white rounded-lg shadow-lg p-6">
        <h1 className="text-2xl font-bold text-gray-800 text-center mb-6">
          Verify Your Email
        </h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Email Display */}
          <div className="flex justify-between gap-2 items-center py-4">
            <label
              htmlFor="email"
              className="block text-sm font-medium text-gray-600"
            >
              Email
            </label>
            <p className="mt-1 w-full p-2 bg-gray-100 text-gray-800 font-semibold rounded">
              {email || "example@gmail.com"}
            </p>
          </div>

          {/* Verification Token */}
          <div>
            <label
              htmlFor="token"
              className="block text-sm font-medium text-gray-600"
            >
              Verification Token
            </label>
            <input
              id="token"
              type="text"
              name="token"
              value={formData.token}
              onChange={handleChange}
              className="h-[40px] text-black mt-1 w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
              required
              placeholder="Enter the 6-digit code"
            />
          </div>

          {/* Submit Button */}
          <div className="flex justify-between items-center">
          <button
            onClick={handleResend}
            disabled={isResending}
            className={`w-[200px] h-[40px] flex items-center justify-center text-[1rem] font-light rounded-md transition-transform transform hover:shadow-lg ${
              isResending
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-blue-500 text-white hover:bg-blue-600"
            }`}
          >
            {isResending ? "Resending..." : "Resend Verification Token"}
          </button>
            <button
              type="submit"
              className="w-[200px] h-[40px] flex items-center justify-center text-[1rem] font-light text-white rounded-md border-transparent transition-transform transform hover:shadow-lg hover:border-[#00b09b] hover:brightness-90 bg-gradient-to-r from-[#00b09b] to-[#96c93d]"
            >
              Verify Email
            </button>
          </div>
        </form>

        {/* Resend Button */}
        <div className="mt-6">

        </div>
      </div>
    </div>
  );
};

export default EmailVerificationPage;
