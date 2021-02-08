package com.vocalink.crossproduct.domain.broadcasts;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;

public interface BroadcastsRepository extends CrossproductRepository {

  Page<Broadcast> findPaginated(BroadcastsSearchCriteria criteria);

  Broadcast create(String message, List<String> recipients);
}
