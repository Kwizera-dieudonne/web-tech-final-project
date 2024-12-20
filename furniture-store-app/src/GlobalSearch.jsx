import React, { useState } from "react";
import axios from "axios";

function GlobalSearch({ onSearch }) {
  const [searchText, setSearchText] = useState("");

  const handleSearch = async () => {
    if (searchText.trim() === "") return; // Do nothing if the search text is empty
    try {
     
      onSearch(searchText);
    } catch (error) {
      console.error("Error performing search", error);
    }
  };

  return (
    <div className="global-search">
      <input
        type="text"
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
        placeholder="Search for furniture..."
      />
      <button onClick={handleSearch}>Search</button>
    </div>
  );
}

export default GlobalSearch;
