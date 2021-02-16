package com.vocalink.crossproduct.domain.report;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface ReportRepository extends CrossproductRepository {

  Page<Report> findPaginated(ReportSearchCriteria criteria);
}
