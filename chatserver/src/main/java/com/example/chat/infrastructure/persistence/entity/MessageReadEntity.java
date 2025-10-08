package com.example.chat.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "ms_message_read")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MessageReadEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ms_message_read_id_gen")
	@SequenceGenerator(name = "ms_message_read_id_gen", sequenceName = "ms_message_read_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "message_id", nullable = false)
	private MessageEntity message;

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@NotNull
	@Column(name = "read_at", nullable = false)
	private LocalDateTime readAt;

	public MessageReadEntity(MessageEntity message, Long userId) {
		this.message = message;
		this.userId = userId;
		this.readAt = LocalDateTime.now();
	}

}