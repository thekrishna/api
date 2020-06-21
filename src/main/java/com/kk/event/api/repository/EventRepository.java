package com.kk.event.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kk.event.api.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>{
	Optional<Event> findById(Long id);
	List<Event> findByActorId(Long actorId);
	List<Event> findByRepoId(Long repoId);
	List<Event> findByRepoIdAndActorId(Long repoId, Long actorId);
}
