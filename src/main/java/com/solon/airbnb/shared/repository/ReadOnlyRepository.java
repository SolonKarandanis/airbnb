package com.solon.airbnb.shared.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;


@NoRepositoryBean
public interface ReadOnlyRepository<T,ID>  extends Repository<T,ID>{

    public List<T> findAll();

    public List<T> findAll(Sort sort);

    public Page<T> findAll(Pageable pageable);

    Optional<T> findById(ID id);

    long count();

}
