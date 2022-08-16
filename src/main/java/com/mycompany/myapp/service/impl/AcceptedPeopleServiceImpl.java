package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AcceptedPeople;
import com.mycompany.myapp.repository.AcceptedPeopleRepository;
import com.mycompany.myapp.service.AcceptedPeopleService;
import com.mycompany.myapp.service.dto.AcceptedPeopleDTO;
import com.mycompany.myapp.service.mapper.AcceptedPeopleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AcceptedPeople}.
 */
@Service
@Transactional
public class AcceptedPeopleServiceImpl implements AcceptedPeopleService {

    private final Logger log = LoggerFactory.getLogger(AcceptedPeopleServiceImpl.class);

    private final AcceptedPeopleRepository acceptedPeopleRepository;

    private final AcceptedPeopleMapper acceptedPeopleMapper;

    public AcceptedPeopleServiceImpl(AcceptedPeopleRepository acceptedPeopleRepository, AcceptedPeopleMapper acceptedPeopleMapper) {
        this.acceptedPeopleRepository = acceptedPeopleRepository;
        this.acceptedPeopleMapper = acceptedPeopleMapper;
    }

    @Override
    public AcceptedPeopleDTO save(AcceptedPeopleDTO acceptedPeopleDTO) {
        log.debug("Request to save AcceptedPeople : {}", acceptedPeopleDTO);
        AcceptedPeople acceptedPeople = acceptedPeopleMapper.toEntity(acceptedPeopleDTO);
        acceptedPeople = acceptedPeopleRepository.save(acceptedPeople);
        return acceptedPeopleMapper.toDto(acceptedPeople);
    }

    @Override
    public AcceptedPeopleDTO update(AcceptedPeopleDTO acceptedPeopleDTO) {
        log.debug("Request to save AcceptedPeople : {}", acceptedPeopleDTO);
        AcceptedPeople acceptedPeople = acceptedPeopleMapper.toEntity(acceptedPeopleDTO);
        acceptedPeople = acceptedPeopleRepository.save(acceptedPeople);
        return acceptedPeopleMapper.toDto(acceptedPeople);
    }

    @Override
    public Optional<AcceptedPeopleDTO> partialUpdate(AcceptedPeopleDTO acceptedPeopleDTO) {
        log.debug("Request to partially update AcceptedPeople : {}", acceptedPeopleDTO);

        return acceptedPeopleRepository
            .findById(acceptedPeopleDTO.getId())
            .map(existingAcceptedPeople -> {
                acceptedPeopleMapper.partialUpdate(existingAcceptedPeople, acceptedPeopleDTO);

                return existingAcceptedPeople;
            })
            .map(acceptedPeopleRepository::save)
            .map(acceptedPeopleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcceptedPeopleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AcceptedPeople");
        return acceptedPeopleRepository.findAll(pageable).map(acceptedPeopleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcceptedPeopleDTO> findOne(Long id) {
        log.debug("Request to get AcceptedPeople : {}", id);
        return acceptedPeopleRepository.findById(id).map(acceptedPeopleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AcceptedPeople : {}", id);
        acceptedPeopleRepository.deleteById(id);
    }
}
