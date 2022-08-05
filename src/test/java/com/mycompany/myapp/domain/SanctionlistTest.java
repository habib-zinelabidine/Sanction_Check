package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanctionlistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sanctionlist.class);
        Sanctionlist sanctionlist1 = new Sanctionlist();
        sanctionlist1.setId(1L);
        Sanctionlist sanctionlist2 = new Sanctionlist();
        sanctionlist2.setId(sanctionlist1.getId());
        assertThat(sanctionlist1).isEqualTo(sanctionlist2);
        sanctionlist2.setId(2L);
        assertThat(sanctionlist1).isNotEqualTo(sanctionlist2);
        sanctionlist1.setId(null);
        assertThat(sanctionlist1).isNotEqualTo(sanctionlist2);
    }
}
