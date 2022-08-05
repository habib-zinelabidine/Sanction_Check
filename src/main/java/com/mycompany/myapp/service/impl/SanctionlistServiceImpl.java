package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Sanctionlist;
import com.mycompany.myapp.repository.SanctionlistRepository;
import com.mycompany.myapp.service.SanctionlistService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sanctionlist}.
 */
@Service
@Transactional
public class SanctionlistServiceImpl implements SanctionlistService {

    private final Logger log = LoggerFactory.getLogger(SanctionlistServiceImpl.class);

    private final SanctionlistRepository sanctionlistRepository;

    public SanctionlistServiceImpl(SanctionlistRepository sanctionlistRepository) {
        this.sanctionlistRepository = sanctionlistRepository;
    }

    @Override
    public Sanctionlist save(Sanctionlist sanctionlist) {
        log.debug("Request to save Sanctionlist : {}", sanctionlist);
        return sanctionlistRepository.save(sanctionlist);
    }

    @Override
    public Sanctionlist update(Sanctionlist sanctionlist) {
        log.debug("Request to save Sanctionlist : {}", sanctionlist);
        return sanctionlistRepository.save(sanctionlist);
    }

    @Override
    public Optional<Sanctionlist> partialUpdate(Sanctionlist sanctionlist) {
        log.debug("Request to partially update Sanctionlist : {}", sanctionlist);

        return sanctionlistRepository
            .findById(sanctionlist.getId())
            .map(existingSanctionlist -> {
                if (sanctionlist.getFirstName() != null) {
                    existingSanctionlist.setFirstName(sanctionlist.getFirstName());
                }
                if (sanctionlist.getLastName() != null) {
                    existingSanctionlist.setLastName(sanctionlist.getLastName());
                }
                if (sanctionlist.getDob() != null) {
                    existingSanctionlist.setDob(sanctionlist.getDob());
                }
                if (sanctionlist.getAddress() != null) {
                    existingSanctionlist.setAddress(sanctionlist.getAddress());
                }
                if (sanctionlist.getPassport() != null) {
                    existingSanctionlist.setPassport(sanctionlist.getPassport());
                }
                if (sanctionlist.getScore() != null) {
                    existingSanctionlist.setScore(sanctionlist.getScore());
                }

                return existingSanctionlist;
            })
            .map(sanctionlistRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sanctionlist> findAll(Pageable pageable) {
        log.debug("Request to get all Sanctionlists");
        return sanctionlistRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sanctionlist> findOne(Long id) {
        log.debug("Request to get Sanctionlist : {}", id);
        return sanctionlistRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sanctionlist : {}", id);
        sanctionlistRepository.deleteById(id);
    }
}
