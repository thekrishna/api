package com.kk.event.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kk.event.api.model.Repo;

@Repository
public interface RepoRepository extends CrudRepository<Repo, Long> {
	Optional<Repo> findById(Long id);
}
