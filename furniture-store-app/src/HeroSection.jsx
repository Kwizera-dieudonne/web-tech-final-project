import React from "react";
import "./HeroSection.css";
import furnitureImage from "./assets/background-furniture.jpg"; // Import the image

function HeroSection() {
  return (
    <div
      className="hero-section"
      style={{ backgroundImage: `url(${furnitureImage})` }}
    >
      <div className="hero-content">
        <h4>We work for</h4>
        <h1>
          Insuring Your Future <br /> From Today
        </h1>
        <button className="work-btn">WORK WITH US</button>
      </div>
    </div>
  );
}

export default HeroSection;
