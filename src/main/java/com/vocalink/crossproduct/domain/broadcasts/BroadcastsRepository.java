package com.vocalink.crossproduct.domain.broadcasts;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface BroadcastsRepository extends CrossproductRepository {

  Page<Broadcast> findPaginated(BroadcastsSearchCriteria criteria);
}
