package com.vocalink.crossproduct.infrastructure.jpa.audit;

import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPageRequest implements Pageable, Serializable {

  public static final String DEFAULT_SORTING_ORDER = "timestamp";
  private final int limit;
  private final int offset;
  private final Sort sort;

  public OffsetBasedPageRequest(int limit, int offset) {
    this.limit = limit;
    this.offset = offset;
    this.sort = Sort.by(DEFAULT_SORTING_ORDER).descending();
  }

  public OffsetBasedPageRequest(int limit, int offset, Sort sort) {
    this.limit = limit;
    this.offset = offset;
    this.sort = sort;
  }

  @Override
  public int getPageNumber() {
    return offset / limit;
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    return new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() + getPageSize()));
  }

  public Pageable previous() {
    return hasPrevious() ?
        new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() - getPageSize())) : this;
  }

  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  @Override
  public Pageable first() {
    return new OffsetBasedPageRequest(getPageSize(), 0);
  }

  @Override
  public boolean hasPrevious() {
    return offset > limit;
  }
}
