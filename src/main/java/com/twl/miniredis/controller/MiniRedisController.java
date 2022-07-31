package com.twl.miniredis.controller;

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
    private String set(@RequestParam String key, @RequestParam String value) {
        return service.setStringValue(key, value);
    }

    @GetMapping("/GET/{key}")
    private String getByKey(@PathVariable String key) {
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
}