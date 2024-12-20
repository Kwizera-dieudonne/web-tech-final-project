import React, { useState } from "react";
import "./Navbar.css";
import axios from "axios";
import FurnitureCard from "./FurnitureCard"; // Import the FurnitureCard component
import Home from "./Home";
import { Link } from 'react-router-dom';

function Navbar() {
  const [query, setQuery] = useState(""); // State to store the search query
  const [furniture, setFurniture] = useState([]); // State for search results
  const [error, setError] = useState(""); // State for error messages

  const handleSearch = async () => {
    if (!query.trim()) {
      setError("Please enter a search term.");
      setFurniture([]);
      return;
    }
    try {
      const response = await axios.get(`http://localhost:8080/api/furniture/search?query=${query}`);
      setFurniture(response.data); // Update state with search results
      setError("");
    } catch (error) {
      if (error.response && error.response.status === 404) {
        setError("No furniture found.");
        setFurniture([]);
      } else {
        console.error("Error performing search:", error);
        setError("An error occurred. Please try again.");
      }
    }
  };

  const handleOrder = (furnitureId) => {
    console.log("Order placed for furniture ID:", furnitureId);
    // You can integrate the order logic here if needed
  };

  return (
    <>
      <nav className="navbar">
        <div className="logo">
          Furnique<span className="dot">.</span>
        </div>
        <div className="search-bar">
          <input
            type="text"
            placeholder="I'm shopping for..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button onClick={handleSearch}>Search</button>
        </div>
        <div className="header-right">
        <Home />
      </div>
      <Link to='/login' className='signup__links'>Login</Link>
        <div className="nav-icons">
          <span>🛒</span>
        </div>
      </nav>
      <div className="results">
        {error && <p className="error">{error}</p>}
        <FurnitureCard furnitureData={furniture} onOrder={handleOrder} />
      </div>
    </>
  );
}

export default Navbar;
