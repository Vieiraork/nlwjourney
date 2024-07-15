package com.vieiraork.planner.participant.service;

import com.vieiraork.planner.participant.entity.Participant;
import com.vieiraork.planner.participant.repository.ParticipantRepository;
import com.vieiraork.planner.participant.response.ParticipantCreateResponse;
import com.vieiraork.planner.participant.response.ParticipantDataResponse;
import com.vieiraork.planner.trip.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository repository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.repository.saveAll(participants);

        System.out.println(participants.getFirst().getId());
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        this.repository.save(participant);

        return new ParticipantCreateResponse(participant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {}

    public List<ParticipantDataResponse> getAllParticipantsFromEvent(UUID tripId) {
        return this.repository.findByTripId(tripId).stream().map(participant ->
                new ParticipantDataResponse(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())).toList();
    }

    public void triggerConfirmationEmailToParticipant(String email) {

    }
}
