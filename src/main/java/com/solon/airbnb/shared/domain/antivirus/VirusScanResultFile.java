package com.solon.airbnb.shared.domain.antivirus;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VirusScanResultFile implements Serializable{

	private static final long serialVersionUID = 5758622640555939971L;
	
	private String fileAlias;

    @Singular
    private Collection<String> viruses;
}
