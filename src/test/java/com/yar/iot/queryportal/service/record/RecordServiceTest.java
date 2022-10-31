package com.yar.iot.queryportal.service.record;

import com.yar.iot.queryportal.common.PaginationResponse;
import com.yar.iot.queryportal.common.response.record.ParamStatisticsResponse;
import com.yar.iot.queryportal.common.response.record.RecordResponse;
import com.yar.iot.queryportal.domain.document.RecordDocument;
import com.yar.iot.queryportal.tests.AbstractUnitTest;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.BDDAssertions.then;

class RecordServiceTest extends AbstractUnitTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    private RecordService recordService;

    @BeforeEach
    public void setUp() {
        this.recordService = new RecordService(mongoTemplate);
    }

    @Disabled(value = "There is a bug on the de.flapdoodle.embed to fetch the correct result.")
    @Test
    void getStatistics() {
        // given
        final String paramName = "parameter";
        final String clientIdKey = "clientId";
        final String collectionName = "RecordDocument";
        RecordDocument record1 = new RecordDocument(
                Map.of(clientIdKey , "clientId1", paramName, 10)
        );
        mongoTemplate.save(record1, collectionName);
        RecordDocument record2 = new RecordDocument(
                Map.of(clientIdKey , "clientId2", paramName, 20)
        );
        mongoTemplate.save(record2, collectionName);
        RecordDocument record3 = new RecordDocument(
                Map.of(clientIdKey , "clientId3", paramName, 30)
        );
        mongoTemplate.save(record3, collectionName);

        // when
        ParamStatisticsResponse statistics = recordService.getStatistics(paramName);

        // then
        then(statistics.getMin())
                .as("Validate the min value is correct.")
                .isEqualTo(10);
        then(statistics.getMax())
                .as("Validate the max value is correct.")
                .isEqualTo(30);

    }

    @Test
    void getClientRecords() {
        // given
        final String clientIdKey = "clientId";
        final String clientIdValue = "testClientId";
        final String collectionName = "RecordDocument";
        RecordDocument record = new RecordDocument(
                Map.of(clientIdKey , clientIdValue, "param1", 10, "param2", 20)
        );
        mongoTemplate.save(record, collectionName);

        // when
        PaginationResponse<RecordResponse> response = recordService.getClientRecords(clientIdValue, 0, 10);

        // then
        then(response.getPage())
                .as("Validate the page value is correct.")
                .isEqualTo(0);
        then(response.getSize())
                .as("Validate the size value is correct.")
                .isEqualTo(10);
        then(response.getTotalPage())
                .as("Validate the total page value is correct.")
                .isEqualTo(1);
        then(response.getTotalSize())
                .as("Validate the total size value is correct.")
                .isEqualTo(1);
        then(response.getList())
                .as("Validate the total size value is correct.")
                .hasSize(1);

    }
}