package com.solon.airbnb.shared.exception;

import java.util.List;

public class AggregateDutException extends AirbnbException{

	private static final long serialVersionUID = 2454950568252034446L;
	
	private List<AirbnbException> basket;

    public AggregateDutException(List<AirbnbException> basket) {
        this.basket = basket;
    }

}
