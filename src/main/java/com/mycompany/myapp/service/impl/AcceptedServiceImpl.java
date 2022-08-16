package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Accepted;
import com.mycompany.myapp.repository.AcceptedRepository;
import com.mycompany.myapp.service.AcceptedService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Accepted}.
 */
@Service
@Transactional
public class AcceptedServiceImpl implements AcceptedService {

    private final Logger log = LoggerFactory.getLogger(AcceptedServiceImpl.class);

    private final AcceptedRepository acceptedRepository;

    public AcceptedServiceImpl(AcceptedRepository acceptedRepository) {
        this.acceptedRepository = acceptedRepository;
    }

    @Override
    public Accepted save(Accepted accepted) {
        log.debug("Request to save Accepted : {}", accepted);
        return acceptedRepository.save(accepted);
    }

    @Override
    public Accepted update(Accepted accepted) {
        log.debug("Request to save Accepted : {}", accepted);
        return acceptedRepository.save(accepted);
    }

    @Override
    public Optional<Accepted> partialUpdate(Accepted accepted) {
        log.debug("Request to partially update Accepted : {}", accepted);

        return acceptedRepository
            .findById(accepted.getId())
            .map(existingAccepted -> {
                if (accepted.getFirstName() != null) {
                    existingAccepted.setFirstName(accepted.getFirstName());
                }
                if (accepted.getLastName() != null) {
                    existingAccepted.setLastName(accepted.getLastName());
                }

                return existingAccepted;
            })
            .map(acceptedRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Accepted> findAll(Pageable pageable) {
        log.debug("Request to get all Accepteds");
        return acceptedRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Accepted> findOne(Long id) {
        log.debug("Request to get Accepted : {}", id);
        return acceptedRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Accepted : {}", id);
        acceptedRepository.deleteById(id);
    }
}
