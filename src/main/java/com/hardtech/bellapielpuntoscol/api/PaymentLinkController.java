package com.hardtech.bellapielpuntoscol.api;

import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkBody;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class PaymentLinkController {

    private final PaymentLinkService paymentLinkService;
    private final XMLService xmlService;

    @GetMapping({"/wompi/v1/payment-links"})
    public String requestPaymentLink() {
        PaymentLinkBody paymentLinkBody = paymentLinkService.createPaymentLinkBody("Monthly rent - Wompi Tower Apartments", "Pay here your apartment monthly rent", false, false, "COP", 15000000, "123456789");
        PaymentLinkResponse paymentLinkResponse = paymentLinkService.sendPaymentLinkRequest(paymentLinkBody);
        paymentLinkService.sendSMS("https://checkout.wompi.co/l/" + paymentLinkResponse.getData().getId(), "3046136949");
        return ("https://checkout.wompi.co/l/" + paymentLinkResponse.getData().getId());

    }

    @GetMapping({"/wompi/v1/payment-links2"})
    public String paymentlink(@RequestParam String numSerie, @RequestParam int numPedido) {

        try{
            return paymentLinkService.flujoPaymentlink(numSerie, numPedido);
        }catch (Exception e) {
            return e.getMessage();
        }

    }

    @GetMapping({"/wompi/v1/payment-link"})
    public String paymentLink(){
        try {
            Map<String, String> pedidoMap = this.xmlService.readXML("payment-link.xml");
            String numSerie = pedidoMap.get("numSerie");
            int numPedido = Integer.parseInt(pedidoMap.get("numFactura"));
            return this.paymentlink(numSerie, numPedido);
        }
        catch (JAXBException e){
            return "Error al leer el archivo XML.";
        }
    }


}
