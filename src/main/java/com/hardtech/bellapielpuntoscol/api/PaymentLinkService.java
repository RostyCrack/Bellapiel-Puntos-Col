package com.hardtech.bellapielpuntoscol.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.CustomerData;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.CustomerReferencesItem;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkBody;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.PaymentLinkResponse;
import com.hardtech.bellapielpuntoscol.context.domain.paymentLink.exceptions.PedidoNotFoundException;
import com.hardtech.bellapielpuntoscol.infrastructure.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentLinkService {

    @Value("${wompi.url}")
    private String url;

    @Value("${wompi.authorization}")
    private String authorization;

    @Value("${wompi.channel}")
    private String channel;



    private static final String ACCOUNT_SID = "ACffa02de3755346b9cb012611c27ab587";
    private static final String AUTH_TOKEN = "b52ae41971036ecf43576b4aa463ef6b";

    private final PedVentaLinRepository pedVentaLinRepository;

    private final PedVentaCabRepository pedVentaCabRepository;

    private final ClientesRepository clientesRepository;
    public PaymentLinkService(PedVentaLinRepository pedVentaLinRepository, PedVentaCabRepository pedVentaCabRepository, ClientesRepository clientesRepository){
        this.pedVentaLinRepository = pedVentaLinRepository;
        this.pedVentaCabRepository = pedVentaCabRepository;
        this.clientesRepository = clientesRepository;
    }


    public PaymentLinkBody createPaymentLinkBody(String name, String description, boolean singleUse, boolean collectShipping, String currency, int amountInCents, String vendorCode){
        CustomerReferencesItem customerReferencesItem = new CustomerReferencesItem();
        customerReferencesItem.setLabel(vendorCode);
        customerReferencesItem.setRequired(false);
        List<CustomerReferencesItem> customerReferencesItemList = new ArrayList<>();
        customerReferencesItemList.add(customerReferencesItem);

        CustomerData customerData = new CustomerData(customerReferencesItemList);
        return new PaymentLinkBody(collectShipping, name, customerData, description, singleUse, amountInCents, currency, LocalDateTime.now().plusHours(1));
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

        assert responseBody != null;
        log.info("https://checkout.wompi.co/l/"+responseBody.getData().getId());

        return responseBody;
    }

    public void sendSMS(String paymentLink, String phone) {
        String telefonoReceptor = "whatsapp:+57"+phone;
        String telefonoEnvio = "whatsapp:+573027879004";
        log.info("Enviando SMS");
        if (channel == "sms"){
            log.info("Canal de envio: SMS");
            telefonoReceptor = "+57"+phone;
            telefonoEnvio = "+12343015274";
        }else{
            log.info("Canal de envio: Whatsapp");
        }
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(telefonoEnvio),
                new com.twilio.type.PhoneNumber(telefonoReceptor),
                        "¡Hola! Este es tu link de pago: " + paymentLink)
                .create();
        log.info(message.toString());

    }

    public List<PedVentaLin> retrievePedido(String numSerie, int numPedido){

        log.info("Consultando pedido: "+numSerie+"-"+numPedido);

        try{
            List<PedVentaLin> pedidoArticulos = pedVentaLinRepository.findDistinctByNumSerieAndNumPedido(numSerie, numPedido);
            for (PedVentaLin pedidioArticulo: pedidoArticulos) {
                log.info("Articulo: " + pedidioArticulo.toString());
            }
            return pedidoArticulos;
        }catch (Exception e){
            log.error("Error al leer la base de datos: "+e.getMessage());
            throw new PedidoNotFoundException();
        }
    }

    public PaymentLinkBody createPaymentLinkBody(List<PedVentaLin> articulos, PedVentaCab pedido){
        log.info("Creando peticion de pago");
        String numSerie = pedido.getNumSerie();
        int numPedido = pedido.getNumPedido();

        PaymentLinkBody body = new PaymentLinkBody();
        body.setCurrency("COP");

        body.setName("Pedido: "+ numSerie+"-"+ numPedido);
        StringBuilder description = new StringBuilder();

        for (PedVentaLin pedidoArticulo: articulos) {
            description.append("•");
            description.append(pedidoArticulo.getDescripcion());
            description.append(System.getProperty("line.separator"));
        }
        log.info("Descripcion: "+ description);
        body.setDescription(description.toString());

        int totalInCents = this.retrieveTotalCostInCents(pedido);
        body.setAmountInCents(totalInCents);

        body.setSingleUse(true);

        body.setCollectShipping(false);

        CustomerData camposPersonalizados = getCustomerData(articulos, numSerie, numPedido);

        body.setCustomerData(camposPersonalizados);
        return body;
    }

    @NotNull
    private static CustomerData getCustomerData(List<PedVentaLin> articulos, String numSerie, int numPedido) {
        String caja = numSerie.substring(0, 3);
        CustomerReferencesItem codVendedorField = new CustomerReferencesItem();
        codVendedorField.setLabel(articulos.get(0).getBodega() + "|" + caja);
        //codVendedorField.setLabel(articulos.get(0).getCodVendedor());
        codVendedorField.setRequired(false);

        CustomerReferencesItem numPedidoField = new CustomerReferencesItem();
        numPedidoField.setLabel(numSerie +"-"+ numPedido);


        List<CustomerReferencesItem> customerReferencesItemList = new ArrayList<>();
        customerReferencesItemList.add(codVendedorField);
        customerReferencesItemList.add(numPedidoField);

        return new CustomerData(customerReferencesItemList);
    }

    public int retrieveTotalCostInCents(PedVentaCab pedido){
        return (int) (pedido.getTotNeto()*100);
    }

    public String retrievePhoneNumber(PedVentaCab pedido) {
        log.info("Consultando numero de telefono");
        int codCliente = pedido.getCodCliente();
        Clientes cliente = clientesRepository.findByCodCliente(codCliente);
        String telefono = cliente.getTelefono();

        String phoneNumberWithOnlyNumbers = telefono.replaceAll("\\D", "");

        if (phoneNumberWithOnlyNumbers.length() != 10) {
            throw new IllegalArgumentException("Numero de telefono invalido");
        }
        return phoneNumberWithOnlyNumbers;
    }

    public String flujoPaymentlink(String numSerie, int numPedido){

        List<PedVentaLin> articulos = retrievePedido(numSerie, numPedido);
        PedVentaCab pedido = pedVentaCabRepository.findByNumSerieAndNumPedido(numSerie, numPedido);
        String phoneNumber = retrievePhoneNumber(pedido);

        PaymentLinkBody paymentLinkBody = createPaymentLinkBody(articulos, pedido);
        PaymentLinkResponse paymentLinkResponse = sendPaymentLinkRequest(paymentLinkBody);

        String paymentLink = "https://checkout.wompi.co/l/"+paymentLinkResponse.getData().getId();
        sendSMS(paymentLink, phoneNumber);

        return paymentLink;
    }

}
