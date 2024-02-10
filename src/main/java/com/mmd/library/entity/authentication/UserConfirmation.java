package com.mmd.library.entity.authentication;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_confirmations")
@Data
@NoArgsConstructor
public class UserConfirmation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String token;

	@Column(name = "time")
	private LocalDateTime localDateTime;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "username")
	private User user;

	public UserConfirmation(User user) {
		this.token = UUID.randomUUID().toString();
		this.localDateTime = LocalDateTime.now();
		this.user = user;
	}
}
