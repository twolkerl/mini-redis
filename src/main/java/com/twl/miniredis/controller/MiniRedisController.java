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

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    private String set(@RequestParam String key, @RequestParam String value) {
        return service.setStringValue(key, value);
    }

    @GetMapping("/{key}")
    private String getByKey(@PathVariable String key) {
        return service.getStringValue(key);
    }

    @DeleteMapping("/{keys}")
    private Integer delByKey(@PathVariable String[] keys) {
        return service.del(keys);
    }

    @GetMapping("/DBSIZE")
    private Integer getDbSize() {
        return service.dbsize();
    }
}
