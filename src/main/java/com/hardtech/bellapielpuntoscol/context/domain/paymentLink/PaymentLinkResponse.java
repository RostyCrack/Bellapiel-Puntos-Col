package com.hardtech.bellapielpuntoscol.context.domain.paymentLink;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkResponse {

	@JsonProperty("data")
	private Data data;

	@JsonProperty("meta")
	private Meta meta;

	@Override
	public String toString() {
		return "PaymentLinkResponse [data=" + data + ", meta=" + meta + "]";
	}
}
