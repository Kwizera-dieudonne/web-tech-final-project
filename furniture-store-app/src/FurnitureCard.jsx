{/*import React, { useState, useEffect } from 'react'
import './FurnitureCard.css'

const FurnitureCard = ({image, name, price}) => {

  const [furnitureData, setFurnitureData] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(4);
  const [userId] = useState(1);  // Assuming the user is logged in and their ID is 1 for this example

  useEffect(() => {
    // Fetch data from database and update state
    const fetchFurnitureData = async () => {
      const response = await fetch('http://localhost:8080/api/furniture/all');
      const data = await response.json();
      setFurnitureData(data);
    };
    fetchFurnitureData();
  }, []);

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = furnitureData.slice(indexOfFirstItem, indexOfLastItem);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const orderFurniture = async (furnitureId) => {
    // Create an order object
    const order = {
      user: { id: userId },
      totalAmount: price, // Assuming price is for one item; if quantity, multiply by price
      orderDate: new Date(),
      status: 'PENDING',
      orderItems: [
        {
          furniture: { id: furnitureId },
          quantity: 1, // Assume quantity of 1 for simplicity
        }
      ]
    };
    const response = await fetch('http://localhost:8080/api/orders/saveOrder', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(order),
    });

    if (response.ok) {
      const data = await response.json();
      console.log('Order created successfully:', data);
    } else {
      console.error('Error creating order');
    }
  };


  return (
    <div className="furniture-app">
      <h1>Our Furniture</h1>
      <div className="furniture-grid">
        {currentItems.map((furniture) => (
          <div key={furniture.id} className="furniture-card">
            <img src={furniture.image} alt={furniture.name} />
            <h3>{furniture.name}</h3>
            <p>Price: {furniture.price} Fr</p>
            {/* Add any additional details or actions you want to include */}  {/*
            <button onClick={() => orderFurniture(furniture.id)} className="order-button">
              Order Now
            </button>
          </div>
        ))}
      </div>
      <div className="pagination">
        {Array.from({ length: Math.ceil(furnitureData.length / itemsPerPage) }, (_, i) => i + 1).map((pageNumber) => (
          <button
            key={pageNumber}
            className={currentPage === pageNumber ? 'active' : ''}
            onClick={() => paginate(pageNumber)}
          >
            {pageNumber}
          </button>
        ))}
      </div>
    </div>
  )
}

export default FurnitureCard */}


import React from 'react';
import './FurnitureCard.css';

const FurnitureCard = ({ furnitureData, onOrder }) => {
  return (
    <div className="furniture-app">
      <h1>Our Furniture</h1>
      <div className="furniture-grid">
        {furnitureData.map((furniture) => (
          <div key={furniture.id} className="furniture-card">
            <img src={furniture.image} alt={furniture.name} />
            <h3>{furniture.name}</h3>
            <p>Price: {furniture.price} Fr</p>
            <button onClick={() => onOrder(furniture.id)} className="order-button">
              Order Now
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FurnitureCard;

