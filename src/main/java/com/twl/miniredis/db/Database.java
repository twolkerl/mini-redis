package com.twl.miniredis.db;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Final class that represents the in-memory database.
 *
 * @author Tiago Wolker
 */
public final class Database {

    @Getter
    private static Map<String, Object> values = new HashMap<>();

    private Database() {
        values = new HashMap<>();
    }
}
