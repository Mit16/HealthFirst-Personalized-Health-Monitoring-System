import React, { useState } from "react";
import { authApi } from "../api";
import { toast } from "react-toastify";

const ForgotPasswordRequest = () => {
  const [email, setEmail] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authApi.post("/auth/forgot-password", null, {
        params: { email },
      });
      toast.success("Reset email sent. Check your inbox!");
    } catch (err) {
      toast.error("Error sending reset email");
    }
  };

  return (
    <div className="max-w-md mx-auto p-4 bg-white dark:bg-black rounded shadow">
      <h2 className="text-xl font-bold mb-4">Forgot Password</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          placeholder="Enter your email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          className="w-full px-4 py-2 border rounded mb-4"
        />
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded"
        >
          Send Reset Link
        </button>
      </form>
    </div>
  );
};

export default ForgotPasswordRequest;
