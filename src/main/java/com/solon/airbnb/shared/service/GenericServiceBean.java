package com.solon.airbnb.shared.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.SynchronizationType;

@Component
public class GenericServiceBean {
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION,
            synchronization = SynchronizationType.SYNCHRONIZED)
    private EntityManager entityManager;
	
	@Autowired
    private TransactionTemplate transactionTemplate;
	
	@Autowired
    private PlatformTransactionManager transactionManager;
	
	@Autowired
    protected MessageSource messageSource;
	
	
	protected EntityManager getEntityManager() {
	    return entityManager;
	}
	
	protected JPAQueryFactory getJPAQueryFactory() {
		return new JPAQueryFactory(getEntityManager());
	}
	
	protected TransactionTemplate getTransactionTemplate() {
    	return transactionTemplate;
    }
	
	protected PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }
	
	protected Locale getDefaultLocale() {
	        return Locale.ENGLISH;
	}
	

    public String translate(String key) {
        return Optional.ofNullable(messageSource.getMessage(key, null, getDefaultLocale())).orElse(key);
    }


    public String translate(String key, Locale locale) {
        return Optional.ofNullable(messageSource.getMessage(key, null, locale)).orElse(key);
    }

}
