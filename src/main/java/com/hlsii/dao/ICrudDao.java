package com.hlsii.dao;

import com.hlsii.entity.DataEntity;

public interface ICrudDao <T extends DataEntity> {
    T get(String id);
    T get(T entity);
    long getCount();
    long getCount(T entity);
    long getCount(String queryCondition);
    void delete(String id);
    void delete(T entity);
    void unDelete(T entity);
    void clearSession();
    void evict(T entity);
    void executeHQL(String hql);
}

