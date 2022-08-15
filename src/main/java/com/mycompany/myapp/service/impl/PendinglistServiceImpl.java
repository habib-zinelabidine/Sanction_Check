package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Pendinglist;
import com.mycompany.myapp.repository.PendinglistRepository;
import com.mycompany.myapp.service.PendinglistService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pendinglist}.
 */
@Service
@Transactional
public class PendinglistServiceImpl implements PendinglistService {

    private final Logger log = LoggerFactory.getLogger(PendinglistServiceImpl.class);

    private final PendinglistRepository pendinglistRepository;

    public PendinglistServiceImpl(PendinglistRepository pendinglistRepository) {
        this.pendinglistRepository = pendinglistRepository;
    }

    @Override
    public Pendinglist save(Pendinglist pendinglist) {
        log.debug("Request to save Pendinglist : {}", pendinglist);
        return pendinglistRepository.save(pendinglist);
    }

    @Override
    public Pendinglist update(Pendinglist pendinglist) {
        log.debug("Request to save Pendinglist : {}", pendinglist);
        return pendinglistRepository.save(pendinglist);
    }

    @Override
    public Optional<Pendinglist> partialUpdate(Pendinglist pendinglist) {
        log.debug("Request to partially update Pendinglist : {}", pendinglist);

        return pendinglistRepository
            .findById(pendinglist.getId())
            .map(existingPendinglist -> {
                if (pendinglist.getFirstName() != null) {
                    existingPendinglist.setFirstName(pendinglist.getFirstName());
                }
                if (pendinglist.getLastName() != null) {
                    existingPendinglist.setLastName(pendinglist.getLastName());
                }
                if (pendinglist.getState() != null) {
                    existingPendinglist.setState(pendinglist.getState());
                }

                return existingPendinglist;
            })
            .map(pendinglistRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pendinglist> findAll(Pageable pageable) {
        log.debug("Request to get all Pendinglists");
        return pendinglistRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pendinglist> findOne(Long id) {
        log.debug("Request to get Pendinglist : {}", id);
        return pendinglistRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pendinglist : {}", id);
        pendinglistRepository.deleteById(id);
    }
}
