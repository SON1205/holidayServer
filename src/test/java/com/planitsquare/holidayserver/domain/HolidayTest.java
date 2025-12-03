package com.planitsquare.holidayserver.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.planitsquare.holidayserver.dto.HolidayApiRes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("엔티티 테스트 - Holiday")
class HolidayTest {

    Country country;

    @BeforeEach
    void setUp() {
        country = new Country("KR", "South Korea");
    }

    @Test
    @DisplayName("API 응답에 Null이 있어도, 엔티티 생성 시 기본값으로 안전하게 변환되어야 한다")
    void createFromApiTest() {
        // given
        // API 응답이 불친절하게 필수값 빼고 다 NULL로 왔다고 가정
        HolidayApiRes nullRes = new HolidayApiRes(
                LocalDate.of(2025, 1, 1),
                null,
                "New Year's Day",
                "KR",
                null,
                null,
                null,
                null,
                null
        );

        // when
        Holiday holiday = Holiday.fromApi(nullRes, country);

        // then
        assertThat(holiday.getCounties()).isNotNull().isEmpty();
        assertThat(holiday.getTypes()).isNotNull().isEmpty();
        assertThat(holiday.getName()).isEqualTo("New Year's Day");
        assertThat(holiday.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(holiday.getCountry()).isEqualTo(country);
    }

    @Test
    @DisplayName("API에서 빈 배열이 오면 그대로 빈 리스트로 매핑된다")
    void fromApiEmptyList() {
        HolidayApiRes res = new HolidayApiRes(
                LocalDate.now(), null, "Test", "KR",
                null, null,
                new ArrayList<>(),
                null,
                new ArrayList<>()
        );

        Holiday holiday = Holiday.fromApi(res, country);

        assertThat(holiday.getCounties()).isEmpty();
        assertThat(holiday.getTypes()).isEmpty();
    }

    @Test
    @DisplayName("API 정보로 업데이트 시, 일반 필드와 컬렉션이 모두 변경되어야 한다")
    void updateHolidayTest() {
        // given
        List<String> types = new ArrayList<>(List.of("Public"));
        Holiday holiday = Holiday.builder()
                .country(country)
                .name("New Year's Day")
                .localName("구정")
                .date(LocalDate.of(2025, 1, 1))
                .types(types)
                .build();

        HolidayApiRes newInfo = new HolidayApiRes(
                LocalDate.of(2025, 1, 1),
                "설날",
                "New Year's Day",
                "KR",
                null,
                null,
                null,
                1929,
                List.of("Public", "Observance")
        );

        // when
        holiday.update(newInfo);

        // then
        assertThat(holiday.getName()).isEqualTo("New Year's Day");
        assertThat(holiday.getLocalName()).isEqualTo("설날");
        assertThat(holiday.getTypes()).hasSize(2);
        assertThat(holiday.getTypes()).containsExactly("Public", "Observance");
    }

    @Test
    @DisplayName("동일한 타입 리스트로 업데이트 시 변경이 없어야 한다")
    void updateSameTypesNoChange() {
        // given
        List<String> types = new ArrayList<>(List.of("Optional", "Authorities", "Observance", "Public"));
        Holiday holiday = Holiday.builder()
                .country(country)
                .name("New Year's Day")
                .localName("구정")
                .date(LocalDate.of(2025, 1, 1))
                .types(types)
                .build();

        HolidayApiRes newInfo = new HolidayApiRes(
                LocalDate.of(2025, 1, 1),
                "설날",
                "New Year's Day",
                "KR",
                null,
                null,
                null,
                null,
                List.of("Public", "Observance", "Authorities", "Optional")
        );

        // when
        holiday.update(newInfo);

        // then
        assertThat(holiday.getTypes()).hasSize(4);
        assertThat(holiday.getTypes()).contains("Public", "Observance", "Authorities", "Optional");
    }

    @Test
    @DisplayName("컬렉션의 순서만 다르고 내용이 같은 경우, 업데이트가 발생하지 않아야 한다 (기존 순서 유지)")
    void optimizationTest() {
        // given
        List<String> originalTypes = new ArrayList<>(List.of("Public", "Observance"));
        Holiday holiday = Holiday.builder()
                .country(country)
                .types(originalTypes)
                .name("Test")
                .date(LocalDate.now())
                .build();

        HolidayApiRes reorderedInfo = new HolidayApiRes(
                LocalDate.now(),
                null,
                null,
                "KR",
                null,
                null,
                null,
                null,
                List.of("Observance", "Public")
        );

        // when
        holiday.update(reorderedInfo);

        // then
        assertThat(holiday.getTypes()).containsExactly("Public", "Observance");
    }

    @Test
    @DisplayName("컬렉션이 축소되는 경우 기존 값이 정확히 대체되어야 한다")
    void updateListShrink() {
        Holiday holiday = Holiday.builder()
                .types(new ArrayList<>(List.of("Public", "Observance")))
                .build();

        HolidayApiRes res = new HolidayApiRes(
                null, null, null, null,
                null, null, null,
                null,
                List.of("Public")    // Observance 제거됨
        );

        holiday.update(res);

        assertThat(holiday.getTypes()).containsExactly("Public");
    }

}