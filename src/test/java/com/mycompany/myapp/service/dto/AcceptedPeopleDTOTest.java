package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcceptedPeopleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcceptedPeopleDTO.class);
        AcceptedPeopleDTO acceptedPeopleDTO1 = new AcceptedPeopleDTO();
        acceptedPeopleDTO1.setId(1L);
        AcceptedPeopleDTO acceptedPeopleDTO2 = new AcceptedPeopleDTO();
        assertThat(acceptedPeopleDTO1).isNotEqualTo(acceptedPeopleDTO2);
        acceptedPeopleDTO2.setId(acceptedPeopleDTO1.getId());
        assertThat(acceptedPeopleDTO1).isEqualTo(acceptedPeopleDTO2);
        acceptedPeopleDTO2.setId(2L);
        assertThat(acceptedPeopleDTO1).isNotEqualTo(acceptedPeopleDTO2);
        acceptedPeopleDTO1.setId(null);
        assertThat(acceptedPeopleDTO1).isNotEqualTo(acceptedPeopleDTO2);
    }
}
