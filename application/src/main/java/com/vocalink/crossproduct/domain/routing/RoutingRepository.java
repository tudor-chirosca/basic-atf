package com.vocalink.crossproduct.domain.routing;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;

public interface RoutingRepository extends CrossproductRepository {

  Page<RoutingRecord> findPaginated(RoutingRecordCriteria criteria);

  List<RoutingRecord> findAllByBic(String bic);
}
