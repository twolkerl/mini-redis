package com.twl.miniredis.controller;

import com.twl.miniredis.exception.BusinessException;
import com.twl.miniredis.service.DatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class MiniRedisController {

    private final DatabaseService service;

    public MiniRedisController(DatabaseService service) {
        this.service = service;
    }

    @PutMapping("/SET")
    @ResponseStatus(HttpStatus.OK)
    private String set(@RequestParam String key, @RequestParam String value) throws BusinessException {
        return service.setStringValue(key, value);
    }

    @GetMapping("/GET/{key}")
    private String getByKey(@PathVariable String key) throws BusinessException {
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
}
