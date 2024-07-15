package com.vieiraork.planner.trip;

import com.vieiraork.planner.activity.request.ActivityRequestPayLoad;
import com.vieiraork.planner.activity.response.ActivityCreateResponse;
import com.vieiraork.planner.activity.response.ActivityDataResponse;
import com.vieiraork.planner.activity.service.ActivityService;
import com.vieiraork.planner.link.request.LinkRequestPayLoad;
import com.vieiraork.planner.link.response.LinkResponse;
import com.vieiraork.planner.link.service.LinkService;
import com.vieiraork.planner.participant.request.ParticipantRequestPayLoad;
import com.vieiraork.planner.participant.response.ParticipantCreateResponse;
import com.vieiraork.planner.participant.response.ParticipantDataResponse;
import com.vieiraork.planner.participant.service.ParticipantService;
import com.vieiraork.planner.trip.entity.Trip;
import com.vieiraork.planner.trip.repository.TripRepository;
import com.vieiraork.planner.trip.request.TripRequestPayLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripRepository repository;
    
    @PostMapping
    public ResponseEntity<String> createTrip(@RequestBody TripRequestPayLoad payLoad) {
        Trip newTrip = new Trip(payLoad);

        this.repository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payLoad.emails_to_invite(), newTrip);

        return ResponseEntity.ok(newTrip.getId().toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayLoad payLoad) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setEndsAt(LocalDateTime.parse(payLoad.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payLoad.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payLoad.destination());

            this.repository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setIsConfirmed(true);
            this.repository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayLoad payLoad) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payLoad.email(), rawTrip);

            if (rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payLoad.email());

            return ResponseEntity.ok(participantResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantDataResponse>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantDataResponse> participantList = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(participantList);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityCreateResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayLoad payLoad) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityCreateResponse activityCreateResponse = this.activityService.registerActivity(payLoad, rawTrip);

            return ResponseEntity.ok(activityCreateResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityDataResponse>> getAllActivitiesFromId(@PathVariable UUID id) {
        List<ActivityDataResponse> activityDataList = this.activityService.getAllActivitiesFromTrip(id);

        return ResponseEntity.ok(activityDataList);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, LinkRequestPayLoad payLoad) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            LinkResponse linkCreateResponse = this.linkService.registerLink(payLoad, rawTrip);

            return ResponseEntity.ok(linkCreateResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkResponse>> getAllLinks(@PathVariable UUID id) {
        return ResponseEntity.ok(this.linkService.getLinksByTripId(id));
    }
}
