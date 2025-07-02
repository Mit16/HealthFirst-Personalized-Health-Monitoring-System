import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { BrowserRouter } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AuthProvider } from "./context/AuthContext";
import { HealthProvider } from "./context/HealthContext";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <BrowserRouter>
    <AuthProvider>
      <HealthProvider>
        <App />
      </HealthProvider>
    </AuthProvider>
    </BrowserRouter>
    <ToastContainer />
  </StrictMode>
);
