package com.vieiraork.planner.activity.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityCreateResponse(UUID id, String title, LocalDateTime occurs_at) {
}