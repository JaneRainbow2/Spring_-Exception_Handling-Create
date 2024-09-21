package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.dto.StateDto;
import com.softserve.itacademy.repository.StateRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateService {

    private final StateRepository stateRepository;

    public State create(State state) {
        log.debug("Creating state {}", state);
        if (state == null) {
            log.error("Failed to create state: state is null");
            throw new NullEntityReferenceException("State cannot be null");
        }

        state = stateRepository.save(state);
        log.debug("State {} was created", state);
        return state;
    }

    public State readById(long id) {
        log.debug("Fetching state with id: {}", id);
        State state = stateRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("State with id {} doesn't exist", id);
                    return new EntityNotFoundException("State with id " + id + " not found");
                });
        log.debug("Fetched state {}", state);
        return state;
    }

    public State update(State state) {
        log.debug("Updating state {}", state);
        if (state == null) {
            log.error("Failed to update state: state is null");
            throw new NullEntityReferenceException("State cannot be null");
        }

        readById(state.getId());  // Ensure the state exists before updating
        state = stateRepository.save(state);
        log.debug("State {} was updated", state);
        return state;
    }

    public void delete(long id) {
        log.debug("Deleting state with id {}", id);
        State state = readById(id);  // Ensure the state exists before deleting
        stateRepository.delete(state);
        log.debug("State with id {} was deleted", id);
    }

    public List<State> getAll() {
        log.debug("Fetching all states");
        List<State> states = stateRepository.findAllByOrderById();
        log.debug("Fetched all states");
        return states;
    }

    public State getByName(String name) {
        log.debug("Fetching state with name {}", name);
        State state = stateRepository.findByName(name);

        if (state != null) {
            log.debug("State found: {}", state);
            return state;
        }

        log.error("No state found with name: {}", name);
        throw new EntityNotFoundException("State with name '" + name + "' not found");
    }

    public List<StateDto> findAll() {
        log.debug("Fetching all states as StateDto");
        List<StateDto> dtos = stateRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
        log.debug("Fetched all states as StateDto");
        return dtos;
    }

    private StateDto toDto(State state) {
        log.debug("Converting {} from State to StateDto", state);
        StateDto dto = StateDto.builder()
                .name(state.getName())
                .build();
        log.debug("Converted {} to StateDto", state);
        return dto;
    }
}