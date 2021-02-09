package com.vocalink.crossproduct.domain.routing;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface RoutingRepository extends CrossproductRepository {

  Page<RoutingRecord> findPaginated(RoutingRecordCriteria criteria);
}
