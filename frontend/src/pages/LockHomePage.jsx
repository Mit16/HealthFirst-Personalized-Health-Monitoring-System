import React from "react";
// import Waves from "../components/Waves";
import { HeroSectionOne } from "../components/HeroSectionOne";
import { TimelineDemo } from "../components/TimeLineDemo";
import { WorldMapDemo } from "../components/WorldMapDemo";
import { GlowingEffect } from "../components/ui/glowing-effect";
import { FooterComponent } from "../components/FooterComponent";
import ScrollReveal from "../components/ScrollReveal";


const LockHomePage = () => {
  return (
    <div className="relative">
      <HeroSectionOne />
      {/* <Waves
        lineColor="#fff"
        backgroundColor="rgba(255, 255, 255, 0.2)"
        waveSpeedX={0.02}
        waveSpeedY={0.01}
        waveAmpX={40}
        waveAmpY={20}
        friction={0.9}
        tension={0.01}
        maxCursorMove={120}
        xGap={12}
        yGap={36}
      /> */}
      <TimelineDemo />
      <WorldMapDemo />
      <GlowingEffect />
      <FooterComponent />
    </div>
  );
};

export default LockHomePage;
