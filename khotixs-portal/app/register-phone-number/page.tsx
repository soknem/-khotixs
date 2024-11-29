
'use client'
import { useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";

interface FormData {
    phoneNumber: string;
    familyName: string;
    givenName: string;
  password: string;
  confirmedPassword: string;
  acceptTerms: "true" | "false";
}

const RegisterPage: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    phoneNumber: "",
    familyName: "",
    givenName: "",
    password: "",
    confirmedPassword: "",
    acceptTerms: "false",
  });

  const router = useRouter();
  const apiBaseUrl = "http://localhost:8000/identity/api/v1/oauth2/register/phone-number";

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };
const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();

  if (formData.password !== formData.confirmedPassword) {
    return alert("Password and confirmation password do not match!");
  }

  try {
    const response = await axios.post(apiBaseUrl, formData, {
      headers: { "Content-Type": "application/json" },
    });

    alert("Registration successful!");

    // Redirect to email verification page with email
    router.push(`/verify-phone-number?phone-number=${encodeURIComponent(formData.phoneNumber)}`);
  } catch (error: any) {
    console.error("Error during registration:", error);
    alert(
      error.response?.data?.message || "An error occurred during registration"
    );
  }
};


  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="w-full max-w-md bg-white rounded-lg shadow-lg p-6">
        <h1 className="text-2xl font-bold text-gray-800 text-center mb-6">
          Register
        </h1>
        <form onSubmit={handleSubmit} className="space-y-4">


          {/* Phone number */}
          <div>
            <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-600">
              Phone Number
            </label>
            <input
              id="phoneNumber"
              type="text"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              className="h-[40px] text-black mt-1 w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
              required
            //   autoComplete="email"
            />
          </div>


          {/* Family Name */}
          <div>
            <label htmlFor="familyName" className="block text-sm font-medium text-gray-600">
              Family Name
            </label>
            <input
              id="familyName"
              type="text"
              name="familyName"
              value={formData.familyName}
              onChange={handleChange}
              className="h-[40px] text-black mt-1 w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
              required
              maxLength={64}
            />
          </div>

          {/* Given Name */}
          <div>
            <label htmlFor="givenName" className="block text-sm font-medium text-gray-600">
              Given Name
            </label>
            <input
              id="givenName"
              type="text"
              name="givenName"
              value={formData.givenName}
              onChange={handleChange}
              className="h-[40px] text-black mt-1 w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
              required
              maxLength={64}
            />
          </div>

                   {/* Password */}
                   <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-600">
              Password
            </label>
            <input
              id="password"
            //   type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className="h-[40px] text-black  mt-1 w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
              required
              minLength={6}
              maxLength={32}
            />
          </div>

          {/* Confirm Password */}
          <div>
            <label htmlFor="confirmedPassword" className="block text-sm font-medium text-gray-600">
              Confirm Password
            </label>
            <input
              id="confirmedPassword"
            //   type="password"
              name="confirmedPassword"
              value={formData.confirmedPassword}
              onChange={handleChange}
              className="h-[40px] text-black mt-1 w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
              required
              minLength={6}
              maxLength={32}
            />
          </div>

          {/* Accept Terms */}
          <div>
            <label htmlFor="acceptTerms" className="block text-sm font-medium text-gray-600">
              Accept Terms
            </label>
            <select
              id="acceptTerms"
              name="acceptTerms"
              value={formData.acceptTerms}
              onChange={handleChange}
              className="h-[40px]  text-black mt-1 w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
              required
            >
              <option value="false">No</option>
              <option value="true">Yes</option>
            </select>
          </div>

          {/* Submit Button */}
          <div>
            <button
              type="submit"
              className="w-full mt-10 mb-5 h-[40px] flex items-center justify-center text-[1.5rem] font-light text-white rounded-lg p-5 border-2 border-transparent transition-transform transform hover:scale-105 hover:shadow-lg hover:border-[#00b09b] hover:brightness-90 bg-gradient-to-r from-[#00b09b] to-[#96c93d]"
            >
              Register
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegisterPage;
