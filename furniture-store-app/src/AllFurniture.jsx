// src/components/AllFurniture.jsx

import React, { useState, useEffect } from 'react';
import FurnitureCard from './FurnitureCard';

const AllFurniture = () => {
  const [furnitureData, setFurnitureData] = useState([]);

  useEffect(() => {
    const fetchFurniture = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/furniture/all');
        const data = await response.json();
        setFurnitureData(data); // Set the fetched data to state
      } catch (error) {
        console.error('Error fetching furniture data:', error);
      }
    };

    fetchFurniture();
  }, []);  // Fetch data when the component is mounted

  return (
    <div>
      <h2>All Furniture</h2>
      <div className="furniture-list">
        {furnitureData.length === 0 ? (
          <p>No furniture available.</p>
        ) : (
          furnitureData.map((furniture) => (
            <FurnitureCard key={furniture.id} furniture={furniture} />
          ))
        )}
      </div>
    </div>
  );
};

export default AllFurniture;
