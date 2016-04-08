package com.mpw.config;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.EntityPathBase;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public abstract class AbstractRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(T elem){
        entityManager.persist(elem);
    }

    protected JPAQuery query(){
        return new JPAQuery(entityManager).from(getPathBase());
    }

    protected abstract EntityPathBase<T> getPathBase();
}