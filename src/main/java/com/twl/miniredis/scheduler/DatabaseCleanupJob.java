package com.twl.miniredis.scheduler;

import com.twl.miniredis.db.Database;
import com.twl.miniredis.model.dto.ExpirableValue;
import com.twl.miniredis.service.DatabaseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;

@Log4j2
@Component
public class DatabaseCleanupJob {

    private DatabaseService service;

    public DatabaseCleanupJob(DatabaseService service) {
        this.service = service;
    }

    @Scheduled(cron = "*/1 * * * * *")
    public void reportCurrentTime() {
        ConcurrentMap<String, Object> values = Database.getValues();
        values.forEach((key, value) -> {
            if (ExpirableValue.class.equals(value.getClass())) {
                LocalDateTime expireTime = ((ExpirableValue) value).getExpireTime();
                if (expireTime != null && (expireTime.isBefore(LocalDateTime.now()) || expireTime.isEqual(LocalDateTime.now()))) {
                    service.del(key);
                    log.info("Key \"{}\" removed from database.", key);
                }
            }
        });
    }
}
