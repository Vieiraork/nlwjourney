package com.vieiraork.planner.participant;

import com.vieiraork.planner.participant.entity.Participant;
import com.vieiraork.planner.participant.repository.ParticipantRepository;
import com.vieiraork.planner.participant.request.ParticipantRequestPayLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository repository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> createParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayLoad payLoad) {
        Optional<Participant> participant = this.repository.findById(id);

        if (participant.isPresent()) {
            Participant rawParticipant = participant.get();

            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payLoad.name());
            this.repository.save(rawParticipant);

            return ResponseEntity.ok(rawParticipant);
        }

        return ResponseEntity.notFound().build();
    }
}
