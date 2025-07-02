"use client";
import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";
import { authApi } from "../api";
import { Label } from "./ui/label";
import { Input } from "./ui/input";
import { cn } from "../lib/utils";
import { IconBrandGithub, IconBrandGoogle } from "@tabler/icons-react";

export default function SignupForm() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
    age: "",
    gender: "",
    medicalHistory: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      await authApi.post("/auth/signup", formData);
      toast.success("Account created. You can now log in.");
      navigate("/login");
    } catch (err) {
      setError("Signup failed. Please check the inputs and try again.");
      toast.error("Signup failed. Please check the inputs and try again.");
    }
  };

  return (
    <div className="mx-auto w-full max-w-md">
      <h2 className="text-2xl font-bold text-zinc-900 dark:text-white">
        Create your account
      </h2>
      <p className="mt-2 text-sm text-zinc-600 dark:text-zinc-400">
        Start your health journey with us.
      </p>

      <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
        <LabelInputContainer>
          <Label htmlFor="name">Full Name</Label>
          <Input
            id="name"
            name="name"
            placeholder="John Doe"
            type="text"
            value={formData.name}
            onChange={handleChange}
          />
        </LabelInputContainer>

        <LabelInputContainer>
          <Label htmlFor="email">Email Address</Label>
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

        <LabelInputContainer>
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

        <LabelInputContainer>
          <Label htmlFor="age">Age</Label>
          <Input
            id="age"
            name="age"
            type="number"
            placeholder="25"
            value={formData.age}
            onChange={handleChange}
          />
        </LabelInputContainer>

        <LabelInputContainer>
          <Label htmlFor="gender">Gender</Label>
          <select
            id="gender"
            name="gender"
            className="rounded-md border border-zinc-300 p-2 text-sm text-zinc-800 dark:border-zinc-700 dark:bg-zinc-800 dark:text-zinc-100"
            value={formData.gender}
            onChange={handleChange}
          >
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
          </select>
        </LabelInputContainer>

        <LabelInputContainer>
          <Label htmlFor="medicalHistory">Medical History</Label>
          <textarea
            id="medicalHistory"
            name="medicalHistory"
            className="min-h-[80px] resize-y rounded-md border border-zinc-300 p-2 text-sm text-zinc-800 dark:border-zinc-700 dark:bg-zinc-800 dark:text-zinc-100"
            placeholder="Any known conditions, allergies, etc."
            value={formData.medicalHistory}
            onChange={handleChange}
          />
        </LabelInputContainer>

        <button
          className="group/btn relative block h-10 w-full rounded-md bg-gradient-to-br from-black to-zinc-700 font-medium text-white transition duration-200 hover:brightness-110"
          type="submit"
        >
          Sign up →
          <BottomGradient />
        </button>

        <p className="text-center text-sm text-zinc-600 dark:text-zinc-400">
          Already have an account?{" "}
          <Link
            to="/login"
            className="font-semibold text-zinc-900 underline dark:text-white"
          >
            Login
          </Link>
        </p>

        <div className="my-6 h-[1px] w-full bg-gradient-to-r from-transparent via-zinc-300 to-transparent dark:via-zinc-700" />

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

        {error && <p className="text-red-500 text-sm mt-4">{error}</p>}
      </form>
    </div>
  );
}

const LabelInputContainer = ({ children, className }) => (
  <div className={cn("flex w-full flex-col space-y-1", className)}>
    {children}
  </div>
);

const BottomGradient = () => (
  <>
    <span className="absolute inset-x-0 -bottom-px block h-px w-full bg-gradient-to-r from-transparent via-cyan-500 to-transparent opacity-0 transition duration-500 group-hover/btn:opacity-100" />
    <span className="absolute inset-x-10 -bottom-px mx-auto block h-px w-1/2 bg-gradient-to-r from-transparent via-indigo-500 to-transparent opacity-0 blur-sm transition duration-500 group-hover/btn:opacity-100" />
  </>
);

const OAuthButton = ({ icon, label }) => (
  <button
    type="button"
    className="group/btn relative flex h-10 w-full items-center justify-start space-x-2 rounded-md bg-zinc-50 px-4 font-medium text-zinc-800 dark:bg-zinc-800 dark:text-zinc-100 dark:shadow-[0_0_1px_1px_#262626]"
  >
    <span>{icon}</span>
    <span className="text-sm">{label}</span>
    <BottomGradient />
  </button>
);
