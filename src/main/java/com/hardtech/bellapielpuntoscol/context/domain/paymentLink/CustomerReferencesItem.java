package com.hardtech.bellapielpuntoscol.context.domain.paymentLink;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerReferencesItem{

	@JsonProperty("is_required")
	private boolean isRequired;

	@JsonProperty("label")
	private String label;
}
