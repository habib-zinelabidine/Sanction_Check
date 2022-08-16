package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcceptedListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcceptedList.class);
        AcceptedList acceptedList1 = new AcceptedList();
        acceptedList1.setId(1L);
        AcceptedList acceptedList2 = new AcceptedList();
        acceptedList2.setId(acceptedList1.getId());
        assertThat(acceptedList1).isEqualTo(acceptedList2);
        acceptedList2.setId(2L);
        assertThat(acceptedList1).isNotEqualTo(acceptedList2);
        acceptedList1.setId(null);
        assertThat(acceptedList1).isNotEqualTo(acceptedList2);
    }
}
