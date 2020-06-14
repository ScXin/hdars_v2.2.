//package com.hlsii.dao;//package dao;
//
//
//
//
//public class CurdDao{
//
//
//
//}
//
////
////import entity.DataEntity;
////
////@SuppressWarnings("unchecked")
////public abstract class CrudDao<T extends DataEntity> implements ICrudDao<T> {
////
////
////    protected Class<T> entityClass = getEntityClass();
////
////    @Override
////    public T get(String id) {
////
////    }
////
////    @Override
////    public T get(T entity) {
////        return (T) sessionFactory.getCurrentSession().get(entityClass, entity.getId());
////    }
////
////    @Override
////    public List<T> getAll() {
////        return sessionFactory.getCurrentSession().createQuery(queryAllString()).list();
////    }
////
////    @Override
////    public List<T> getAll(String queryCondition, String orderBy) {
////        return getAll(queryCondition, orderBy, true);
////    }
////
////    @Override
////    public List<T> getAll(String queryCondition, String orderBy, boolean excludeDeleted) {
////        String query = conditionQueryWithOrderString(queryCondition, orderBy, excludeDeleted);
////        return sessionFactory.getCurrentSession().createQuery(query).list();
////    }
////
////    @Override
////    public long getCount() {
////        return getCount("");
////    }
////
////    @Override
////    public long getCount(T entity) {
////        String queryCondition = entity.constructQueryCondition();
////        return getCount(queryCondition);
////    }
////
////    @Override
////    public long getCount(String queryCondition) {
////        Query query = sessionFactory.getCurrentSession().createQuery("select count(*) " +
////                conditionQueryString(queryCondition, true));
////        return (long)query.iterate().next();
////    }
////
////    @Override
////    public Page<T> getPage(Page<T> page, T entity) {
////        String queryCondition = entity.constructQueryCondition();
////        return getPage(page, queryCondition);
////    }
////
////    @Override
////    public Page<T> getPage(Page<T> page, String queryCondition) {
////        page.setCount(getCount(queryCondition));
////        int begin = (page.getPageNo() - 1) * page.getPageSize();
////        Query query = sessionFactory.getCurrentSession().createQuery(conditionQueryWithOrderString(
////                queryCondition, page.getOrderBy(), true));
////        page.setList((List<T>)query.setFirstResult(begin).setMaxResults(page.getPageSize()).list());
////        return page;
////    }
////
////    @Override
////    public void saveOrUpdate(T entity){
////        if (StringUtils.isEmpty(entity.getId())) {
////            entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
////            save(entity);
////        }
////        else {
////            update(entity);
////        }
////    }
////
////    @Override
////    public void delete(T entity){
////        entity.setDelFlag("1");
////        update(entity);
////    }
////
////    @Override
////    public void delete(String id){
////        T entity = (T)sessionFactory.getCurrentSession().load(entityClass, id);
////        if (entity != null) {
////            entity.setDelFlag("1");
////            update(entity);
////        }
////    }
////
////    @Override
////    public void unDelete(T entity){
////        entity.setDelFlag("");
////        update(entity);
////    }
////
////    @Override
////    public void evict(T entity){
////        sessionFactory.getCurrentSession().evict(entity);
////    }
////
////    @Override
////    public void clearSession() {
////        sessionFactory.getCurrentSession();
////    }
////
////    @Override
////    public void executeHQL(String hql) {
////        Query query = sessionFactory.getCurrentSession().createQuery(hql);
////        query.executeUpdate();
////    }
////
////    protected String getEntityName() {
////        String entityName = entityClass.getSimpleName();
////        Entity entity = entityClass.getAnnotation(Entity.class);
////        if(entity.name() != null && !"".equals(entity.name()))
////            entityName = entity.name();
////        return entityName;
////    }
////
////    private void save(T entity) {
////        sessionFactory.getCurrentSession().save(entity);
////    }
////
////    private void update(T entity) {
////        sessionFactory.getCurrentSession().merge(entity);
////    }
////
////    private String queryAllString() {
////        return conditionQueryString(null, true);
////    }
////
////    private String conditionQueryString(String condition, boolean excludeDelRecord) {
////        StringBuilder sb = new StringBuilder("from " + getEntityName() + " " +
////                DataEntity.DEFAULT_ALIAS + " ");
////        StringBuilder condSb = new StringBuilder();
////        if (StringUtils.isNotEmpty(condition)) {
////            condSb.append(condition);
////        }
////        if (excludeDelRecord) {
////            if (condSb.length() > 0) {
////                condSb.append(" and ");
////            }
////            condSb.append("(" + DataEntity.DEFAULT_ALIAS + "." + "delFlag is null or trim(" +
////                    DataEntity.DEFAULT_ALIAS + "." + "delFlag) = '')");
////        }
////        if (condSb.length() > 0) {
////            sb.append(" where ");
////            sb.append(condSb);
////        }
////        return sb.toString();
////    }
////
////    private String conditionQueryWithOrderString(String condition, String orderBy, boolean excludeDeleted) {
////        String queryStr = conditionQueryString(condition, excludeDeleted);
////        if (StringUtils.isNotEmpty(orderBy)) {
////            queryStr += " order by " + orderBy;
////        }
////        return queryStr;
////    }
////
////    private Class<T> getEntityClass() {
////        Type parentType = this.getClass().getGenericSuperclass();
////        if(parentType instanceof ParameterizedType) {
////            ParameterizedType ptype = (ParameterizedType) parentType;
////            return (Class<T>) ptype.getActualTypeArguments()[0];
////        }
////        return (Class<T>)parentType;
////    }
////}