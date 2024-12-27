package com.hardtech.bellapielpuntoscol.context.domain.paymentLink;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkBody{

	@JsonProperty("collect_shipping")
	private boolean collectShipping;

	@JsonProperty("name")
	private String name;

	@JsonProperty("customer_data")
	private CustomerData customerData;

	@JsonProperty("description")
	private String description;

	@JsonProperty("single_use")
	private boolean singleUse;

	@JsonProperty("amount_in_cents")
	private int amountInCents;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("expires_at")
	private LocalDateTime expirationDate;
}


