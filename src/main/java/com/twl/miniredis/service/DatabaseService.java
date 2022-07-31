package com.twl.miniredis.service;

import com.twl.miniredis.exception.BusinessException;
import com.twl.miniredis.exception.NonNumericValueException;
import com.twl.miniredis.repository.DatabaseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

/**
 * @author Tiago Wolker
 */
@Service
@Log4j2
public class DatabaseService {

    private DatabaseRepository repository;

    public DatabaseService(DatabaseRepository repository) {
        this.repository = repository;
    }

    public String setStringValue(String key, Object value) throws BusinessException {
        repository.setStringValue(key, value);
        return this.getStringValue(key);
    }

    public String getStringValue(String key) throws BusinessException {
        return repository.getStringValue(key);
    }

    public int del(String... keys) {
        return repository.del(keys);
    }

    public Integer dbsize() {
        return repository.dbsize();
    }

    public String incr(String key) throws NonNumericValueException, BusinessException {
        return repository.incr(key);
    }

    public Integer zadd(String key, String... scoreMembers) throws BusinessException {
        if (scoreMembers == null || scoreMembers.length < 2) {
            String message = "Invalid number of arguments for method ZADD key score1 member1 score2 member2 ... .";
            log.error(message);
            throw new BusinessException(message);
        } else {
//            TreeMap<String, Double> zset = repository.
            // TODO - obter lista existente para substituir valores das chaves caso já existam
            TreeMap<String, Double> zset = new TreeMap<>();
            int valuesSaved = 0;
            Double score = null;
            for (int i = 0; i < scoreMembers.length; i++) {
                if ((i+1) % 2 != 0) {
                    try {
                        score = Double.parseDouble(scoreMembers[i]);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid score provided. Score must be a number.");
                        score = null;
                    }
                } else {
                    if (score != null) {
                        zset.put(scoreMembers[i], score);
                        valuesSaved++;
                        score = null;
                    } else {
                        log.warn("No valid score provided for member \"{}\". Member will not be added.", scoreMembers[i]);
                    }
                }
            }
            // TODO - ordenar lista
            if (valuesSaved > 0) {
                repository.setStringValue(key, zset);
            }
            return valuesSaved;
        }
    }
}
