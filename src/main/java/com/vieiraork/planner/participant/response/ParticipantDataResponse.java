package com.vieiraork.planner.participant.response;

import java.util.UUID;

public record ParticipantDataResponse(UUID id, String name, String email, Boolean isConfirmed) {
}
