package com.twl.miniredis.service;

import com.twl.miniredis.repository.DatabaseRepository;
import org.springframework.stereotype.Service;

/**
 * @author Tiago Wolker
 */
@Service
public class DatabaseService {

    private DatabaseRepository repository;

    public DatabaseService(DatabaseRepository repository) {
        this.repository = repository;
    }

    public String setStringValue(String key, String value) {
        repository.setStringValue(key, value);
        return this.getStringValue(key);
    }

    public String getStringValue(String key) {
        return repository.getStringValue(key);
    }

    public int del(String... keys) {
        return repository.del(keys);
    }

    public Integer dbsize() {
        return repository.dbsize();
    }
}
