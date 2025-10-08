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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ms_user_chat_room")
public class UserChatRoomEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ms_user_chat_room_id_gen")
	@SequenceGenerator(name = "ms_user_chat_room_id_gen", sequenceName = "ms_user_chat_room_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoomEntity chatRoom;

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Size(max = 255)
	@Column(name = "room_name")
	private String roomName;

	@NotNull
	@ColumnDefault("now()")
	@Column(name = "joined_at", nullable = false)
	private LocalDateTime joinedAt;

}