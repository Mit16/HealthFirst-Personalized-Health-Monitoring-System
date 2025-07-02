import React, { useState, useContext } from "react";
import QRCode from "react-qr-code";
import SpotlightCard from "./SpotlightCard";
import { AuthContext } from "../context/AuthContext";

const SpotlightTokenCard = () => {
  const [error, setError] = useState("");
  const { wearToken, generateWearToken } = useContext(AuthContext);

  const handleGenerate = async () => {
    const result = await generateWearToken();
    if (!result.success) {
      setError(result.error);
    } else {
      setError("");
    }
  };

  return (
    <SpotlightCard
      spotlightColor="rgba(0, 229, 255, 0.2)"
      className="w-full max-w-2xl mx-auto mt-6"
    >
      <div className="text-white text-center flex flex-col items-center justify-center">
        <h3 className="text-xl font-bold mb-2">🎯 Generate Pairing Token</h3>

        <p className="text-sm text-neutral-300 mb-4 max-w-md">
          Click to generate a unique token that your Wear OS device can use to connect securely.
        </p>

        <button
          onClick={handleGenerate}
          className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded mb-4"
        >
          🔐 Generate Token
        </button>

        {error && <p className="text-red-500 mt-2">{error}</p>}

        {wearToken && (
          <div className="mt-6 flex flex-col items-center">
            <p className="text-sm font-medium text-neutral-300 mb-2">
              🔗 Your Token:
            </p>

            <div className="bg-gray-800 text-white px-4 py-2 rounded text-sm mb-4">
              {wearToken}
            </div>

            <div className="border border-white p-4 rounded-lg shadow-lg bg-white">
              <QRCode value={wearToken} size={160} />
            </div>

            <p className="text-xs mt-2 text-neutral-400">
              Scan this in your Wear OS app
            </p>
          </div>
        )}
      </div>
    </SpotlightCard>
  );
};

export default SpotlightTokenCard;
