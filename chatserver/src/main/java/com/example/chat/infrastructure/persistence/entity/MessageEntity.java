package com.example.chat.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "ms_message")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ms_message_id_gen")
	@SequenceGenerator(name = "ms_message_id_gen", sequenceName = "ms_message_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@Column(name = "room_id", nullable = false)
	private Long roomId;


	@Size(max = 50)
	@Column(name = "type", nullable = false, length = 50)
	private String type;

	@NotNull
	@Column(name = "sender_id", nullable = false)
	@CreatedBy
	private Long senderId;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "details")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> details;

}