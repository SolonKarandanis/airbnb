package com.solon.airbnb.listing.application.dto;

import com.solon.airbnb.listing.application.dto.vo.BathsVO;
import com.solon.airbnb.listing.application.dto.vo.BedroomsVO;
import com.solon.airbnb.listing.application.dto.vo.BedsVO;
import com.solon.airbnb.listing.application.dto.vo.GuestsVO;
import com.solon.airbnb.shared.dto.SearchRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class TenantSearchRequestDTO extends SearchRequestDTO {
    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime endDate;

    @NotEmpty
    private String location;

    @NotNull
    @Valid
    private GuestsVO guests;

    @NotNull
    @Valid
    private BedroomsVO bedrooms;

    @NotNull
    @Valid
    private BedsVO beds;

    @NotNull
    @Valid
    private BathsVO baths;

}
