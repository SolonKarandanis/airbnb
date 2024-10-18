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


@Getter
@Setter
public class ListingSearchRequestDTO extends SearchRequestDTO {
    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    @NotEmpty
    private String location;

    @Valid
    private GuestsVO guests;

    @Valid
    private BedroomsVO bedrooms;

    @Valid
    private BedsVO beds;

    @Valid
    private BathsVO baths;

}
