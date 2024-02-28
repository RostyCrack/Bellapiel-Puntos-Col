package com.hardtech.bellapielpuntoscol.context.domain.paymentLink;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerData{

	@JsonProperty("customer_references")
	private List<CustomerReferencesItem> customerReferences;
}
