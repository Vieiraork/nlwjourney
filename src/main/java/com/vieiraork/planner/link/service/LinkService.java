package com.vieiraork.planner.link.service;

import com.vieiraork.planner.link.entity.Link;
import com.vieiraork.planner.link.repository.LinkRepository;
import com.vieiraork.planner.link.request.LinkRequestPayLoad;
import com.vieiraork.planner.link.response.LinkResponse;
import com.vieiraork.planner.trip.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repository;

    public LinkResponse registerLink(LinkRequestPayLoad payLoad, Trip trip) {
        Link rawLink = new Link(payLoad.title(), payLoad.url(), trip);

        this.repository.save(rawLink);

        return new LinkResponse(rawLink.getId(), rawLink.getTitle(), rawLink.getUrl());
    }

    public List<LinkResponse> getLinksByTripId(UUID tripId) {
        return this.repository.findByTripId(tripId).stream().map(link ->
                new LinkResponse(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
