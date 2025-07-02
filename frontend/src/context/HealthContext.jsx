// src/context/HealthContext.jsx
import React, { createContext, useState, useEffect, useContext } from "react";
import { userApi, metricsApi, predictionApi, alertApi } from "../api";
import { toast } from "react-toastify";
import { AuthContext } from "./AuthContext";

export const HealthContext = createContext();

export const HealthProvider = ({ children }) => {
  const { token, userId } = useContext(AuthContext);

  const [user, setUser] = useState(null);
  const [frequentMetrics, setFrequentMetrics] = useState([]);
  const [dailyMetrics, setDailyMetrics] = useState({ steps: [], sleep: [] });
  const [prediction, setPrediction] = useState(null);
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);

  const headers = { Authorization: `Bearer ${token}` };

  const fetchAllHealthData = async () => {
    setLoading(true);
    try {
      const [userRes, rawRes, dailyRes, predictionRes, alertsRes] =
        await Promise.all([
          userApi.get(`/user/profile/${userId}`, { headers }),
          metricsApi.get(`/metrics/raw/${userId}`, { headers }),
          metricsApi.get(`/metrics/daily-view/${userId}`, { headers }),
          predictionApi.get(`/api/prediction/${userId}`, { headers }),
          alertApi.get(`/api/alerts/${userId}`, { headers }),
        ]);

      setUser(userRes.data);
      const { heartRate, spo2, temperature } = rawRes.data;
      const combined = [];
      for (let i = 0; i < Math.min(20, heartRate.length); i++) {
        combined.push({
          timestamp: heartRate[i]?.timestamp,
          heartRate: heartRate[i]?.heartRate,
          spo2: spo2[i]?.spo2,
          temperature: temperature[i]?.bodyTemperature,
        });
      }
      setFrequentMetrics(combined);
      setDailyMetrics(dailyRes.data);
      setPrediction(predictionRes.data);
      setAlerts(alertsRes.data);
    } catch (err) {
      console.error("❌ Failed to load health data:", err);
      toast.error("❌ Failed to load health data.");
    } finally {
      setLoading(false);
    }
  };

  const submitHealthMetrics = async ({
    heartRate,
    spo2,
    bodyTemperature,
    accX,
    accY,
    accZ,
  }) => {
    const payload = {
      userId: userId?.toString(),
      timestamp: Date.now(),
      heartRate: Number(heartRate),
      spo2: Number(spo2),
      bodyTemperature: Number(bodyTemperature),
      accX: Number(accX),
      accY: Number(accY),
      accZ: Number(accZ),
    };

    return metricsApi.post("/metrics/health", payload, { headers });
  };

const submitDailyMetrics = async ({
  steps,
  sleepStartTime,
  sleepEndTime,
}) => {
  const durationInMs = sleepEndTime - sleepStartTime;

  const payload = {
    userId: userId?.toString(),
    date: new Date().toISOString().split("T")[0],
    steps: Number(steps),
    sleepStartTime, // ✅ already a timestamp
    sleepEndTime,   // ✅ already a timestamp
    sleepDuration: durationInMs, // ✅ duration in ms
  };

  return metricsApi.post("/metrics/daily", payload, { headers });
};

  useEffect(() => {
    if (userId) fetchAllHealthData();
  }, [userId]);

  return (
    <HealthContext.Provider
      value={{
        user,
        frequentMetrics,
        dailyMetrics,
        prediction,
        alerts,
        loading,
        setLoading,
        fetchAllHealthData,
        submitHealthMetrics,
        submitDailyMetrics,
      }}
    >
      {children}
    </HealthContext.Provider>
  );
};
