package com.hackerrank.github.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.hackerrank.github.model.Repo;

@Repository
public interface RepoRepository extends CrudRepository<Repo, Long> {
}
