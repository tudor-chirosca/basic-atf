package com.vocalink.crossproduct.domain.audit;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.util.List;
import java.util.UUID;

public interface UserActivityRepository extends CrossproductRepository {

  List<UserActivity> getActivitiesByIds(List<UUID> id);

  UserActivity getActivitiesById(UUID id);

  List<String> getEventTypes();
}
