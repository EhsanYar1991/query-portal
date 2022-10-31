package com.yar.iot.queryportal.config;

import com.yar.iot.queryportal.domain.BaseDocument;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;


public class CustomAuditingEventListener implements ApplicationListener<BeforeConvertEvent<Object>> {
    @Override
    public void onApplicationEvent(BeforeConvertEvent<Object> event) {
        Optional.of(event)
                .map(MongoMappingEvent::getSource)
                .filter(Persistable.class::isInstance)
                .map(BaseDocument.class::cast)
                .ifPresent(parentDocument -> {
                    final String user = "testUser";
                    LocalDateTime now = LocalDateTime.now();
                    parentDocument.setModifiedTime(now);
                    parentDocument.setModifiedBy(user);
                    if (parentDocument.getCreatedTime() == null) {
                        parentDocument.setCreatedTime(now);
                        parentDocument.setCreatedBy(user);
                    }
                });
    }
}
