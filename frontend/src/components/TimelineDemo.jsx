import React from "react";
import { Timeline } from "./ui/timeline";

// Importing assets directly
import ImgBrainstorm from "../assets/10541562_18915856.svg";
import ImgIdeaSketch from "../assets/21118169_6428081.svg";
import ImgArch from "../assets/31576.jpg";
import ImgSchema from "../assets/emile-perron-xrVDYZRGdw4-unsplash.jpg";
import ImgWearOS from "../assets/daniel-korpai-QhF3YGsDrYk-unsplash.jpg";
import ImgFuture from "../assets/alexander-ruiz-sHAucwW08Fk-unsplash.jpg";

export function TimelineDemo() {
  const data = [
    {
      title: "Idea & Inspiration (March 2025)",
      content: (
        <div>
          <p className="mb-8 text-xs font-normal text-neutral-800 md:text-sm dark:text-neutral-200">
            The idea sparked from observing the growing need for real-time, personalized health monitoring. I envisioned a system that could combine IoT, AI/ML, and wearable tech into a single ecosystem.
          </p>
          <div className="grid grid-cols-2 gap-4">
            <img src={ImgBrainstorm} alt="brainstorming" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
            <img src={ImgIdeaSketch} alt="idea sketch" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
          </div>
        </div>
      ),
    },
    {
      title: "Planning & Architecture (April 2025)",
      content: (
        <div>
          <p className="mb-8 text-xs font-normal text-neutral-800 md:text-sm dark:text-neutral-200">
            Carefully analyzed requirements, designed database schemas, and planned a scalable microservices architecture. Focused on security, modularity, and future-proofing the system.
          </p>
          <div className="grid grid-cols-2 gap-4">
            <img src={ImgArch} alt="architecture planning" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
            <img src={ImgSchema} alt="database schema" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
          </div>
        </div>
      ),
    },
    {
      title: "Backend, AI & Website (May 2025)",
      content: (
        <div>
          <p className="mb-4 text-xs font-normal text-neutral-800 md:text-sm dark:text-neutral-200">
            Developed Spring Boot-based microservices: user auth, health metrics, prediction, and alerts. Integrated AI using Gemini APIs. Built the website dashboard with real-time health data visualizations.
          </p>
          <div className="grid grid-cols-2 gap-4">
            <img src="https://images.unsplash.com/photo-1555949963-aa79dcee981c" alt="backend development" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
            <img src="https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5" alt="AI integration" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
          </div>
        </div>
      ),
    },
    {
      title: "WearOS Integration & Future Roadmap (June 2025)",
      content: (
        <div>
          <p className="mb-4 text-xs font-normal text-neutral-800 md:text-sm dark:text-neutral-200">
            Developed a WearOS app to sync vital signs from smartwatch sensors. Optimized for offline queuing and background syncing. Future goals include anomaly detection, user feedback loop, and medical API integrations.
          </p>
          <div className="grid grid-cols-2 gap-4">
            <img src={ImgWearOS} alt="wearos" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
            <img src={ImgFuture} alt="future healthcare" className="h-20 w-full rounded-lg object-cover shadow-md md:h-44 lg:h-60" />
          </div>
        </div>
      ),
    },
  ];
  return (
    <div className="relative w-full overflow-clip">
      <Timeline data={data} />
    </div>
  );
}
