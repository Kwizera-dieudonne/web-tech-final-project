package com.furniture.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import com.furniture.domain.Role;
import com.furniture.domain.User;
import com.furniture.repository.UserRepository;

@Service
public class UserService {

    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

     private Map<String, String> otpStorage = new HashMap<>();

      public String initiateLogin(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent() && password.equals(userOptional.get().getPassword())) {
            // Generate 6-digit OTP
            String otp = String.valueOf(new Random().nextInt(900000) + 100000);
            otpStorage.put(email, otp);

            // Send OTP via email
            sendOtpByEmail(email, otp);

            return "OTP sent to your email. Please verify.";
        } else {
            throw new IllegalArgumentException("Invalid email or password.");
        }
    }

    public boolean verifyOtp(String email, String inputOtp) {
        String storedOtp = otpStorage.get(email);
        if (storedOtp != null && storedOtp.equals(inputOtp)) {
            otpStorage.remove(email); // Remove OTP after successful verification
            return true;
        }
        return false;
    }


    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + ". It is valid for 5 minutes.");

        mailSender.send(message);
        System.out.println("OTP sent to email: " + email);
    }
    //private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  put security dependency
 
        public User createUser(User user, String confirmPassword) {
            //will check if email is already used
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()){
                throw new IllegalArgumentException("Email is already in use.");
            }

            //confirm is password is match
            if (!user.getPassword().equals(confirmPassword)) {
                throw new IllegalArgumentException("passwords do not match.");
            }
            if (user.getRole() == null) {
                user.setRole(Role.CUSTOMER);
            }

            //save the user harsh the password later
           // user.setPassword(passwordEncoder.encode(user.getPassword()));
            //userRepository.save(user);
            userRepository.save(user);
            
            return user;
        }

       
    
        public List<User> getAllUsers() {
            return userRepository.findAll();
        }
    
        public Optional<User> getUserById(Long id) {
            return userRepository.findById(id);
        }
    
        public User updateUser(User user) {
            return userRepository.save(user);
        }
    
        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }  
        
        public boolean verifyEmail(String email) {
           
            return true; 
        }


        public User authenticateUser(String email, String password) {
            // Fetch the user by email
            Optional<User> userOptional = userRepository.findByEmail(email);
    
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("Invalid email or password.");
            }
    
            User user = userOptional.get();
    
            // Verify the password (assuming passwords are hashed)
            if (!user.getPassword().equals(password)) { // Replace with hashing comparison if passwords are hashed
                throw new IllegalArgumentException("Invalid email or password.");
            }
    
            return user;
        }
    
}
