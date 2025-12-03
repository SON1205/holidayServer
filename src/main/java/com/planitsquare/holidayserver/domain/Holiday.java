package com.planitsquare.holidayserver.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.planitsquare.holidayserver.common.domain.BaseEntity;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_holiday_country_date", columnList = "country_id, date"),
        @Index(name = "idx_holiday_date", columnList = "date")
})
@Getter
@Builder
public class Holiday extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;
    private String localName;
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    private Boolean fixed;
    private Boolean global;
    private Integer launchYear;

    @ElementCollection
    @CollectionTable(name = "holiday_county", joinColumns = @JoinColumn(name = "holiday_id"))
    @BatchSize(size = 100)
    @Builder.Default
    @Column(name = "counties", nullable = false)
    private List<String> counties = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "holiday_type", joinColumns = @JoinColumn(name = "holiday_id"))
    @BatchSize(size = 100)
    @Builder.Default
    @Column(name = "type")
    private List<String> types = new ArrayList<>();

    public static Holiday fromApi(HolidayApiRes apiRes, Country country) {
        return Holiday.builder()
                .date(apiRes.date())
                .localName(apiRes.localName())
                .name(apiRes.name())
                .country(country)
                .fixed(apiRes.fixed())
                .global(apiRes.global())
                .launchYear(apiRes.launchYear())
                .counties(apiRes.counties() == null ? new ArrayList<>() : apiRes.counties())
                .types(apiRes.types() == null ? new ArrayList<>() : apiRes.types())
                .build();
    }

    public void update(HolidayApiRes res) {
        this.date = res.date();
        this.localName = res.localName();
        this.name = res.name();
        this.fixed = res.fixed();
        this.global = res.global();
        this.launchYear = res.launchYear();

        updateCounties(res.counties());
        updateTypes(res.types());
    }

    private void updateCounties(List<String> newCounties) {
        if (newCounties == null) {
            newCounties = new ArrayList<>();
        }

        // 1. 사이즈가 다르면 무조건 다른 것
        if (this.counties.size() != newCounties.size()) {
            replaceCounties(newCounties);
            return;
        }

        // 2. HashSet으로 변환하여 순서 무시하고 비교
        // (기존 리스트와 새 리스트를 Set으로 만들어 비교)
        if (new HashSet<>(this.counties).equals(new HashSet<>(newCounties))) {
            return; // 내용물이 같으므로 아무것도 안 함 (쿼리 방지)
        }

        // 3. 다르면 교체
        replaceCounties(newCounties);
    }

    private void updateTypes(List<String> newTypes) {
        if (newTypes == null) {
            newTypes = new ArrayList<>();
        }

        if (this.types.size() != newTypes.size()) {
            replaceTypes(newTypes);
            return;
        }

        // 순서 무시 비교
        if (new HashSet<>(this.types).equals(new HashSet<>(newTypes))) {
            return;
        }

        replaceTypes(newTypes);
    }

    // 중복 코드 제거용 헬퍼 메서드
    private void replaceCounties(List<String> newCounties) {
        this.counties.clear();
        this.counties.addAll(newCounties);
    }

    private void replaceTypes(List<String> newTypes) {
        this.types.clear();
        this.types.addAll(newTypes);
    }
}
