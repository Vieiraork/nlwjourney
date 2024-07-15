package com.vieiraork.planner.trip.repository;

import com.vieiraork.planner.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
