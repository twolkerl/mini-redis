package com.twl.miniredis.db;

import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Final class that represents the in-memory database.
 *
 * @author Tiago Wolker
 */
public final class Database {

    @Getter
    private static ConcurrentMap<String, Object> values = new ConcurrentHashMap<>();

    private Database() {
        values = new ConcurrentHashMap<>();
    }
}
