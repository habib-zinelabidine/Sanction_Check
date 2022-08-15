package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PendinglistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pendinglist.class);
        Pendinglist pendinglist1 = new Pendinglist();
        pendinglist1.setId(1L);
        Pendinglist pendinglist2 = new Pendinglist();
        pendinglist2.setId(pendinglist1.getId());
        assertThat(pendinglist1).isEqualTo(pendinglist2);
        pendinglist2.setId(2L);
        assertThat(pendinglist1).isNotEqualTo(pendinglist2);
        pendinglist1.setId(null);
        assertThat(pendinglist1).isNotEqualTo(pendinglist2);
    }
}
