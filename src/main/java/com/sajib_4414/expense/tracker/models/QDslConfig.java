package com.sajib_4414.expense.tracker.models;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QDslConfig {
    @PersistenceContext
    EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    //this is providing the query factory bean implementation
    @Bean
    public JPAQueryFactory getQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
