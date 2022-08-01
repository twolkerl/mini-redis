package com.twl.miniredis.service;

import com.twl.miniredis.exception.BusinessException;
import com.twl.miniredis.exception.NonNumericValueException;
import com.twl.miniredis.exception.NotFoundException;
import com.twl.miniredis.repository.DatabaseRepository;
import com.twl.miniredis.service.comparator.MapValueKeyComparator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

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

    // TODO: mover mensagens para arquivo properties.

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
            Object existingKeyValue = repository.getObject(key);
            LinkedHashMap<String, Double> zset = new LinkedHashMap<>();
            if (existingKeyValue != null &&LinkedHashMap.class.equals(existingKeyValue.getClass())) {
                zset = (LinkedHashMap<String, Double>) existingKeyValue;
            }
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
            // FIXME: Refatorar para uma maneira mais performática.
            List<Map.Entry<String, Double>> list = new ArrayList<>(zset.entrySet());
            Collections.sort(list, new MapValueKeyComparator<String, Double>());

            zset.clear();
            for (Map.Entry<String, Double> entry : list) {
                zset.put(entry.getKey(), entry.getValue());
            }
            if (valuesSaved > 0) {
                repository.setStringValue(key, zset);
            }
            return valuesSaved;
        }
    }

    public Integer zcard(String key) throws BusinessException {
        return repository.zcard(key);
    }

    public Integer zrank(String key, String member) throws NotFoundException, BusinessException {
        Integer zrank = repository.zrank(key, member);
        if (zrank == null) {

            String message = "Key not found in database.";
            log.warn(message);
            throw new NotFoundException(message);
        } else if (zrank.equals(-1)) {

            String message = "(nil) Member not found in zset.";
            log.warn(message);
            throw new NotFoundException(message);
        } else {
            return zrank;
        }
    }
}
