// src/App.js

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';



import Home from './Home';
import SignUp from './SignUp';
import Footer from './Footer'
import Login from './Login';
import Furniture from './Furniture';
import './App.css'
import Navbar from './Navbar';
import HeroSection from './HeroSection';
import FurnitureParent from './FurnitureParent';



function MainComponents() {
  return (
    <>
      <Navbar />
      <HeroSection />
      <FurnitureParent />
      
      
      <Footer />
      
    </>
  );
}

function App() {
  return (
    <Router>
      <div className="App">   
        <Routes>
          <Route path="/" element={<MainComponents />} />
          <Route path="/login" element={<Login/>}/>
          <Route path="/signup" element={<SignUp/>} />
          <Route path="/admin" element={<Furniture/>} />
        </Routes>       
      </div>
    </Router>
  );
}

export default App;   
