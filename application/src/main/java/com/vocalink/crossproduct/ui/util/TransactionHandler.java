package com.vocalink.crossproduct.ui.util;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionHandler {

    @Transactional
    public <T> T runInTransaction(Supplier<T> supplier) {
       return supplier.get();
    }
}
