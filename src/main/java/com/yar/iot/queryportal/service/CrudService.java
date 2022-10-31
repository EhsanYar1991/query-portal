package com.yar.iot.queryportal.service;


import com.yar.iot.queryportal.common.PaginationResponse;
import com.yar.iot.queryportal.exception.BusinessException;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The crud service
 *
 * @param <DOCUMENT>   The target entity
 * @param <REQUEST>  The target request
 * @param <RESPONSE> The target response
 */
@NoRepositoryBean
public interface CrudService<REQUEST, RESPONSE extends Serializable, DOCUMENT extends Persistable> {

    /**
     * Add new record
     *
     * @param request The request
     * @return {@link RESPONSE}
     */
    RESPONSE add(@NotNull REQUEST request) throws BusinessException;

    /**
     * Edit a record
     *
     * @param request The request
     * @return {@link RESPONSE}
     */
    RESPONSE edit(@NotBlank String id, @NotNull REQUEST request) throws BusinessException;

    /**
     * Delete a record by given ID
     *
     * @param id The record ID
     */
    void delete(String id) throws BusinessException;

    /**
     * Activate a record by given ID
     *
     * @param id The record ID
     */
    void activation(String id, boolean isActive) throws BusinessException;

    /**
     * Get a single record by given ID
     *
     * @param id The request
     * @return {@link RESPONSE}
     */
    RESPONSE get(@NotNull String id) throws BusinessException;

    /**
     * Get a page of records and filtered by search
     *
     * @param search   The search criteria
     * @param pageable The pageable
     * @return {@link RESPONSE}
     */
    PaginationResponse<RESPONSE> list(String search, Pageable pageable) throws BusinessException;

    /**
     * Convert the DOCUMENT to the response
     *
     * @param DOCUMENT The room DOCUMENT
     * @return {@link RESPONSE}
     */
    RESPONSE makeResponse(@NotNull DOCUMENT DOCUMENT) throws BusinessException;

    /**
     * Convert the request to the entity
     *
     * @param request The room request
     * @return {@link DOCUMENT}
     */
    DOCUMENT makeEntity(@NotNull REQUEST request) throws BusinessException;


}
