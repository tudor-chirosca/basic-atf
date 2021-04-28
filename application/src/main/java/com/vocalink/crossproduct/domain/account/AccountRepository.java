package com.vocalink.crossproduct.domain.account;

import com.vocalink.crossproduct.domain.CrossproductRepository;

public interface AccountRepository extends CrossproductRepository {

  Account findByPartyCode(String partyCode);
}
