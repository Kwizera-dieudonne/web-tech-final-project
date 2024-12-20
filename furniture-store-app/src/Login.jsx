import React, { useState, useEffect } from 'react';
import { FaInstagram, FaWhatsapp } from "react-icons/fa";
import { FaXTwitter } from "react-icons/fa6";
import { Link, useNavigate } from 'react-router-dom';
import './login.css';
import Navbar from './Navbar';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [otp, setOtp] = useState('');
  const [token, setToken] = useState('');
  const [isOtpRequired, setIsOtpRequired] = useState(false);
  const [error, setError] = useState('');
  const [otpSent, setOtpSent] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    if (!email || !password) {
      setError('Email and Password are required');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok) {
        if (data.role && data.message === 'Login successful.') {
          // User logged in successfully without OTP
          handlePostLogin(data.token, data.role);
        } else {
          // OTP required
          setIsOtpRequired(true);
          setOtpSent(true);
          setToken(data.token); // Save temporary token if needed for OTP
        }
      } else {
        setError(data.error || 'Invalid login credentials.');
      }
    } catch (error) {
      console.error('Login failed:', error);
      setError('Something went wrong. Please try again later.');
    }
  };

  const handleOtpVerification = async (e) => {
    e.preventDefault();
    setError('');

    if (!otp) {
      setError('OTP is required');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/users/verify-otp', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, otp }),
      });

      const data = await response.json();

      if (response.ok) {
        // OTP verified successfully
        handlePostLogin(token, data.role);
      } else {
        setError(data.error || 'Invalid or expired OTP.');
      }
    } catch (error) {
      console.error('OTP verification failed:', error);
      setError('Something went wrong. Please try again later.');
    }
  };

  const handlePostLogin = (token, role) => {
    sessionStorage.setItem('isAuthenticated', true);
    sessionStorage.setItem('userRole', role);
    sessionStorage.setItem('token', token);

    if (role === 'ADMIN') {
      navigate('/admin');
    } else if (role === 'USER') {
      navigate('/houses');
    } else {
      navigate('/');
    }
  };

  return (
    <>
    <Navbar />
    <div className="login__home">
      <div className="login__card">
        <div className="login__images">
          <p className="login__p">Join Us Now</p>
          <div className="login__icons">
            <FaInstagram className="icon__login" />
            <FaXTwitter className="icon__login" />
            <FaWhatsapp className="icon__login" />
          </div>
        </div>
        <div className="login__content">
          <h2 className="login__header">{isOtpRequired ? 'Verify OTP' : 'Login'}</h2>
          {error && <span className="error">{error}</span>}

          {!isOtpRequired ? (
            <form onSubmit={handleLogin} className="login__form">
              <label className="login__label">Email</label>
              <input
                type="email"
                placeholder="Enter email"
                className="login__input"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <label className="login__label">Password</label>
              <input
                type="password"
                placeholder="Enter password"
                className="login__input"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <a href="#home" className="login__link">Forgot password?</a>
              <button type="submit" className="form__btn">Login</button>
              <Link to="/signup" className="login__links">Don't have an account? Sign Up</Link>
            </form>
          ) : (
            <form onSubmit={handleOtpVerification} className="login__form">
              <label className="login__label">Enter OTP</label>
              <input
                type="text"
                placeholder="Enter OTP"
                className="login__input"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
              />
              <button type="submit" className="form__btn">Verify OTP</button>
            </form>
          )}
        </div>
      </div>
    </div>
    </>
  );
};

export default Login;
