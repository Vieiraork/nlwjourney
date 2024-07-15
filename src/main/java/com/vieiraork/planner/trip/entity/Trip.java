package com.vieiraork.planner.trip.entity;

import com.vieiraork.planner.Helper.Helpers;
import com.vieiraork.planner.trip.request.TripRequestPayLoad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    public Trip(TripRequestPayLoad data) {
        this.destination = data.destination();
        this.isConfirmed = false;
        this.ownerEmail  = data.owner_email();
        this.ownerName   = data.owner_name();
        this.startsAt    = Helpers.transformStringToLocalDatetime(data.starts_at());
        this.endsAt      = Helpers.transformStringToLocalDatetime(data.ends_at());
    }
}
