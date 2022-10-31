package com.yar.iot.queryportal.controller.record;

import com.yar.iot.queryportal.common.PaginationResponse;
import com.yar.iot.queryportal.common.response.record.ParamStatisticsResponse;
import com.yar.iot.queryportal.common.response.record.RecordResponse;
import com.yar.iot.queryportal.controller.BaseController;
import com.yar.iot.queryportal.service.record.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Record controller
 *
 * @see com.yar.iot.queryportal.controller.BaseController
 * */
@RestController
@RequestMapping(RecordController.RECORD_URL_CONTEXT_PATH)
@RequiredArgsConstructor
@Api(value = "Record APIs")
public class RecordController implements BaseController {

    public static final String RECORD_URL_CONTEXT_PATH = "/record";
    public static final String QUERY_URL_CONTEXT_PATH = "/query";
    public static final String GET_CLIENT_RECORDS_URL_CONTEXT_PATH = "/{clientID}/records";

    private final RecordService recordService;


    /**
     * Get parameter statistics
     *
     * @param param The parameter name
     * @return {@link ResponseEntity}
     * */
    @PreAuthorize(ALL_AUTHORITY)
    @GetMapping(QUERY_URL_CONTEXT_PATH)
    @ApiOperation(value = "Get parameter statistics")
    public ResponseEntity<ParamStatisticsResponse> query(
            @NotBlank(message = "parameter must be determined.") @RequestParam(name = "parameter") String param) {
        ParamStatisticsResponse response = recordService.getStatistics(param);
        return okResponse(response);
    }

    /**
     * Get client records
     *
     * @param clientId The client ID
     * @return {@link ResponseEntity}
     * */
    @PreAuthorize(ALL_AUTHORITY)
    @GetMapping(GET_CLIENT_RECORDS_URL_CONTEXT_PATH)
    @ApiOperation(value = "Get client records")
    public ResponseEntity<PaginationResponse<RecordResponse>> getClientRecords(
            @NotBlank(message = "clientID must be determined.") @PathVariable(name = "clientID") String clientId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PaginationResponse<RecordResponse> response = recordService.getClientRecords(clientId, page, size);
        return okResponse(response);
    }

}
