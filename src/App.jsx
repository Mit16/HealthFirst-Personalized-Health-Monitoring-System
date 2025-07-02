import React, { useContext } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import LoginForm from "./components/LoginForm";
import SignupForm from "./components/SignupForm";
import Dashboard from "./pages/Dashboard";
import HealthForm from "./pages/HealthForm";
import AlertsPage from "./pages/AlertsPage";
import HomePage from "./pages/HomePage";
import LockHomePage from "./pages/LockHomePage";
import NotFound from "./pages/NotFound";
import ForgotPasswordRequest from "./pages/ForgotPasswordRequest";
import ResetPassword from "./pages/ResetPassword";
import PredictionPage from "./pages/PredictionPage";
import PrivateRoute from "./components/PrivateRoute";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import WearIntegrationPage from "./pages/WearIntegrationPage";
import { AuthContext } from "./context/AuthContext";

const App = () => {
  const { token, isAuthenticated } = useContext(AuthContext);

  return (
    <>
      <Routes>
        <Route
          path="/"
          element={
            isAuthenticated ? <Navigate to="/homepage" /> : <LockHomePage />
          }
        />
        <Route
          path="/homepage"
          element={isAuthenticated ? <HomePage /> : <LockHomePage />}
        />
        <Route
          path="/prediction"
          element={
            <PrivateRoute>
              <PredictionPage />
            </PrivateRoute>
          }
        />

        <Route path="/forgot-password" element={<ForgotPasswordRequest />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/login" element={<LoginForm />} />
        <Route path="/signup" element={<SignupForm />} />
        <Route
          path="/wear-integration"
          element={
            <PrivateRoute>
              <WearIntegrationPage />
            </PrivateRoute>
          }
        />
        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />

        <Route
          path="/healthform"
          element={
            <PrivateRoute>
              <HealthForm />
            </PrivateRoute>
          }
        />
        <Route
          path="/alerts"
          element={
            <PrivateRoute>
              <AlertsPage />
            </PrivateRoute>
          }
        />
        <Route path="/home" element={<HomePage />} />
        <Route path="*" element={<NotFound />} />
      </Routes>

      <ToastContainer position="top-right" autoClose={3000} />
    </>
  );
};

export default App;
