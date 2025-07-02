import React from "react";
import { TracingBeam } from "./ui/TracingBeam";

const TracingBeamGuide = () => {
  return (
    <TracingBeam className="px-4 pt-6">
      <div className="max-w-2xl mx-auto antialiased">
        <div className="mb-10">
          <h2 className="bg-indigo-700 text-white rounded-full text-sm w-fit px-4 py-1 mb-4">
            Wear OS Setup
          </h2>

          <p className="text-xl font-semibold mb-4 text-white">
            How to Link Your Watch
          </p>

          <div className="prose prose-sm dark:prose-invert text-neutral-300">
            <ol className="list-decimal pl-4 space-y-2">
              <li>Open the Wear OS app on your smartwatch.</li>
              <li>Go to the "Pair with Web" option.</li>
              <li>Scan the QR code or manually enter the token.</li>
              <li>Once paired, your health data will sync automatically.</li>
            </ol>
          </div>
        </div>
      </div>
    </TracingBeam>
  );
};

export default TracingBeamGuide;
