package com.twl.miniredis.controller;

import com.twl.miniredis.exception.BusinessException;
import com.twl.miniredis.exception.NotFoundException;
import com.twl.miniredis.service.DatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping
public class MiniRedisController {

    private final DatabaseService service;

    public MiniRedisController(DatabaseService service) {
        this.service = service;
    }

    @PutMapping("/SET")
    @ResponseStatus(HttpStatus.OK)
    private String set(@RequestParam String key, @RequestParam String value,
                       @RequestParam(required = false) Integer exSeconds) throws BusinessException, NotFoundException {
        return service.setKeyValue(key, value, exSeconds);
    }

    @GetMapping("/GET/{key}")
    private String getByKey(@PathVariable String key) throws BusinessException, NotFoundException {
        return service.getStringValue(key);
    }

    @DeleteMapping("/DEL/{keys}")
    private Integer delByKey(@PathVariable String[] keys) {
        return service.del(keys);
    }

    @GetMapping("/DBSIZE")
    private Integer getDbSize() {
        return service.dbsize();
    }

    @PutMapping("/INCR/{key}")
    private String incr(@PathVariable String key) throws Exception {
        return service.incr(key);
    }

    @PutMapping("/ZADD/{key}")
    private Integer zadd(@PathVariable String key, @RequestParam String... values) throws Exception {
        return service.zadd(key, values);
    }

    @GetMapping("/ZCARD/{key}")
    private Integer zcard(@PathVariable String key) throws BusinessException {
        return service.zcard(key);
    }

    @GetMapping("/ZRANK/{key}")
    private Integer zrank(@PathVariable String key, @RequestParam String member) throws NotFoundException, BusinessException {
        return service.zrank(key, member);
    }

    @GetMapping("/ZRANGE/{key}")
    private LinkedHashMap<String, Double> zrange(@PathVariable String key, @RequestParam int start, @RequestParam int stop)
            throws BusinessException, NotFoundException {

        return service.zrange(key, start, stop);
    }
}
