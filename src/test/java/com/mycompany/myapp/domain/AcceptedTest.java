package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcceptedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Accepted.class);
        Accepted accepted1 = new Accepted();
        accepted1.setId(1L);
        Accepted accepted2 = new Accepted();
        accepted2.setId(accepted1.getId());
        assertThat(accepted1).isEqualTo(accepted2);
        accepted2.setId(2L);
        assertThat(accepted1).isNotEqualTo(accepted2);
        accepted1.setId(null);
        assertThat(accepted1).isNotEqualTo(accepted2);
    }
}
