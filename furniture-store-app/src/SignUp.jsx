import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './signup.css'
import Navbar from './Navbar';

const SignUp = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [error, setError] = useState(''); // Store error message
  const [message, setMessage] = useState(''); // Store success message

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validateForm = () => {
    if (!formData.username || !formData.email || !formData.password || !formData.confirmPassword) {
      setError('Please fill in all fields');
      return false;
    }

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return false;
    }

    // Simple email regex validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      setError('Invalid email address');
      return false;
    }

    setError('');
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate form before submitting
    if (!validateForm()) {
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/api/users/signup', formData);
      setMessage('Sign up successful!');
      setFormData({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
      }); // Clear the form after successful submission
    } catch (error) {
      // Handle errors from the backend
      setError('Sign up failed. Please try again.');
    }
  };

  return (
    <>
    <Navbar />
    <div className='signup__home sign__bg'>
      <div className="signup__card">
        <div className="signup__forms">
          <h2 className='signup__header'>Sign Up</h2>
          {error && <span className="error">{error}</span>}
          {message && <span className="message">{message}</span>}
          <form onSubmit={handleSubmit} className="sign__form">
            <label className='login__label'>Username</label>
            <input
              type='text'
              placeholder='Enter Username'
              name="username"
              className='login__input'
              value={formData.username}
              onChange={handleChange}
            />
            <label className='login__label'>Email</label>
            <input
              type='email'
              placeholder='Enter Email'
              name="email"
              className='login__input'
              value={formData.email}
              onChange={handleChange}
            />
            <label className='login__label'>Password</label>
            <input
              type='password'
              placeholder='Enter Password'
              name="password"
              className='login__input'
              value={formData.password}
              onChange={handleChange}
            />
            <label className='login__label'>Confirm Password</label>
            <input
              type='password'
              placeholder='Confirm Password'
              name="confirmPassword"
              className='login__input'
              value={formData.confirmPassword}
              onChange={handleChange}
            />
            <button type='submit' className='form__btn'>Sign up</button>
            <Link to='/login' className='signup__links'>Already have an account? Login</Link>
          </form>
        </div>
      </div>

      <div className="signup__content">
        <h2 className='headers'>Join Our Fast Growing Company</h2>
        <p className="headers__p">Find better homes to create memories with your loved ones.</p>
      </div>
    </div>
    </>
  );
};

export default SignUp;
