package com.mmd.library.Repository;

import com.mmd.library.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	public Payment findByUserEmail(String userEmail);
}
