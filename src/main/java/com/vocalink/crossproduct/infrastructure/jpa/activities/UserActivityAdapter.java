package com.vocalink.crossproduct.infrastructure.jpa.activities;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.audit.UserActivity;
import com.vocalink.crossproduct.domain.audit.UserActivityRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserActivityAdapter implements UserActivityRepository {

  public static final String ACTIVITY_ID = "id";

  @PersistenceContext
  private final EntityManager entityManager;

  @Override
  public List<UserActivity> getActivitiesByIds(List<UUID> ids) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserActivityJpa> query = cb.createQuery(UserActivityJpa.class);
    Root<UserActivityJpa> activities = query.from(UserActivityJpa.class);

    query.select(activities)
        .where(activities.get(ACTIVITY_ID).in(ids));

    return entityManager.createQuery(query)
        .getResultList()
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
