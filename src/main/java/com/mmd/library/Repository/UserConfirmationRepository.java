package com.mmd.library.Repository;

import com.mmd.library.entity.authentication.UserConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfirmationRepository extends JpaRepository<UserConfirmation, Long> {

	UserConfirmation findByToken(String token);
}
