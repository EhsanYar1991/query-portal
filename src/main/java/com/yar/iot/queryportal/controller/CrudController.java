package com.yar.iot.queryportal.controller;

import com.yar.iot.queryportal.service.CrudService;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The crud controller
 *
 * @param <REQUEST>  The request
 * @param <RESPONSE> The response
 */
@RequiredArgsConstructor
public class CrudController<REQUEST, RESPONSE extends Serializable> implements BaseController {

    private static final String ACTIVATION_URL_CONTEXT = "/activation";
    private static final String ID_PARAM = "id";
    private static final String ACTIVE_PARAM = "active";
    protected static final String PAGE_NUMBER = "pageNumber";
    protected static final String PAGE_SIZE = "pageSize";
    private static final String SEARCH_PARAM = "search";

    /**
     * The target service bean
     */
    private final CrudService<REQUEST, RESPONSE, ?> crudService;

    /**
     * The constructor for wiring the target service bean
     */
    public CrudService<REQUEST, RESPONSE, ?> getCrudService() {
        return crudService;
    }

    /**
     * API for adding new record
     *
     * @param request The request
     * @return {@link ResponseEntity<RESPONSE>} the response
     */
    @PreAuthorize(ALL_AUTHORITY)
    @PostMapping
    @ApiOperation(value = "Add new record")
    public ResponseEntity<RESPONSE> add(@Valid REQUEST request) {
        RESPONSE add = getCrudService().add(request);
        return responseBuilder(HttpStatus.CREATED, add);
    }

    /**
     * API for editing a record
     *
     * @param request The request
     * @return {@link ResponseEntity<RESPONSE>} the response
     */
    @PreAuthorize(ALL_AUTHORITY)
    @PutMapping
    @ApiOperation(value = "Edit a record")
    public ResponseEntity<RESPONSE> edit(@NotBlank(message = "id must be determined") @RequestParam(ID_PARAM) String id, @Valid REQUEST request) {
        RESPONSE edit = getCrudService().edit(id, request);
        return okResponse(edit);
    }

    /**
     * API for a record deletion
     *
     * @param id The record ID
     * @return {@link ResponseEntity<RESPONSE>} the response
     */
    @DeleteMapping
    @PreAuthorize(ADMIN_AUTHORITY)
    @ApiOperation(value = "Delete a record")
    public void delete(@RequestParam(ID_PARAM)
    @NotNull(message = "id must be determined.") String id) {
        getCrudService().delete(id);
    }

    @PreAuthorize(ADMIN_AUTHORITY)
    @PutMapping(ACTIVATION_URL_CONTEXT)
    public ResponseEntity<?> activation(@RequestParam(ID_PARAM) @NotNull(message = "id must be determined.") String id,
            @RequestParam(ACTIVE_PARAM) @NotBlank(message = "active must be determined.") Boolean active) {
        getCrudService().activation(id, active);
        return okResponse();
    }

    /**
     * API for getting a single record of getting a list of records
     *
     * @param id The record ID
     * @return {@link ResponseEntity<RESPONSE>} the response
     */
    @PreAuthorize(ALL_AUTHORITY)
    @GetMapping("")
    @ApiOperation(value = "Get a single record by given id or a pagination based on search filter")
    public ResponseEntity<?> getOrList(
            @RequestParam(value = ID_PARAM, required = false) String id,
            @RequestParam(value = SEARCH_PARAM, required = false, defaultValue = "") String search,
            @PageableDefault Pageable pageable) {
        if (id != null) {
            RESPONSE result = getCrudService().get(id);
            return okResponse(result);
        } else {
            return okResponse(
                    getCrudService().list(search, pageable)
            );
        }
    }

}
