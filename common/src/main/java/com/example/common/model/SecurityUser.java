package com.example.common.model;

import java.util.List;
import java.util.UUID;

public record SecurityUser(
    UUID id,
    String username,
    String password,
    List<String> permission
) {}
