package com.solon.airbnb.shared.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class AggregateAirbnbException extends AirbnbException{

	private static final long serialVersionUID = 2454950568252034446L;
	
	private List<AirbnbException> basket;

    public AggregateAirbnbException(List<AirbnbException> basket) {
        this.basket = basket;
    }

}
