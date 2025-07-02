import React, { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { authApi } from "../api";
import { toast } from "react-toastify";

const ResetPassword = () => {
  const [params] = useSearchParams();
  const token = params.get("token");

  const [newPass, setNewPass] = useState("");
  const [confirmPass, setConfirmPass] = useState("");
  const [buttonEnabled, setButtonEnabled] = useState(false);

  useEffect(() => {
    setButtonEnabled(newPass && confirmPass && newPass === confirmPass);
  }, [newPass, confirmPass]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authApi.post("/auth/reset-password", null, {
        params: { token, newPassword: newPass },
      });
      toast.success("Password reset successful. You can close this page.");
    } catch (err) {
      const msg =
        err.response?.data ||
        err.message ||
        "Failed to reset password. Try again.";
      toast.error(msg);
    }
  };

  return (
    <div className="max-w-md mx-auto p-4 bg-white dark:bg-black rounded shadow">
      <h2 className="text-xl font-bold mb-4">Reset Password</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="password"
          placeholder="New password"
          value={newPass}
          onChange={(e) => setNewPass(e.target.value)}
          className="w-full px-4 py-2 border rounded mb-4"
          required
        />
        <input
          type="password"
          placeholder="Confirm password"
          value={confirmPass}
          onChange={(e) => setConfirmPass(e.target.value)}
          className="w-full px-4 py-2 border rounded mb-4"
          required
        />
        <button
          type="submit"
          disabled={!buttonEnabled}
          className={`w-full py-2 rounded ${
            buttonEnabled
              ? "bg-green-600 text-white"
              : "bg-gray-400 text-white cursor-not-allowed"
          }`}
        >
          Reset Password
        </button>
      </form>
    </div>
  );
};

export default ResetPassword;
