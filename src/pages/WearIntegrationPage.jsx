import React from "react";
import TracingBeamGuide from "../components/TracingBeamGuide";
import SpotlightTokenCard from "../components/SpotlightTokenCard";
import Header from "../components/Header";
import { FooterComponent } from "../components/FooterComponent";

const WearIntegrationPage = () => {
  return (
    <>
    <Header/>
    <div className="py-10 px-4">
      <h2 className="text-2xl font-bold text-center text-white mb-8">🧠 Wear OS Integration</h2>
      <SpotlightTokenCard />
      <TracingBeamGuide />
      <FooterComponent/>
    </div>
    </>
  );
};

export default WearIntegrationPage;
