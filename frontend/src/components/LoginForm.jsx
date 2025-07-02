"use client";
import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authApi } from "../api";
import { AuthContext } from "../context/AuthContext";
import { toast } from "react-toastify";
import { Label } from "./ui/label";
import { Input } from "./ui/input";
import { cn } from "../lib/utils";
import { IconBrandGithub, IconBrandGoogle } from "@tabler/icons-react";

export default function LoginForm() {
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    const loginPromise = login(formData);

    toast
      .promise(loginPromise, {
        pending: "Logging in...",
        success: "Logged in successfully!",
        error: {
          render({ data }) {
            return data?.message || "Login failed";
          },
        },
      })
      .then((result) => {
        if (result.success) {
          navigate("/homepage");
        } else {
          setError(result.message || "Login failed");
        }
      })
      .then((result) => {
        if (result.success) {
          // Add a small delay to allow toast to fully display
          setTimeout(() => {
            navigate("/homepage");
          }, 100); // 100-300ms is usually enough
        } else {
          setError(result.message || "Login failed");
        }
      })
      .catch((err) => {
        const errorMessage =
          err?.response?.data?.message ||
          err?.message ||
          "Something went wrong. Please try again.";
        setError(errorMessage);
      });
  };

  return (
    <div className="shadow-input mx-auto w-full max-w-md rounded-none bg-white p-4 md:rounded-2xl md:p-8 dark:bg-black">
      <h2 className="text-xl font-bold text-neutral-800 dark:text-neutral-200">
        Welcome Back
      </h2>
      <p className="mt-2 text-sm text-neutral-600 dark:text-neutral-300">
        Enter your credentials to access your account
      </p>

      <form className="my-8" onSubmit={handleSubmit}>
        <LabelInputContainer className="mb-4">
          <Label htmlFor="email">Email</Label>
          <Input
            id="email"
            name="email"
            placeholder="you@example.com"
            type="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </LabelInputContainer>

        <LabelInputContainer className="mb-6">
          <Label htmlFor="password">Password</Label>
          <Input
            id="password"
            name="password"
            placeholder="••••••••"
            type="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </LabelInputContainer>
        <div className="text-left text-sm text-blue-500 mb-4">
          <Link to="/forgot-password" className="hover:underline">
            Forgot password?
          </Link>
        </div>
        {error && (
          <p className="mb-4 text-sm text-red-500 font-medium">{error}</p>
        )}

        <button
          type="submit"
          className="group/btn relative block h-10 w-full rounded-md bg-gradient-to-br from-black to-neutral-600 font-medium text-white shadow-[inset_0px_1px_0px_#ffffff40,inset_0px_-1px_0px_#ffffff40] dark:from-zinc-900 dark:to-zinc-900 dark:shadow-[inset_0px_1px_0px_#27272a,inset_0px_-1px_0px_#27272a]"
        >
          Log in &rarr;
          <BottomGradient />
        </button>

        <div className="my-8 h-[1px] w-full bg-gradient-to-r from-transparent via-neutral-300 to-transparent dark:via-neutral-700" />

        <div className="flex flex-col space-y-4">
          <OAuthButton
            icon={<IconBrandGithub className="h-4 w-4" />}
            label="Continue with GitHub"
          />
          <OAuthButton
            icon={<IconBrandGoogle className="h-4 w-4" />}
            label="Continue with Google"
          />
        </div>
      </form>

      <p className="mt-4 text-center text-sm text-neutral-600 dark:text-neutral-400">
        Don’t have an account?{" "}
        <Link
          to="/signup"
          className="font-semibold text-black underline dark:text-white"
        >
          Create one
        </Link>
      </p>
    </div>
  );
}

const OAuthButton = ({ icon, label }) => (
  <button
    type="button"
    className="group/btn shadow-input relative flex h-10 w-full items-center justify-start space-x-2 rounded-md bg-gray-50 px-4 font-medium text-black dark:bg-zinc-900 dark:shadow-[0_0_1px_1px_#262626]"
  >
    <span className="text-neutral-800 dark:text-neutral-300">{icon}</span>
    <span className="text-sm text-neutral-700 dark:text-neutral-300">
      {label}
    </span>
    <BottomGradient />
  </button>
);

const BottomGradient = () => (
  <>
    <span className="absolute inset-x-0 -bottom-px block h-px bg-gradient-to-r from-transparent via-cyan-500 to-transparent opacity-0 transition duration-500 group-hover/btn:opacity-100" />
    <span className="absolute inset-x-10 -bottom-px mx-auto block h-px w-1/2 bg-gradient-to-r from-transparent via-indigo-500 to-transparent opacity-0 blur-sm transition duration-500 group-hover/btn:opacity-100" />
  </>
);

const LabelInputContainer = ({ children, className }) => (
  <div className={cn("flex w-full flex-col space-y-2", className)}>
    {children}
  </div>
);
