// src/pages/PredictionPage.jsx
import React, { useContext } from "react";
import { HealthContext } from "../context/HealthContext";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import { FooterComponent } from "../components/FooterComponent";
import Header from "../components/Header";

const riskColorMap = {
  HIGH: "#ef4444", // red-500
  MEDIUM: "#facc15", // yellow-400
  LOW: "#22c55e", // green-500
};

const PredictionPage = () => {
  const { prediction, loading } = useContext(HealthContext);

  const predictionsRaw = Array.isArray(prediction)
  ? prediction
  : prediction
  ? [prediction]
  : [];

const predictionsArray = [...predictionsRaw].sort(
  (a, b) => new Date(b.timestamp) - new Date(a.timestamp) // for table (newest first)
);

const graphData = [...predictionsRaw]
  .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp)) // for graph (oldest first)
  .map((item) => ({
    timestamp: new Date(item.timestamp).toLocaleDateString(),
    risk: item.riskLevel,
    riskValue:
      item.riskLevel === "HIGH"
        ? 3
        : item.riskLevel === "MEDIUM"
        ? 2
        : item.riskLevel === "LOW"
        ? 1
        : 0,
  }));


  if (loading)
    return <p className="text-center text-gray-300">Loading predictions...</p>;

  return (
    <>
    <Header/>
    <div className="p-6 bg-black min-h-screen text-white">
      <h2 className="text-2xl font-bold mb-6">📊 Health Risk Predictions</h2>

      <div className="w-full h-64 mb-8">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart
            data={graphData}
            margin={{ top: 10, right: 30, left: 0, bottom: 0 }}
          >
            <XAxis dataKey="timestamp" stroke="#d1d5db" />
            <YAxis
              allowDecimals={false}
              ticks={[0, 1, 2, 3]}
              domain={[0, 3]}
              hide
            />
            <Tooltip
              formatter={(value) =>
                value === 3 ? "HIGH" : value === 2 ? "MEDIUM" : "LOW"
              }
            />
            <Area
              type="monotone"
              dataKey="riskValue"
              stroke="#06b6d4"
              fill="#06b6d4"
              strokeWidth={2}
              isAnimationActive={true}
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>

      <div className="overflow-x-auto bg-gray-800 rounded shadow mt-8">
        <table className="min-w-full divide-y divide-gray-700">
          <thead className="bg-gray-700">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">
                Date
              </th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">
                Risk Level
              </th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">
                Precautions
              </th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">
                Conditions
              </th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">
                Notes
              </th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">
                User ID
              </th>
            </tr>
          </thead>
          <tbody className="bg-gray-800 divide-y divide-gray-700">
            {predictionsArray.map((item, index) => (
              <tr key={index}>
                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-200">
                  {item.timestamp
                    ? new Date(item.timestamp).toLocaleDateString()
                    : "N/A"}
                </td>
                <td className="px-4 py-3 whitespace-nowrap text-sm font-medium">
                  <span
                    className={`px-3 py-1 rounded-full text-xs font-semibold text-white ${
                      item.riskLevel === "HIGH"
                        ? "bg-red-600"
                        : item.riskLevel === "MEDIUM"
                        ? "bg-yellow-500"
                        : item.riskLevel === "LOW"
                        ? "bg-green-600"
                        : "bg-gray-500"
                    }`}
                  >
                    {item.riskLevel ?? "N/A"}
                  </span>
                </td>
                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-200">
                  {item.precautions ?? "N/A"}
                </td>
                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-200">
                  {item.possibleConditions ?? "N/A"}
                </td>
                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-200">
                  {item.additionalNotes ?? "N/A"}
                </td>
                <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-200">
                  {item.userId ?? "N/A"}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
    <FooterComponent/>
    </>
  );
};

export default PredictionPage;
