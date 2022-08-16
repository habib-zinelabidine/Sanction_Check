package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RejectedListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RejectedList.class);
        RejectedList rejectedList1 = new RejectedList();
        rejectedList1.setId(1L);
        RejectedList rejectedList2 = new RejectedList();
        rejectedList2.setId(rejectedList1.getId());
        assertThat(rejectedList1).isEqualTo(rejectedList2);
        rejectedList2.setId(2L);
        assertThat(rejectedList1).isNotEqualTo(rejectedList2);
        rejectedList1.setId(null);
        assertThat(rejectedList1).isNotEqualTo(rejectedList2);
    }
}
