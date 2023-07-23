package com.hardtech.bellapielpuntoscol.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.CustomerData;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.CustomerReferencesItem;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkBody;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentLinkService {

    @Value("${wompi.url}")
    private String url;

    @Value("${wompi.authorization}")
    private String authorization;

    private static final String ACCOUNT_SID =
            "ACffa02de3755346b9cb012611c27ab587";
    private static final String AUTH_TOKEN = "b52ae41971036ecf43576b4aa463ef6b";


    public PaymentLinkBody createPaymentLinkBody(String name, String description, boolean singleUse, boolean collectShipping, String currency, int amountInCents, String vendorCode){
        CustomerReferencesItem customerReferencesItem = new CustomerReferencesItem();
        customerReferencesItem.setLabel(vendorCode);
        customerReferencesItem.setRequired(false);
        List<CustomerReferencesItem> customerReferencesItemList = new ArrayList<>();
        customerReferencesItemList.add(customerReferencesItem);

        CustomerData customerData = new CustomerData(customerReferencesItemList);
        return new PaymentLinkBody(collectShipping, name, customerData, description, singleUse, amountInCents, currency);
    }

    @SneakyThrows
    public PaymentLinkResponse sendPaymentLinkRequest(PaymentLinkBody paymentLinkBody) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request URL

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authorization);



        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(paymentLinkBody);



        // Create the HTTP entity with headers and body
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PaymentLinkResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, PaymentLinkResponse.class);
        PaymentLinkResponse responseBody = response.getBody();

        log.info("https://checkout.wompi.co/l/"+responseBody.getData().getId());

        return responseBody;
    }

    public void sendSMS(String paymentLink) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+573046136949"),
                new com.twilio.type.PhoneNumber("+12343015274"),
                "Este es tu link de pago: "+paymentLink)
                .create();
    }



}
