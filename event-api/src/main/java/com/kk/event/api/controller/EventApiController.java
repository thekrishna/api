package com.kk.event.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kk.event.api.exception.RecordAlreadyExistException;
import com.kk.event.api.exception.RecordNotFoundException;
import com.kk.event.api.model.Actor;
import com.kk.event.api.model.Event;
import com.kk.event.api.model.Repo;
import com.kk.event.api.repository.ActorRepository;
import com.kk.event.api.repository.EventRepository;
import com.kk.event.api.repository.RepoRepository;

@RestController
public class EventApiController {
	private final EventRepository eventRepository;
	private final ActorRepository actorRepository;
	private final RepoRepository repoRepository;

	@Autowired
	public EventApiController(EventRepository eventRepository, ActorRepository actorRepository,
			RepoRepository repoRepository) {
		this.eventRepository = eventRepository;
		this.actorRepository = actorRepository;
		this.repoRepository = repoRepository;
	}

	@DeleteMapping("/erase/")
	@ResponseStatus(code = HttpStatus.OK)
	public void eraseEvents() {
		this.eventRepository.deleteAll();
	}

	@PostMapping(value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Event addEvent(@RequestBody Event event) {
		final Optional<Event> optionalEvent = this.eventRepository.findById(event.getId());
		if (optionalEvent.isPresent()) {
			throw new RecordAlreadyExistException(String.format("Event already exist for this %s id", event.getId()));
		}

		final Optional<Actor> optActor = this.actorRepository.findById(event.getActor().getId());
		if (optActor.isPresent()) {
			event.setActor(optActor.get());
		} else {
			this.actorRepository.save(event.getActor());
			// Either save Actor or throw Foreign Key Actor does not exist.
		}

		final Optional<Repo> opRepo = this.repoRepository.findById(event.getRepo().getId());
		if (opRepo.isPresent()) {
			event.setRepo(opRepo.get());
		} else {
			this.repoRepository.save(event.getRepo());
			// Either save Repo or throw Foreign Key Repo does not exist.
		}

		return this.eventRepository.save(event);
	}

	@GetMapping("/events")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getAllEvents() {
		final List<Event> events = new ArrayList<>();
		final Iterable<Event> iterable = this.eventRepository.findAll(new Sort("id"));
		iterable.forEach(event -> events.add(event));
		return events;
	}

	@GetMapping("/events/repos/{repoID}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getEventsByRepoId(@PathVariable Long repoID) {
		final List<Event> events = new ArrayList<>();
		final Iterable<Event> iterable = this.eventRepository.findByRepoId(repoID);
		iterable.forEach(event -> events.add(event));
		return events;
	}

	@GetMapping("/events/actors/{actorID}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getEventsByActorId(@PathVariable Long actorID) {
		final List<Event> events = new ArrayList<>();
		final Iterable<Event> iterable = this.eventRepository.findByActorId(actorID);
		iterable.forEach(event -> events.add(event));
		return events;
	}

	@GetMapping("/events/repos/{repoID}/actors/{actorID}")
	@ResponseStatus(code = HttpStatus.OK)
	public Event getEventByRepoIdAndActorId(@PathVariable Long repoID, @PathVariable Long actorID) {
		return this.eventRepository.findByRepoIdAndActorId(repoID, actorID)
				.orElseThrow(()-> new RecordNotFoundException("Event not found for ActorId and RepoId"));
	}
}
