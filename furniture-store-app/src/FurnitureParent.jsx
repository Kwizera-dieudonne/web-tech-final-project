import React, { useState, useEffect } from 'react';
import axios from 'axios';
import FurnitureCard from './FurnitureCard';

const FurnitureParent = () => {
  const [furnitureData, setFurnitureData] = useState([]);
  const [filteredFurniture, setFilteredFurniture] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 4;

  useEffect(() => {
    // Fetch all furniture
    const fetchFurniture = async () => {
      const response = await axios.get('http://localhost:8080/api/furniture/all');
      setFurnitureData(response.data);
      setFilteredFurniture(response.data); // Initially display all furniture
    };

    // Fetch categories
    const fetchCategories = async () => {
      const response = await axios.get('http://localhost:8080/api/categories/all');
      setCategories(response.data);
    };

    fetchFurniture();
    fetchCategories();
  }, []);

  const handleCategoryChange = async (categoryId) => {
    setSelectedCategory(categoryId);
    setCurrentPage(1); // Reset to the first page on category change

    if (categoryId) {
      // Fetch furniture by selected category
      const response = await axios.get(`http://localhost:8080/api/furniture/by-category/${categoryId}`);
      setFilteredFurniture(response.data);
    } else {
      // Reset to all furniture if no category is selected
      setFilteredFurniture(furnitureData);
    }
  };

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  // Pagination logic
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredFurniture.slice(indexOfFirstItem, indexOfLastItem);

  const orderFurniture = async (furnitureId) => {
    // Logic to order furniture goes here
    console.log('Order furniture with ID:', furnitureId);
  };

  return (
    <div>
      <h1>Furniture Store</h1>

      {/* Category Dropdown */}
      <select value={selectedCategory} onChange={(e) => handleCategoryChange(e.target.value)}>
        <option value="">All Categories</option>
        {categories.map((category) => (
          <option key={category.id} value={category.id}>
            {category.name}
          </option>
        ))}
      </select>

      {/* Pass paginated furniture data to FurnitureCard */}
      <FurnitureCard furnitureData={currentItems} onOrder={orderFurniture} />

      {/* Pagination Controls */}
      <div className="pagination">
        {Array.from({ length: Math.ceil(filteredFurniture.length / itemsPerPage) }, (_, i) => i + 1).map((pageNumber) => (
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
  );
};

export default FurnitureParent;
