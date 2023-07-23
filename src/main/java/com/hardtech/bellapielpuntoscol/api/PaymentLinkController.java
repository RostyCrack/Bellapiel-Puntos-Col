package com.hardtech.bellapielpuntoscol.api;

import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkBody;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaymentLinkController {

    private final PaymentLinkService paymentLinkService;

    @GetMapping({"/wompi/v1/payment-links"})
    public String requestPaymentLink() {
        PaymentLinkBody paymentLinkBody = paymentLinkService.createPaymentLinkBody("Monthly rent - Wompi Tower Apartments", "Pay here your apartment monthly rent", false, false, "COP", 15000000, "123456789");
        PaymentLinkResponse paymentLinkResponse = paymentLinkService.sendPaymentLinkRequest(paymentLinkBody);
        paymentLinkService.sendSMS("https://checkout.wompi.co/l/" + paymentLinkResponse.getData().getId());
        return ("https://checkout.wompi.co/l/" + paymentLinkResponse.getData().getId());

    }
}
