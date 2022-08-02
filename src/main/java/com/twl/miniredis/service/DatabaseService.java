package com.twl.miniredis.service;

import com.twl.miniredis.exception.BusinessException;
import com.twl.miniredis.exception.NonNumericValueException;
import com.twl.miniredis.exception.NotFoundException;
import com.twl.miniredis.model.dto.ExpirableValue;
import com.twl.miniredis.repository.DatabaseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Tiago Wolker
 */
@Service
@Log4j2
public class DatabaseService {

    // TODO: mover mensagens para arquivo properties.
    public static final String KEY_DOES_NOT_EXIST = "(nil) Key does not exist in database.";
    public static final String MEMBER_NOT_FOUND = "(nil) Member not found in zset.";
    public static final String COULD_NOT_FIND_RESULTS = "Could not find any results.";

    private DatabaseRepository repository;

    public DatabaseService(DatabaseRepository repository) {
        this.repository = repository;
    }


    public String setKeyValue(String key, Object value, Integer exSeconds) throws BusinessException, NotFoundException {
        repository.setKeyValue(key, value, exSeconds);
        return this.getStringValue(key);
    }

    public String getStringValue(String key) throws BusinessException, NotFoundException {
        ExpirableValue expirableValue = repository.getKey(key);
        if (expirableValue != null && expirableValue.getValue() != null) {
            return String.valueOf(expirableValue.getValue());
        } else {
            log.warn(KEY_DOES_NOT_EXIST);
            throw new NotFoundException(KEY_DOES_NOT_EXIST);
        }
    }

    public int del(String... keys) {
        return repository.del(keys);
    }

    public Integer dbsize() {
        return repository.dbsize();
    }

    public String incr(String key) throws NonNumericValueException, BusinessException {
        ExpirableValue expirableValue = repository.incr(key);
        return expirableValue == null || expirableValue.getValue() == null ? "0" : String.valueOf(expirableValue.getValue());
    }

    public Integer zadd(String key, String... scoreMembers) throws BusinessException {
        return repository.zadd(key, scoreMembers);
    }

    public Integer zcard(String key) throws BusinessException {
        return repository.zcard(key);
    }

    public Integer zrank(String key, String member) throws NotFoundException, BusinessException {
        Integer zrank = repository.zrank(key, member);
        if (zrank == null) {

            log.warn(KEY_DOES_NOT_EXIST);
            throw new NotFoundException(KEY_DOES_NOT_EXIST);
        } else if (zrank.equals(-1)) {

            log.warn(MEMBER_NOT_FOUND);
            throw new NotFoundException(MEMBER_NOT_FOUND);
        } else {
            return zrank;
        }
    }

    public LinkedHashMap<String, Double> zrange(String key, int start, int stop) throws BusinessException, NotFoundException {
        LinkedHashMap<String, Double> zrange = repository.zrange(key, start, stop);
        if (zrange.isEmpty()) {
            log.warn(COULD_NOT_FIND_RESULTS);
            throw new NotFoundException(COULD_NOT_FIND_RESULTS);
        }
        return zrange;
    }
}
