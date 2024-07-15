package com.vieiraork.planner.activity.service;

import com.vieiraork.planner.Helper.Helpers;
import com.vieiraork.planner.activity.entity.Activity;
import com.vieiraork.planner.activity.repository.ActivityRepository;
import com.vieiraork.planner.activity.request.ActivityRequestPayLoad;
import com.vieiraork.planner.activity.response.ActivityCreateResponse;
import com.vieiraork.planner.activity.response.ActivityDataResponse;
import com.vieiraork.planner.trip.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityCreateResponse registerActivity(ActivityRequestPayLoad payLoad, Trip trip) {
        Activity activity = new Activity(payLoad.title(),
                Helpers.transformStringToLocalDatetime(payLoad.occurs_at()), trip);

        this.activityRepository.save(activity);

        return new ActivityCreateResponse(activity.getId(), activity.getTitle(), activity.getOccursAt());
    }

    public List<ActivityDataResponse> getAllActivitiesFromTrip(UUID tripId) {
        return this.activityRepository.findAll().stream().map(activity -> new ActivityDataResponse(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
