/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.shared.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Interceptor;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;



@Component
public abstract class AbstractRepository<T, ID extends Serializable> extends AbstractJdbcRepository implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(AbstractRepository.class);

    private static final long serialVersionUID = 1L;
    protected static final int BATCH_SIZE = 1000;
    private static final String SELECT_FROM="select o from ";
    private static final String FIELD_VALUE="fieldValue";
    private final Class<T> entityClass;
    
    private final JPAQueryFactory jpaQueryFactory;
    
    @jakarta.persistence.PersistenceContext(type = PersistenceContextType.TRANSACTION,
            synchronization = SynchronizationType.SYNCHRONIZED)
    private EntityManager em;


    @Autowired
    private TransactionTemplate txTemplate;

    @SuppressWarnings("unchecked")
	protected AbstractRepository() {
        this.entityClass = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        this.jpaQueryFactory=new JPAQueryFactory(getEntityManager());
    }
    
    
    protected Class getEntityClass() {
        return entityClass;
    }
    
    protected EntityManager getEntityManager() {
        return em;
    }
    
    protected JPAQueryFactory getJpaQueryFactory() {
    	return jpaQueryFactory;
    }
    
    


    protected TransactionTemplate getTransactionTemplate(){return txTemplate;}


    public T create(T entity) {

        // checking for constraint violations
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                log.error("{}.{} {}",cv.getRootBeanClass().getName(),cv.getPropertyPath(),cv.getMessage());
            }
        } else {
            getEntityManager().persist(entity);
            return entity;
        }
        return null;
    }

    public T edit(T entity) {
        // checking for constraint violations
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                log.error("{}.{} {}",cv.getRootBeanClass().getName(),cv.getPropertyPath(),cv.getMessage());
            }
        } else {
            return getEntityManager().merge(entity);
        }

        return null;
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(ID id) {
        return (T) getEntityManager().find(getEntityClass(), id);
    }

    //////////////////////////////////////solon////////////////////////////////////////////////////////////////////////////////////////////////////
    public Optional<T> findOpt(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return Optional.ofNullable((T) getEntityManager().find(getEntityClass(), id));
    }

    public Optional<T> findReferenceOpt(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return Optional.ofNullable((T) getEntityManager().getReference(getEntityClass(), id));
    }

    public Optional<T> findViaSession(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return getSession().byId(getEntityClass()).loadOptional(id);
    }

    public Optional<T> loadViaSession(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return Optional.ofNullable((T)getSession().load(getEntityClass(), id));
    }

    public List<T> fetchIn(List<ID> ids) {
        MultiIdentifierLoadAccess<T> multi = getSession().byMultipleIds(getEntityClass());
        List<T> resultList = multi.multiLoad(ids);
        return resultList;
    }

    public void delete(ID id) {
        Object ref = getEntityManager().getReference(getEntityClass(), id);
        getEntityManager().remove(ref);
    }

    // helper methods
    public boolean isManaged(Class<T> entityClass) {
        return getEntityManager().contains(entityClass);
    }

    public void clearContext() {
        getEntityManager().clear();
    }

    public void flushContext() {
        getEntityManager().flush();
    }

    public EntityEntry getEntityEntry(Class<T> entityClass) {
        return getPersistenceContext().getEntry(entityClass);
    }

    public Object[] getLoadedState(Class<T> entityClass) {
        return getEntityEntry(entityClass).getLoadedState();
    }

    // if the entity is detached the save() spring method(persist JPA method) will call a select statement first.
    // This avoids that.
    public T updateDetachedEntity(T entity) {
        Session session = getSession();
        session.merge(entity);
        return entity;
    }

    public void setInterceptor(Interceptor interceptor) {
        Session session = getSession();
        session.sessionWithOptions().interceptor(interceptor);
    }

    public Session getSession() {
        return getEntityManager().unwrap(Session.class);
    }

    // inspect the Persistence Context,with  the following helper method    
    private PersistenceContext getPersistenceContext() {
        SharedSessionContractImplementor sharedSession = getEntityManager().unwrap(
                SharedSessionContractImplementor.class
        );
        return sharedSession.getPersistenceContext();
    }

    @SuppressWarnings("unused")
    private Object constructObjectFromString(String value) {
        Object result = null;
        try {
            result = getEntityClass().getDeclaredMethod("valueOf", new Class[]{String.class}).invoke(null, value);
        } catch (Exception ex) {
    		log.error("error.converting.string", ex);
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int countAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(getEntityClass());
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public ArrayList<StatementParameter> generateParams(T entity) throws Exception {
        ArrayList<StatementParameter> params = RepositoryUtils.getDirty(entity);
        return params;
    }

    public List<Predicate> generatePredicates(CriteriaBuilder cb, Root<T> from, List<StatementParameter> params) throws Exception {
        List<Predicate> predicates = new ArrayList<>();
        for (StatementParameter param : params) {
            Predicate predicate;
            if (param.getType().equals(String.class)) {
            	predicate=setPredicateForString(cb,from,param);
            } else if (param.getType().equals(Date.class)) {
            	predicate=setPredicateForDate(cb,from,param);
            } else if (param.getType().equals(Integer.class)) {
            	predicate=setPredicateForInteger(cb,from,param);
            } else if (param.getType().equals(BigInteger.class)) {
            	predicate=setPredicateForBigInteger(cb,from,param);
            } else if (param.getType().equals(BigDecimal.class)) {
            	predicate=setPredicateForBigDecimal(cb,from,param);
            } else if (param.getType().equals(Long.class)) {
            	predicate=setPredicateForLong(cb,from,param);
            } else if (param.getType().equals(Short.class)) {
            	predicate=setPredicateForShort(cb,from,param);
            } else {
                predicate = cb.equal(getExpression(from, param.getColumn()), param.getValue());
            }
            predicates.add(predicate);
        }
        return predicates;
    }
    
    private Predicate setPredicateForString(CriteriaBuilder cb,Root<T> from,StatementParameter param) {
    	Expression<String> expr = getExpression(from, param.getColumn());
    	Predicate predicate;
        if ("<".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThan(expr, (String) param.getValue());
        } else if ("<=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThanOrEqualTo(expr, (String) param.getValue());
        } else if (">".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThan(expr, (String) param.getValue());
        } else if (">=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThanOrEqualTo(expr, (String) param.getValue());
        } else if ("LIKE".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.like(expr, "%" + param.getValue() + "%");
        } else {
            predicate = cb.equal(expr, param.getValue());
        }
        return predicate;
    }
    
    private Predicate setPredicateForDate(CriteriaBuilder cb,Root<T> from,StatementParameter param) {
    	Predicate predicate;
    	Expression<Date> expr = getExpression(from, param.getColumn());
        if ("<".equalsIgnoreCase(param.getOperator())) {
             predicate = cb.lessThan(expr, (Date) param.getValue());
        } else if ("<=".equalsIgnoreCase(param.getOperator())) {
             predicate = cb.lessThanOrEqualTo(expr, (Date) param.getValue());
        } else if (">".equalsIgnoreCase(param.getOperator())) {
             predicate = cb.greaterThan(expr, (Date) param.getValue());
        } else if (">=".equalsIgnoreCase(param.getOperator())) {
             predicate = cb.greaterThanOrEqualTo(expr, (Date) param.getValue());
        } else {
             predicate = cb.equal(expr, param.getValue());
        }
        return predicate;
    }
    
    private Predicate setPredicateForInteger(CriteriaBuilder cb,Root<T> from,StatementParameter param) {
    	Predicate predicate;
    	Expression<Integer> expr = getExpression(from, param.getColumn());
        if ("<".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThan(expr, (Integer) param.getValue());
        } else if ("<=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThanOrEqualTo(expr, (Integer) param.getValue());
        } else if (">".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThan(expr, (Integer) param.getValue());
        } else if (">=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThanOrEqualTo(expr, (Integer) param.getValue());
        } else {
            predicate = cb.equal(expr, param.getValue());
        }
        return predicate;
    }
    
    private Predicate setPredicateForLong(CriteriaBuilder cb,Root<T> from,StatementParameter param) {
    	Predicate predicate;
    	Expression<Long> expr = getExpression(from, param.getColumn());
        if ("<".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThan(expr, (Long) param.getValue());
        } else if ("<=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThanOrEqualTo(expr, (Long) param.getValue());
        } else if (">".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThan(expr, (Long) param.getValue());
        } else if (">=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThanOrEqualTo(expr, (Long) param.getValue());
        } else {
            predicate = cb.equal(expr, param.getValue());
        }
        return predicate;
    }
    
    private Predicate setPredicateForShort(CriteriaBuilder cb,Root<T> from,StatementParameter param) {
    	Predicate predicate;
    	Expression<Short> expr = getExpression(from, param.getColumn());
        if ("<".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThan(expr, (Short) param.getValue());
        } else if ("<=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThanOrEqualTo(expr, (Short) param.getValue());
        } else if (">".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThan(expr, (Short) param.getValue());
        } else if (">=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThanOrEqualTo(expr, (Short) param.getValue());
        } else {
            predicate = cb.equal(expr, param.getValue());
        }
        return predicate;
    }
    
    private Predicate setPredicateForBigInteger(CriteriaBuilder cb,Root<T> from,StatementParameter param) {
    	Predicate predicate;
    	Expression<BigInteger> expr = getExpression(from, param.getColumn());
        if ("<".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThan(expr, (BigInteger) param.getValue());
        } else if ("<=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThanOrEqualTo(expr, (BigInteger) param.getValue());
        } else if (">".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThan(expr, (BigInteger) param.getValue());
        } else if (">=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThanOrEqualTo(expr, (BigInteger) param.getValue());
        } else {
            predicate = cb.equal(expr, param.getValue());
        }
        return predicate;
    }
    
    private Predicate setPredicateForBigDecimal(CriteriaBuilder cb,Root<T> from,StatementParameter param) {
    	Predicate predicate;
    	Expression<BigDecimal> expr = getExpression(from, param.getColumn());
        if ("<".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThan(expr, (BigDecimal) param.getValue());
        } else if ("<=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.lessThanOrEqualTo(expr, (BigDecimal) param.getValue());
        } else if (">".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThan(expr, (BigDecimal) param.getValue());
        } else if (">=".equalsIgnoreCase(param.getOperator())) {
            predicate = cb.greaterThanOrEqualTo(expr, (BigDecimal) param.getValue());
        } else {
            predicate = cb.equal(expr, param.getValue());
        }
        return predicate;
    }

    /**
     * <A name="buildQuery"> Creates a Query based on the values of the user
     * input form.The user may or may not have filled a value for each form
     * field and accordingly the query will be different.<br> This is typical of
     * a form-based query.To account for all possible combinations of field
     * values to build a String-based JPQL can be a daunting exercise. This
     * method demonstrates how such dynamic, conditional be alternatively
     * developed using {@link CriteriaQuery} introduced in JPA version 2.0. <br>
     */
    public Long count(T entity) throws Exception {
        // builder generates the Criteria Query as well as all the expressions
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        // The query declares what type of result it will produce 
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        // Which type will be searched
        Root<T> from = q.from(getEntityClass());
        // of course, the projection term must match the result type declared earlier
        q.select(cb.count(from));
        // Builds the predicates conditionally for the filled-in input fields 
        ArrayList<StatementParameter> params = generateParams(entity);
        List<Predicate> predicates = generatePredicates(cb, from, params);

        // Sets the evaluation criteria     
        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(new Predicate[predicates.size()]));
        } else if (!isReturnAllAllowed()) {
            return Long.valueOf(0);
        }
        return getEntityManager().createQuery(q).getSingleResult();
    }

    public boolean isReturnAllAllowed() {
        return true;
    }

    public Expression getExpression(Root<T> from, String propertyname) {
        Expression retValue;
        if (!propertyname.contains(".")) {
            retValue = from.get(propertyname);
        } else {
            String[] fields = StringUtils.split(propertyname, ".");
            Path path = from.get(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                path = path.get(fields[i]);
            }
            retValue = path;
        }
        return retValue;
    }

    public List<T> findAll(T entity, String sortField, boolean asc) throws Exception {
        return findAllRange(entity, -1, -1, sortField, asc);
    }

    public List<T> findAllRange(T entity, int first, int pageSize, String sortField, boolean asc) throws Exception {
        // builder generates the Criteria Query as well as all the expressions
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        // The query declares what type of result it will produce 
        CriteriaQuery<T> q = cb.createQuery(getEntityClass());
        // Which type will be searched
        Root<T> from = q.from(getEntityClass());
        // of course, the projection term must match the result type declared earlier
        q.select(from);
        // Builds the predicates conditionally for the filled-in input fields 
        ArrayList<StatementParameter> params = generateParams(entity);
        List<Predicate> predicates = generatePredicates(cb, from, params);
        // Sets the evaluation criteria     
        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(new Predicate[predicates.size()]));
        } else if (!isReturnAllAllowed()) {
            return new ArrayList<>();
        }
        if (sortField != null) {
            if (asc) {
                q.orderBy(cb.asc(getExpression(from, sortField)));
            } else {
                q.orderBy(cb.desc(getExpression(from, sortField)));
            }
        }
        Query query = getEntityManager().createQuery(q);
        if ((pageSize > -1) && (first > -1)) {
            query.setMaxResults(pageSize);
            query.setFirstResult(first);
        }
        return query.getResultList();
    }

    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(getEntityClass()));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByField(String fieldName, String fieldValue, Class<?> fieldType) {
        String fieldStr = fieldName;
        if (String.class.equals(fieldType)) {
            fieldStr = "lower(o." + fieldName + ")";
        }
        String sQuery = SELECT_FROM + getEntityClass().getSimpleName()
                + " o where " + fieldStr + " = :"+FIELD_VALUE;
        Query hsql = getEntityManager().createQuery(sQuery);
        if (Long.class.equals(fieldType)) {
            hsql.setParameter(FIELD_VALUE, Long.valueOf(fieldValue));
        } else if (Integer.class.equals(fieldType)) {
            hsql.setParameter(FIELD_VALUE, Integer.valueOf(fieldValue));
        } else {
            hsql.setParameter(FIELD_VALUE, fieldValue != null ? fieldValue.trim().toLowerCase()
                    : null);
        }
        List<T> list = hsql.getResultList();
        return list;
    }

    /**
     * This method to find a record from a table passing a list of fields that
     * compose a unique id
     *
     * @param fieldValuePairList The mapping with field names and values
     * @param fieldTypePairsList The mapping with the field names and types
     * @return The record that matches the criteria
     */
    @SuppressWarnings("unchecked")
    public T findByUniqueKey(Map<String, String> fieldValuePairList, Map<String, Class<?>> fieldTypePairsList) {
        T result = null;
        try {
            String sQuery = SELECT_FROM + getEntityClass().getSimpleName() + " o where ";
            Iterator<Map.Entry<String, String>> fields = fieldValuePairList.entrySet().iterator();

            while (fields.hasNext()) {
                Map.Entry<String, String> fieldValuePair = fields.next();
                sQuery += fieldValuePair.getKey() + " = :" + fieldValuePair.getKey();
                sQuery += " and ";
            }
            sQuery = sQuery.substring(0, sQuery.lastIndexOf(" and "));
            Query hsql = getEntityManager().createQuery(sQuery);
            for (Map.Entry<String, Class<?>> typePair : fieldTypePairsList.entrySet()) {
                String val = fieldValuePairList.get(typePair.getKey());

                if (Long.class.equals(typePair.getValue())) {
                    hsql.setParameter(typePair.getKey(), Long.valueOf(val));
                    continue;
                }
                if (Integer.class.equals(typePair.getValue())) {
                    hsql.setParameter(typePair.getKey(), Integer.valueOf(val));
                    continue;
                }

                hsql.setParameter(typePair.getKey(), val != null ? val.trim().toLowerCase() : null);
            }
            List<T> list = hsql.getResultList();
            result = (list != null && !list.isEmpty()) ? list.get(0) : null;
        } catch (Exception e) {
        	log.error("error");
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<T> findByLikeField(String fieldName, String fieldValue) {
        String sQuery = SELECT_FROM + getEntityClass().getSimpleName()
                + " o where o." + fieldName + " like :"+FIELD_VALUE;
        Query hsql = getEntityManager().createQuery(sQuery);
        hsql.setParameter(FIELD_VALUE, fieldValue != null
                ? fieldValue.trim().toLowerCase() : null);
        List<T> resultList = hsql.getResultList();
        return resultList;
    }


    public List<T> bulkCreate(List<T> entities) {
        final List<T> savedEntities = new ArrayList<>(entities.size());
        int i = 0;
        for (T t : entities) {
            getEntityManager().persist(t);
            savedEntities.add(t);
            i++;
            if (i % BATCH_SIZE == 0) {
                // Flush a batch of inserts and release memory.
                getEntityManager().flush();
                getEntityManager().clear();
            }
        }
        return savedEntities;
    }


    public List<T> bulkUpdate(List<T> entities) {
        final List<T> updateEntities = new ArrayList<>(entities.size());
        int i = 0;
        for (T t : entities) {
            getEntityManager().merge(t);
            updateEntities.add(t);
            i++;
            if (i % BATCH_SIZE == 0) {
                // Flush a batch of inserts and release memory.
                getEntityManager().flush();
                getEntityManager().clear();
            }
        }
        return updateEntities;
    }
    
    public BigInteger generateIdFromSequencer(String sequencerName) {
		String sqlQuery = "SELECT nextval('" + sequencerName + "')";
		Query hsql = getEntityManager().createNativeQuery(sqlQuery);
		BigInteger fid = (BigInteger) hsql.getSingleResult();
		return fid;

	}


	@SuppressWarnings("rawtypes")
	public BigInteger[] generateIdsFromSequencer(String sequencerName, int n) {
		BigInteger[] retVal = new BigInteger[n];
		String sqlQuery = "SELECT nextval('" + sequencerName + "') from generate_series(1," + n + ",1)";
		Query hsql = getEntityManager().createNativeQuery(sqlQuery);
		List ids = hsql.getResultList();
		for (int i = 0; i < n; i++) {
			retVal[i] = (BigInteger) ids.get(i);
		}

		return retVal;
	}
	

}
