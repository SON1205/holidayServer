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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Builder.Default
    @Column(name = "counties", nullable = false)
    private List<String> counties = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "holiday_type", joinColumns = @JoinColumn(name = "holiday_id"))
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
                .counties(apiRes.counties())
                .types(apiRes.types())
                .build();
    }

    public void update(HolidayApiRes res) {
        this.date = res.date();
        this.localName = res.localName();
        this.name = res.name();
        this.fixed = res.fixed();
        this.global = res.global();
        this.launchYear = res.launchYear();

        this.counties.clear();
        if (res.counties() != null) {
            this.counties.addAll(res.counties());
        }

        this.types.clear();
        if (res.types() != null) {
            this.types.addAll(res.types());
        }
    }
}
