package com.example.chat.core.tests;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestDocument {

    @Id
    private String id;

    private String sender;
    private String content;
    private LocalDateTime createdAt;
}
