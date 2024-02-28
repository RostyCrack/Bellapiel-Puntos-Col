package com.hardtech.bellapielpuntoscol.api;

import com.hardtech.bellapielpuntoscol.context.datasource.DataSourceContextHolder;
import com.hardtech.bellapielpuntoscol.context.datasource.DataSourceEnum;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkBody;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.xml.bind.JAXBException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class PaymentLinkController {

    private final PaymentLinkService paymentLinkService;
    private final XMLService xmlService;

    private final DataSourceContextHolder dataSourceContextHolder;

    @GetMapping({"/wompi/v1/payment-links"})
    public String requestPaymentLink() {
        dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);

        PaymentLinkBody paymentLinkBody = paymentLinkService.createPaymentLinkBody("Monthly rent - Wompi Tower Apartments", "Pay here your apartment monthly rent", false, false, "COP", 15000000, "123456789");
        PaymentLinkResponse paymentLinkResponse = paymentLinkService.sendPaymentLinkRequest(paymentLinkBody);
        paymentLinkService.sendSMS("https://checkout.wompi.co/l/" + paymentLinkResponse.getData().getId(), "3046136949");
        return ("https://checkout.wompi.co/l/" + paymentLinkResponse.getData().getId());

    }

    @GetMapping({"/wompi/v1/payment-links2"})
    public ResponseEntity<String> paymentlink(@RequestParam String numSerie, @RequestParam int numPedido) {
        dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);

        try{
            var paymentLink = paymentLinkService.flujoPaymentlink(numSerie, numPedido);
            return ResponseEntity.ok(paymentLink);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el link de pago:" + e.getMessage());
        }

    }

    @GetMapping({"/wompi/v1/payment-link"})
    public ResponseEntity<String> paymentLink(){
        dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);

        try {
            Map<String, String> pedidoMap = this.xmlService.readPedidoXML("payment-link.xml");
            String numSerie = pedidoMap.get("numSerie");
            int numPedido = Integer.parseInt(pedidoMap.get("numFactura"));
            var paymentLink = paymentLinkService.flujoPaymentlink(numSerie, numPedido);
            return ResponseEntity.ok(paymentLink);
        }
        catch (JAXBException e){
            return ResponseEntity.badRequest().body("Error al crear el link de pago.");
        }
    }
}
