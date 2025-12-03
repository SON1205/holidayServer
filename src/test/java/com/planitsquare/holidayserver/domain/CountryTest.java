package com.planitsquare.holidayserver.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("엔티티 테스트 - Country")
class CountryTest {

    @Test
    @DisplayName("나라 이름 업데이트 테스트")
    void updateTest() {
        Country country = new Country("KR", "Korea");

        country.updateName("South Korea");

        assertThat(country.getName()).isEqualTo("South Korea");
    }
}