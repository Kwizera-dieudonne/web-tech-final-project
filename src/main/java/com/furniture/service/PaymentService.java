package com.furniture.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furniture.domain.Payment;
import com.furniture.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

   
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Create or update a payment
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get a payment by ID
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    // Get payments by Order ID
    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

}
