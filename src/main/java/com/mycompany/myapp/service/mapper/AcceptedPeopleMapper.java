package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AcceptedPeople;
import com.mycompany.myapp.service.dto.AcceptedPeopleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AcceptedPeople} and its DTO {@link AcceptedPeopleDTO}.
 */
@Mapper(componentModel = "spring")
public interface AcceptedPeopleMapper extends EntityMapper<AcceptedPeopleDTO, AcceptedPeople> {}
