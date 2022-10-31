package com.yar.iot.queryportal.service.record;

import com.yar.iot.queryportal.common.PaginationResponse;
import com.yar.iot.queryportal.common.response.record.ParamStatisticsResponse;
import com.yar.iot.queryportal.common.response.record.RecordResponse;
import com.yar.iot.queryportal.domain.document.RecordDocument;
import com.yar.iot.queryportal.service.BaseService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ScriptOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 * Record service
 * */
@Service
@RequiredArgsConstructor
public class RecordService extends BaseService {

    private final MongoTemplate mongoTemplate;

    /**
     * Get parameter statistics
     *
     * @param param The parameter name
     * @return {@link ParamStatisticsResponse}
     * */
    public ParamStatisticsResponse getStatistics(String param) {
        GroupOperation groupOperation = group()
                .min(param).as("min")
                .max(param).as("max")
                .avg(param).as("average")
                .count().as("count")
                .accumulate(ScriptOperators.accumulatorBuilder()
                        .init("function() { return []; }")
                        .accumulate("function(bs, b) { return bs.concat(b); }")
                        .accumulateArgs("$thermostat")
                        .merge("function(bs1, bs2) { return bs1.concat(bs2); }")
                        .finalize("function(bs) { " +
                                " bs.sort(function(a, b) { return a - b });" +
                                " var mid = bs.length / 2; " +
                                " return mid % 1 ? bs[mid - 0.5] : (bs[mid - 1] + bs[mid]) / 2; " +
                                " }"))
                .as("median");

        Aggregation aggregation = newAggregation(groupOperation);

        AggregationResults<ParamStatisticsResponse> result = mongoTemplate
                .aggregate(aggregation, "Record", ParamStatisticsResponse.class);

        return result.getUniqueMappedResult();
    }

    /**
     * Get client records
     *
     * @param clientId The client ID
     * @return {@link PaginationResponse<RecordResponse>}
     * */
    public PaginationResponse<RecordResponse> getClientRecords(String clientId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "timeStamp");
        Query query = new Query().addCriteria(Criteria.where("clientId").is(clientId));
        long count = mongoTemplate.count(Query.of(query), RecordDocument.class);
        List<RecordDocument> recordDocuments = mongoTemplate.find(query.with(pageRequest), RecordDocument.class);
        return PaginationResponse.<RecordResponse>builder()
                .list(recordDocuments.stream().map(RecordResponse::new).collect(Collectors.toList()))
                .page(pageNumber)
                .size(pageSize)
                .totalPage(count != 0 && count / pageSize > 1 ? count / pageSize : 1)
                .totalSize(count)
                .build();
    }
}
