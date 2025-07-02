import React, { useContext } from "react";
import Header from "../components/Header";
import { HealthContext } from "../context/HealthContext";

const AlertsPage = () => {
  const { alerts, loading } = useContext(HealthContext);

  return (
    <>
      <Header />
      <div className="max-w-4xl mx-auto p-6">
        <h2 className="text-2xl font-bold mb-6 text-gray-800 dark:text-gray-100">
          ⚠ Active Alerts
        </h2>

        {loading ? (
          <p className="text-gray-600 dark:text-gray-400">Loading alerts...</p>
        ) : alerts.length === 0 ? (
          <p className="text-green-600 dark:text-green-400">✅ No active alerts!</p>
        ) : (
          <ul className="space-y-4">
            {alerts.map((alert) => (
              <li
                key={alert.id}
                className="p-4 rounded-xl shadow border border-red-300 dark:border-red-700 bg-red-50 dark:bg-red-900"
              >
                <p className="text-sm text-gray-700 dark:text-gray-200">
                  <strong className="text-red-800 dark:text-red-300">User ID:</strong>{" "}
                  {alert.userId}
                </p>
                <p className="mt-1 text-base font-medium text-red-900 dark:text-red-200">
                  {alert.message}
                </p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </>
  );
};

export default AlertsPage;
