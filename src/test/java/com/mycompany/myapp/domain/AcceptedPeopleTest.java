package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcceptedPeopleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcceptedPeople.class);
        AcceptedPeople acceptedPeople1 = new AcceptedPeople();
        acceptedPeople1.setId(1L);
        AcceptedPeople acceptedPeople2 = new AcceptedPeople();
        acceptedPeople2.setId(acceptedPeople1.getId());
        assertThat(acceptedPeople1).isEqualTo(acceptedPeople2);
        acceptedPeople2.setId(2L);
        assertThat(acceptedPeople1).isNotEqualTo(acceptedPeople2);
        acceptedPeople1.setId(null);
        assertThat(acceptedPeople1).isNotEqualTo(acceptedPeople2);
    }
}
