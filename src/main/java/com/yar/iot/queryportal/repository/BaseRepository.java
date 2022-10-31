package com.yar.iot.queryportal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The base repository
 * @param <T> the entity
 * */
@NoRepositoryBean
public interface BaseRepository<T> extends MongoRepository<T,String>, QuerydslPredicateExecutor<T> {
}
