package com.hardtech.bellapielpuntoscol.context.domain.paymentLink;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Data implements Serializable {

	@JsonProperty("image_url")
	private Object imageUrl;

	@JsonProperty("merchant_public_key")
	private String merchantPublicKey;

	@JsonProperty("description")
	private String description;

	@JsonProperty("active")
	private boolean active;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("single_use")
	private boolean singleUse;

	@JsonProperty("collect_shipping")
	private boolean collectShipping;

	@JsonProperty("amount_in_cents")
	private Object amountInCents;

	@JsonProperty("expires_at")
	private Object expiresAt;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("name")
	private String name;

	@JsonProperty("customer_data")
	private CustomerData customerData;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("id")
	private String id;

	@JsonProperty("sku")
	private Object sku;

	@JsonProperty("redirect_url")
	private Object redirectUrl;

	@Override
	public String toString() {
		return "Data [imageUrl=" + imageUrl + ", merchantPublicKey=" + merchantPublicKey + ", description="
				+ description + ", active=" + active + ", createdAt=" + createdAt + ", singleUse=" + singleUse
				+ ", collectShipping=" + collectShipping + ", amountInCents=" + amountInCents + ", expiresAt="
				+ expiresAt + ", updatedAt=" + updatedAt + ", name=" + name + ", customerData=" + customerData
				+ ", currency=" + currency + ", id=" + id + ", sku=" + sku + ", redirectUrl=" + redirectUrl + "]";
	}
}
