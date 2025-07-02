// src/components/DashboardComponent.jsx
import React, { useContext, useState } from "react";
import { HealthContext } from "../context/HealthContext";
import { motion, AnimatePresence } from "framer-motion";

const DashboardComponent = () => {
  const { user, prediction, loading, frequentMetrics, dailyMetrics } = useContext(HealthContext);
  const [currentView, setCurrentView] = useState("frequent");

  const tableVariants = {
    hidden: { opacity: 0, x: 100 },
    visible: { opacity: 1, x: 0 },
    exit: { opacity: 0, x: -100 },
  };

  const Table = ({ data, isDaily }) => (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-700">
        <thead className="bg-gray-700">
          <tr>
            <th className="px-4 py-2 text-left text-xs font-medium text-gray-300">Date</th>
            {isDaily ? (
              <>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-300">Steps</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-300">Sleep Hours</th>
              </>
            ) : (
              <>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-300">Heart Rate</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-300">SpO2</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-300">Temp (°C)</th>
              </>
            )}
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-700">
          {isDaily
            ? dailyMetrics.steps.map((step, idx) => (
                <tr key={idx}>
                  <td className="px-4 py-2 text-sm">{new Date(step.timestamp).toLocaleDateString()}</td>
                  <td className="px-4 py-2 text-sm">{step.steps}</td>
                  <td className="px-4 py-2 text-sm">{dailyMetrics.sleep[idx]?.sleepHours || "N/A"}</td>
                </tr>
              ))
            : data.map((item, idx) => (
                <tr key={idx}>
                  <td className="px-4 py-2 text-sm">{new Date(item.timestamp).toLocaleString()}</td>
                  <td className="px-4 py-2 text-sm">{item.heartRate || "N/A"}</td>
                  <td className="px-4 py-2 text-sm">{item.spo2 || "N/A"}</td>
                  <td className="px-4 py-2 text-sm">{item.temperature || "N/A"}</td>
                </tr>
              ))}
        </tbody>
      </table>
    </div>
  );

  if (loading) return <p className="text-center text-gray-300">Loading...</p>;

  return (
    <div className="p-6 space-y-6 bg-black min-h-screen text-gray-200">
      <h2 className="text-2xl font-semibold mb-4">
        Welcome, {user?.name || "User"}!
      </h2>

      <div className="bg-gray-800 shadow rounded p-4">
        <h3 className="text-lg font-semibold mb-4 text-gray-100">
          {currentView === "frequent" ? "📈 Recent Frequent Metrics" : "📅 Daily Aggregated Metrics"}
        </h3>

        <AnimatePresence mode="wait">
          <motion.div
            key={currentView}
            initial="hidden"
            animate="visible"
            exit="exit"
            variants={tableVariants}
            transition={{ duration: 0.5 }}
          >
            <Table
              data={currentView === "frequent" ? frequentMetrics : []}
              isDaily={currentView === "daily"}
            />
          </motion.div>
        </AnimatePresence>

        <div className="flex justify-center gap-4 mt-4">
          <button
            onClick={() => setCurrentView("frequent")}
            disabled={currentView === "frequent"}
            className="px-4 py-2 bg-gray-600 text-white rounded disabled:opacity-40"
          >
            ◀ Prev
          </button>
          <button
            onClick={() => setCurrentView("daily")}
            disabled={currentView === "daily"}
            className="px-4 py-2 bg-gray-600 text-white rounded disabled:opacity-40"
          >
            Next ▶
          </button>
        </div>
      </div>
    </div>
  );
};

export default DashboardComponent;
