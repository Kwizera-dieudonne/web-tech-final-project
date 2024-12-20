package com.furniture.controller;



import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.furniture.domain.Role;
import com.furniture.domain.User;
import com.furniture.repository.UserRepository;
import com.furniture.service.UserService;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<?> initiateLogin(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        String password = request.get("password");


       try {
            String responseMessage = userService.initiateLogin(email, password);

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("User not found.");
            }
            User user = userOptional.get();

            session.setAttribute("user", user);

           // return ResponseEntity.ok(Map.of("message", responseMessage));
           return ResponseEntity.ok(Map.of(
            "message", responseMessage,
            "email", user.getEmail(),
            "role", user.getRole().name()
        ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }  

       /*  try {
            User user = userService.authenticateUser(email, password); // Use the method here
            session.setAttribute("user", user); // Store user in session
            return ResponseEntity.ok(Map.of(
                "message", "Login successful.",
                "role", user.getRole().name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }*/
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        boolean isVerified = userService.verifyOtp(email, otp);
        if (isVerified) {
            return ResponseEntity.ok(Map.of("message", "Login successful."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP."));
        }
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestBody Map<String, Object> payload) {
        try {
            
            String username = (String) payload.get("username");
            String email = (String) payload.get("email");
            String password = (String) payload.get("password");
            String confirmPassword = (String) payload.get("confirmPassword");
            String roleStr = (String) payload.get("role");

            
            Role role = roleStr != null ? Role.valueOf(roleStr) : Role.CUSTOMER;

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);

            
           // User newUser = userService.createUser(user, confirmPassword);
           userService.createUser(user, confirmPassword);

            return ResponseEntity.ok("Signup successful. Check your email to verify your account.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        boolean verified = userService.verifyEmail(token);
        if (verified) {
            return "Email verified successfully. You can now log in.";
        } else {
            return "Email verification or expired token.";
        }
    }


    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setUsername(userDetails.getUsername());
            updatedUser.setEmail(userDetails.getEmail());
            return ResponseEntity.ok(userService.updateUser(updatedUser));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
