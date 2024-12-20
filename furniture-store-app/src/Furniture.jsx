import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Furniture.css"; // Include the CSS file

const Furniture = () => {
  const [furnitureData, setFurnitureData] = useState({
    name: "",
    quantity: "",
    price: "",
    category: "",
    image: null,
  });

  const [responseMessage, setResponseMessage] = useState("");
  const [categories, setCategories] = useState([]);
  const [imagePreview, setImagePreview] = useState(null); // State for image preview

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/categories/all");
        setCategories(response.data);
      } catch (error) {
        console.error("Error fetching categories", error);
      }
    };

    fetchCategories();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFurnitureData({ ...furnitureData, [name]: value });
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setFurnitureData({ ...furnitureData, image: file });
    setImagePreview(URL.createObjectURL(file)); // Set image preview URL
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("name", furnitureData.name);
    formData.append("quantity", furnitureData.quantity);
    formData.append("price", furnitureData.price);
    formData.append("category", furnitureData.category);
    formData.append("file", furnitureData.image);

    try {
      const response = await axios.post("http://localhost:8080/api/furniture/saveFurniture", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      setResponseMessage("Furniture saved successfully!");
      setImagePreview(null); // Clear image preview after success
    } catch (error) {
      setResponseMessage("Failed to save furniture.");
      console.error("Error:", error);
    }
  };

  return (
    <div className="form-container">
      <h2>Add New Furniture</h2>
      <form onSubmit={handleSubmit} encType="multipart/form-data">
        <div className="form-group">
          <label>Furniture Name:</label>
          <input
            type="text"
            name="name"
            value={furnitureData.name}
            onChange={handleInputChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Quantity:</label>
          <input
            type="number"
            name="quantity"
            value={furnitureData.quantity}
            onChange={handleInputChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Price:</label>
          <input
            type="number"
            name="price"
            value={furnitureData.price}
            onChange={handleInputChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Category:</label>
          <select
            name="category"
            value={furnitureData.category}
            onChange={handleInputChange}
            required
          >
            <option value="">Select Category</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Upload Image:</label>
          <input
            type="file"
            name="image"
            accept="image/*"
            onChange={handleImageChange}
            required
          />
          {/* Display image preview */}
          {imagePreview && (
            <div className="image-preview">
              <img src={imagePreview} alt="Preview" />
            </div>
          )}
        </div>

        <button type="submit" className="submit-btn">
          Save Furniture
        </button>
      </form>

      {responseMessage && <p className="response-message">{responseMessage}</p>}
    </div>
  );
};

export default Furniture;
