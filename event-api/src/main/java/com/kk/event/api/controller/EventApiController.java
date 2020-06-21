package com.kk.event.api.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.kk.event.api.model.Event;
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
		this.actorRepository.save(event.getActor());
		this.repoRepository.save(event.getRepo());
		return this.eventRepository.save(event);
	}

	@GetMapping("/events")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getAllEvents() {
		return (List<Event>) this.eventRepository.findAll(new Sort("id"));
	}

	@GetMapping("/events/repos/{repoID}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getAllEventsByRepoId(@PathVariable Long repoID) {
		return(List<Event>) this.eventRepository.findByRepoId(repoID);
	}
	
	@GetMapping("/events/actors/{actorID}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getAllEventsByActorId(@PathVariable Long actorID) {
		return (List<Event>) this.eventRepository.findByActorId(actorID);
	}
	
	@GetMapping("/events/repos/{repoID}/actors/{actorID}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getAllEventsByRepoIdAndActorId(@PathVariable Long repoID,@PathVariable Long actorID) {
		final List<Event> events = (List<Event>) this.eventRepository.findByRepoIdAndActorId(repoID, actorID);
		return events.stream().sorted(Comparator.comparing(Event::getId)).collect(Collectors.toList());
	}
}
