import React from "react";
// import { NavbarDemo } from "../components/NavbarDemo";
import { GlowingEffect } from "../components/ui/glowing-effect";
import { WorldMapDemo } from "../components/WorldMapDemo";
import { TimelineDemo } from "../components/TimeLineDemo";
import Header from "../components/Header";
import { FooterComponent } from "../components/FooterComponent";
import { FeaturesSectionDemo } from "../components/FeaturesSectionDemo";

const HomePage = () => {
  return (
    <div>
      <Header/>
      <FeaturesSectionDemo/>
      <GlowingEffect />
      <TimelineDemo />
      <WorldMapDemo />
      <FooterComponent />
    </div>
  );
};

export default HomePage;
