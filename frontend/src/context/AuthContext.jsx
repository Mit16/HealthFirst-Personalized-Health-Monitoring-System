// src/context/AuthContext.js
import React, { createContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { authApi } from "../api";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [userId, setUserId] = useState(null);
  const [user, setUser] = useState(null); // { name, email }
  const [wearToken, setWearToken] = useState(null);
  const navigate = useNavigate();

  const login = async (credentials) => {
    try {
      const res = await authApi.post("/auth/login", credentials);
      const { token, userId, name, email } = res.data;

      localStorage.setItem("token", token);
      setToken(token);
      setUserId(userId);
      setUser({ name, email });

      return { success: true };
    } catch (error) {
      return { success: false, message: error.response?.data || "Login failed" };
    }
  };

  useEffect(() => {
  if (token) {
    const payload = JSON.parse(atob(token.split(".")[1]));
    setUserId(payload.userId);
    setUser({ email: payload.sub, name: payload.name }); // or from API if updated
  }
}, [token]);


  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUserId(null);
    setUser(null);
    setWearToken(null);
    navigate("/");
  };

  const generateWearToken = async () => {
    try {
      const res = await authApi.get(`/wear/generate-token?userId=${userId}`,{
        headers:{
          Authorization: `Bearer ${token}`,
        },
      });
      setWearToken(res.data.token);
      return { success: true, token: res.data.token };
    } catch (err) {
      console.error("Failed to generate Wear token", err);
      return { success: false, error: "Failed to generate token. Try again." };
    }
  };

  return (
    <AuthContext.Provider
      value={{
        token,
        userId,
        user,
        wearToken,
        isAuthenticated: !!token,
        login,
        logout,
        generateWearToken,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
