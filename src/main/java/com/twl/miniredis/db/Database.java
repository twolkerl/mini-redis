package com.twl.miniredis.db;

import com.twl.miniredis.model.dto.ScoreMember;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Final class that represents the in-memory database.
 *
 * @author Tiago Wolker
 */
public final class Database {

    // TODO - Verificar possibilidade de utilizar apenas 1 map. Porém será necessário alterar as operações o repository.
    @Getter
    private static Map<String, String> stringValues = new HashMap<>();
    @Getter
    private static LinkedHashMap<String, List<ScoreMember>> zset = new LinkedHashMap<>();

    private Database() {
        stringValues = new HashMap<>();
        zset = new LinkedHashMap<>();
    }
}
