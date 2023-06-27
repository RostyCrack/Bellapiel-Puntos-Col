//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hardtech.bellapielpuntoscol.context.domain.account.AccountResponse;
import com.hardtech.bellapielpuntoscol.context.domain.accumulation.AccumulationResponse;
import com.hardtech.bellapielpuntoscol.context.domain.accumulation.RequestBody;
import com.hardtech.bellapielpuntoscol.context.domain.accumulation.exceptions.CommonBusinessErrorException;
import com.hardtech.bellapielpuntoscol.context.domain.cancelation.CancelationRequestBody;
import com.hardtech.bellapielpuntoscol.context.domain.cancelation.CancelationResponse;
import com.hardtech.bellapielpuntoscol.context.domain.cancelation.exceptions.TimeOutException;
import com.hardtech.bellapielpuntoscol.context.domain.token.TokenResponse;
import com.hardtech.bellapielpuntoscol.infrastructure.*;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
public class PuntosService {
    private static final Logger log = LoggerFactory.getLogger(PuntosService.class);
    @Value("${url.headers.ca-channel}")
    private String caChannel;
    @Value("${url.headers.ca-partnercode}")
    private String partnerCode;
    @Value("${url.headers.x-remote-ip}")
    private String xRemoteIp;
    @Value("${url.grant-type}")
    private String grantType;
    @Value("${url.client-secret}")
    private String clientSecret;
    @Value("${url.client-id}")
    private String clientId;
    @Value("${puntos-colombia.url}")
    private String url;
    @Value("${url.locationCode}")
    private String locationCode;
    @Value("${puntos-colombia.version-token}")
    private String versionToken;
    @Value("${puntos-colombia.version-account}")
    private String versionAccount;
    @Value("${puntos-colombia.version-accumulation}")
    private String versionAccumulation;
    @Value("${puntos-colombia.version-cancellation}")
    private String versionCancellation;
    private TokenResponse tokenResponse;
    private final ClientesRepository clientesRepository;
    private final FacturasVentaRepository facturasVentaRepository;
    private final ClienteCamposLibresRepository clienteCamposLibresRepository;
    private final FacturasVentaSeriesRepository facturasVentaSeriesRepository;
    private final FacturasVentaCamposLibresRepository facturasVentaCamposLibresRepository;
    private final AlbVentaLinRepository albVentaLinRepository;
    private final AlbVentaCabRepository albVentaCabRepository;
    private final TesoreriaRepository tesoreriaRepository;


    public PuntosService(FacturasVentaRepository facturasVentaRepository,
                         ClienteCamposLibresRepository clienteCamposLibresRepository,
                         FacturasVentaSeriesRepository facturasVentaSeriesRepository,
                         ClientesRepository clientesRepository,
                         FacturasVentaCamposLibresRepository facturasVentaCamposLibresRepository,
                         AlbVentaLinRepository albVentaLinRepository,
                         AlbVentaCabRepository albVentaCabRepository,
                         TesoreriaRepository tesoreriaRepository) {
        this.facturasVentaRepository = facturasVentaRepository;
        this.clienteCamposLibresRepository = clienteCamposLibresRepository;
        this.facturasVentaSeriesRepository = facturasVentaSeriesRepository;
        this.clientesRepository = clientesRepository;
        this.facturasVentaCamposLibresRepository = facturasVentaCamposLibresRepository;
        this.albVentaLinRepository = albVentaLinRepository;
        this.albVentaCabRepository = albVentaCabRepository;
        this.tesoreriaRepository = tesoreriaRepository;
    }


    public void accumulate() {
        LocalDateTime minDate = LocalDateTime.of(1899, 12, 30, 0, 0);

        log.info("Fetching transactions... ");
        List<FacturasVentaCamposLibres> notAccumulated = this.facturasVentaCamposLibresRepository.findByAccumulatedAfter(minDate);
        if (notAccumulated.isEmpty()) {
            log.info("No transactions to accumulate");

        } else {
            if (this.tokenResponse == null || !this.tokenResponse.getIsTokenExpired()) {
                log.info("Sending token request...");
                this.tokenResponse = this.sendTokenRequest();
                log.info("Token request sent successfully");
            }

                for (FacturasVentaCamposLibres transaction : notAccumulated) {
                    FacturasVenta factura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(transaction.getNumFactura(), transaction.getNumSerie());
                    int var10001 = factura.getNumFactura();
                    log.info("Fetched factura: " + var10001 + " " + factura.getNumSerie());
                    ClientesCamposLibres cliente = this.clienteCamposLibresRepository.findByCodCliente(factura.getCodCliente());
                    AccountResponse accountResponse = this.processShortBalance(cliente, factura);

                    try{
                        this.process(transaction, factura, accountResponse, String.valueOf(cliente.getCodCliente()), cliente.getTipoDeDocumento());
                    }
                    catch (Exception var7) {
                        log.error("Error al acumular puntos: " + var7.getMessage());
                        try {
                            this.process(transaction, factura, accountResponse, String.valueOf(cliente.getCodCliente()), cliente.getTipoDeDocumento());
                        }
                        catch (Exception e){
                            log.error("Error al acumular puntos: " + e.getMessage());
                        }
                    }
                }

        }

    }

    public String accumulate(String numSerie, int numFactura) {
        log.info("Fetching transaction: " + numSerie + " " + numFactura + "...");

        if (this.tokenResponse == null || !this.tokenResponse.getIsTokenExpired()) {
            log.info("Sending token request...");
            this.tokenResponse = this.sendTokenRequest();
            log.info("Token request sent successfully");
        }

        FacturasVentaCamposLibres transaction = this.facturasVentaCamposLibresRepository.findByNumFacturaAndNumSerie(numFactura, numSerie);
        FacturasVenta factura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(numFactura, numSerie);
        int var10001 = factura.getNumFactura();
        log.info("Fetched factura: " + var10001 + " " + factura.getNumSerie());
        ClientesCamposLibres cliente = this.clienteCamposLibresRepository.findByCodCliente(factura.getCodCliente());
        AccountResponse accountResponse = this.processShortBalance(cliente, factura);

        try {
            this.process(transaction, factura, accountResponse, String.valueOf(cliente.getCodCliente()), cliente.getTipoDeDocumento());
        } catch (Exception var7) {
            log.error("Error al acumular puntos: " + var7.getMessage());
            return "Error al acumular puntos: " + var7.getMessage();

        }
        return "Acumulacion exitosa";
    }

    private AccountResponse processShortBalance(ClientesCamposLibres cliente, FacturasVenta factura ){
        String documentNo = this.clientesRepository.findByCodCliente(factura.getCodCliente()).getDocumentNo();
        String documentType = cliente.getTipoDeDocumento();
        if (documentType.contains("13")) {
            documentType = "2";
        } else {
            documentType = "3";
        }
        return this.sendShortBalanceRequest(documentNo, documentType, this.tokenResponse);

    }
    private void process(FacturasVentaCamposLibres transaction, FacturasVenta factura,
                         AccountResponse accountResponse, String documentNo, String documentType) {
        AccumulationResponse accumulationResponse;
        if (accountResponse == null) {
            log.info("Error al obtener el balance del cliente");
            transaction.setPuntosAcumulados(0);
            transaction.setFechaAcumulacionPuntos(LocalDateTime.of(1899, 1, 1, 0, 0));
            this.facturasVentaCamposLibresRepository.save(transaction);
        } else {
            try {
                 accumulationResponse = this.sendAccumulationRequest(transaction, accountResponse, factura, documentNo, documentType);
            }
            catch (Exception e){
                try{
                    log.info("Reintentando acumulacion...");
                     accumulationResponse = this.sendAccumulationRequest(transaction, accountResponse, factura, documentNo, documentType);
                }
                catch (Exception ex){
                    throw ex;
                }
            }
            if (accumulationResponse != null) {
                log.info("Accumulation request sent successfully");
                transaction.setPuntosAcumulados(accumulationResponse.getMainBalance().getPointsEarned());
            } else {
                transaction.setPuntosAcumulados(0);
                transaction.setFechaAcumulacionPuntos(LocalDateTime.of(1899, 1, 1, 0, 0));
            }

            this.facturasVentaCamposLibresRepository.save(transaction);
            log.info("Transaction saved successfully");
        }

    }

    public String cancellation(FacturasVenta factura, TransactionIdentifier originalTransactionId, FacturasVentaCamposLibres transaction) {
        if (this.tokenResponse == null || !this.tokenResponse.getIsTokenExpired()) {
            log.info("Sending token request...");
            this.tokenResponse = this.sendTokenRequest();
            log.info("Token request sent successfully");
        }

        TransactionIdentifier newTransactionId = this.createTransactionIdentifier(factura);
        String newJson = this.buildCancellationJsonRequest(originalTransactionId, newTransactionId);

        try {
            log.info("Sending cancellation request for transaction: " + originalTransactionId);
            log.info(this.sendCancellationRequest(newJson).toString());
        } catch (Exception var6) {
            log.error("Error al cancelar puntos: " + var6.getMessage());
                log.info("Reintentando cancelacion puntos...");
                try {
                    log.info(this.sendCancellationRequest(newJson).toString());
                }
                catch (Exception e) {
                    log.error("Error al cancelar puntos: " + var6.getMessage() + " - No se volverá a intentar.");
                    return "Error al cancelar puntos, contacte con Puntos Colombia";
                }
            }

        transaction.setFechaAcumulacionPuntos(LocalDateTime.of(1899, 1, 1, 0, 0));
        transaction.setPuntosAcumulados(transaction.getPuntosAcumulados() * -1);

        this.facturasVentaCamposLibresRepository.save(transaction);
        log.info("Cancellation request sent successfully");
        return "Cancelacion exitosa";
    }

    public String sendCancellation(String numSerie, int numAlbaran) {
        this.sendTokenRequest();
        log.info("Fetching transaction: " + numSerie + " " + numAlbaran + "...");
        AlbVentaLin devolucion = this.albVentaLinRepository.findByNumAlbaranAndNumSerie(numAlbaran, numSerie);
        String numSerieFactura = devolucion.getNumSerieFactura();
        numSerieFactura = numSerieFactura.substring(1);
        String[] parts = numSerieFactura.split("-");
        String orderNumSerie = parts[0];
        int orderNumFactura = Integer.parseInt(parts[1]);
        AlbVentaCab albVentaCab = this.albVentaCabRepository.findByNumSerieAndNumAlbaran(orderNumSerie, orderNumFactura);
        int originalNumFac = albVentaCab.getNumFactura();
        log.info("Orden a devolver: " + orderNumSerie + " " + originalNumFac);
        FacturasVentaCamposLibres transaction = this.facturasVentaCamposLibresRepository.findByNumFacturaAndNumSerie(originalNumFac, orderNumSerie);
        FacturasVenta factura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(originalNumFac, orderNumSerie);
        log.info("Fetched.");
        log.info("Creating transactionId...: ");
        TransactionIdentifier oldTransactionId = this.createTransactionIdentifier(factura, transaction.getFechaAcumulacionPuntos());
        return this.cancellation(factura, oldTransactionId, transaction);
    }

    public TokenResponse sendTokenRequest() {
        TokenResponse tokenResponse = null;
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("CA-CHANNEL", this.caChannel);
        headers.set("CA-PARTNERCODE", this.partnerCode);
        headers.set("X-REMOTE-IP", this.xRemoteIp);
        String requestBody = "partnerCode=" + this.partnerCode;
        String requesturl = this.url + "/auth/oauth/" + this.versionToken + "/token?grant_type=" + this.grantType + "&client_secret=" + this.clientSecret + "&client_id=" + this.clientId;
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(requesturl, HttpMethod.POST, requestEntity, TokenResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                tokenResponse = response.getBody();

                assert tokenResponse != null;

                log.info("Token: " + tokenResponse.getAccessToken());
                log.info("Token type: " + tokenResponse.getTokenType());
                log.info("Expires in: " + tokenResponse.getExpiresIn());
                log.info("Scope: " + tokenResponse.getScope());
                tokenResponse.setExpiration();
                log.info("Expires at: " + tokenResponse.getExpiresAt());
            }

            return tokenResponse;
        } catch (HttpServerErrorException | HttpClientErrorException var8) {
            log.error(var8.getMessage());
            throw var8;
        }
    }

    public AccountResponse sendShortBalanceRequest(String documentNo, String documentType, TokenResponse tokenResponse) {
        AccountResponse accountResponse = null;

        try {
            RestTemplate restTemplate = buildRestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenResponse.getAccessToken());
            headers.set("CA-CHANNEL", this.caChannel);
            headers.set("X-REMOTE-IP", this.xRemoteIp);
            log.info("Sending shortBalance request to: " + this.url + "/pos-management/" + this.versionAccount + "/accounts/" + documentNo + "/shortBalance");
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.url + "/pos-management/" + this.versionAccount + "/accounts/" + documentNo + "/shortBalance").queryParam("partnerCode", this.partnerCode).queryParam("LocationCode", this.locationCode).queryParam("documentType", documentType);
           

            ResponseEntity<AccountResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), AccountResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                accountResponse = response.getBody();

                assert accountResponse != null;

                log.info(accountResponse.toString());
            }
        } catch (HttpServerErrorException | HttpClientErrorException var11) {
            log.error(var11.getMessage());
        }

        return accountResponse;
    }

    public AccumulationResponse sendAccumulationRequest(FacturasVentaCamposLibres transaction, AccountResponse accountResponse, FacturasVenta factura, String documentNo, String documentType) {
        AccumulationResponse accumulationResponse;
        if (accountResponse.getAllowAccrual()) {
            TransactionIdentifier transactionIdentifier = this.createTransactionIdentifier(factura);
            String jsonRequest = this.buildJsonRequest(factura, documentNo, documentType, transactionIdentifier);

            RestTemplate restTemplate = buildRestTemplate();

            String requestUrl = this.url + "/pos-management/" + this.versionAccumulation + "/transactions/processsale";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(this.tokenResponse.getAccessToken());
            headers.set("CA-CHANNEL", this.caChannel);
            headers.set("X-REMOTE-IP", this.xRemoteIp);

            try {
                ResponseEntity<AccumulationResponse> response = restTemplate
                        .exchange(requestUrl, HttpMethod.POST, new HttpEntity<>(jsonRequest, headers), AccumulationResponse.class);
                response.getBody();
                accumulationResponse = response.getBody();
                log.info("Accumulation request sent successfully");
                transaction.setFechaAcumulacionPuntos(LocalDateTime.parse(transactionIdentifier.getTransactionDate()));
                this.facturasVentaCamposLibresRepository.save(transaction);
                return accumulationResponse;
            } catch (HttpClientErrorException var13) {
                String var10001 = transaction.getNumSerie();
                log.error("Error in order: " + var10001 + "-" + transaction.getNumFactura() + ": " + var13.getMessage());
                transaction.setMensajePuntos(var13.getMessage());
                this.facturasVentaCamposLibresRepository.save(transaction);
                throw var13;
            } catch (TimeOutException | HttpServerErrorException var14) {
                this.cancellation(factura, transactionIdentifier, transaction);
                log.error(var14.getMessage());
                transaction.setMensajePuntos(var14.getMessage());
                this.facturasVentaCamposLibresRepository.save(transaction);
                throw var14;
            }
        } else {
            log.error("La cuenta no permite acumulación");
            transaction.setMensajePuntos("La cuenta no permite acumulación");
            this.facturasVentaCamposLibresRepository.save(transaction);
            return null;
        }
    }

    public CancelationResponse sendCancellationRequest(String newJson) {
        RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(13L)).setReadTimeout(Duration.ofSeconds(13L)).build();
        String requestUrl = this.url + "/pos-management/" + this.versionCancellation + "/transactions/cancel";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.tokenResponse.getAccessToken());
        headers.set("CA-CHANNEL", this.caChannel);
        headers.set("X-REMOTE-IP", this.xRemoteIp);

        try {
            ResponseEntity<CancelationResponse> response = restTemplate
                    .exchange(requestUrl, HttpMethod.POST, new HttpEntity<>(newJson, headers), CancelationResponse.class);
            log.info("Cancelation request sent successfully");
            return response.getBody();
        } catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException var6) {
            log.error(var6.getMessage());
            throw var6;
        }

    }

    @SneakyThrows
    public String buildJsonRequest(FacturasVenta factura, String documentNo, String documentType, TransactionIdentifier transactionIdentifier) {
        String numSerie = factura.getNumSerie();
        int numFactura = factura.getNumFactura();
        log.info("Building JSON request for " + numSerie + "-" + numFactura);
        log.info("transactionIdentifierList: " + transactionIdentifier);

        RequestBody requestBody = new RequestBody();
        requestBody.setDocumentNo(documentNo);
        requestBody.setDocumentType(documentType);
        requestBody.setPartnerCode(this.partnerCode);

        List<Tesoreria> tesoreria = this.tesoreriaRepository.findDistinctBySerieAndNumero(numSerie, numFactura);

        for (Tesoreria tesoreria1 : tesoreria) {
            PaymentMethod paymentMethod = this.createPaymentMethod(tesoreria1.getCodTipoPago(), tesoreria1.getImporte());
            requestBody.append(paymentMethod);
        }

        requestBody.setTransactionIdentifier(transactionIdentifier);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        log.info("JSON Built: " + jsonBody);
        return jsonBody;
    }

    @SneakyThrows
    public String buildCancellationJsonRequest(TransactionIdentifier originalTransactionId, TransactionIdentifier newTransactionId) {
        log.info("Building cancelation request");

        CancelationRequestBody requestBody = new CancelationRequestBody();
        requestBody.setPartnerCode(this.partnerCode);
        requestBody.setTransactionIdentifier(newTransactionId);
        requestBody.setOriginalTransactionIdentifier(originalTransactionId);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        log.info("JSON Built: " + jsonBody);
        return jsonBody;
    }

    private TransactionIdentifier createTransactionIdentifier(FacturasVenta factura) {
        TransactionIdentifier transactionIdentifier = new TransactionIdentifier();
        transactionIdentifier.setTerminalId(factura.getNumSerie());
        String var10001 = factura.getNumSerie();
        transactionIdentifier.setTransactionId(var10001 + "-" + factura.getNumFactura());
        transactionIdentifier.setCashierId(factura.getCodVendedor());
        transactionIdentifier.setLocationCode(this.locationCode);
        LocalDateTime date = factura.getFecha().with(LocalTime.from(factura.getHora()));
        transactionIdentifier.setTransactionDate(String.valueOf(date));
        transactionIdentifier.setNut(this.facturasVentaSeriesRepository.findByNumFacturaAndNumSerie(factura.getNumFactura(), factura.getNumSerie()).getNumeroFiscal());
        return transactionIdentifier;
    }

    private TransactionIdentifier createTransactionIdentifier(FacturasVenta factura, LocalDateTime date) {
        TransactionIdentifier transactionIdentifier = new TransactionIdentifier();
        transactionIdentifier.setTerminalId(factura.getNumSerie());
        String var10001 = factura.getNumSerie();
        transactionIdentifier.setTransactionId(var10001 + "-" + factura.getNumFactura());
        transactionIdentifier.setCashierId(factura.getCodVendedor());
        transactionIdentifier.setLocationCode(this.locationCode);
        transactionIdentifier.setTransactionDate(date.toString());
        transactionIdentifier.setNut(this.facturasVentaSeriesRepository.findByNumFacturaAndNumSerie(factura.getNumFactura(), factura.getNumSerie()).getNumeroFiscal());
        return transactionIdentifier;
    }

    private PaymentMethod createPaymentMethod(String code, double amount) {

        PaymentMethod paymentMethod = new PaymentMethod();
        if (code.equals("1")) {
            code = "PC";
        } else if (code.equals("3")) {
            code = "CC";
        } else {
            code = "UN";
        }

        paymentMethod.setCode(code);
        paymentMethod.setAmount(amount);
        return paymentMethod;
    }

    private RestTemplate buildRestTemplate() {
        return (new RestTemplateBuilder())
                .setConnectTimeout(Duration.ofSeconds(13))
                .setReadTimeout(Duration.ofSeconds(13))
                .errorHandler(new TimeoutErrorHandler())
                .build();
    }
    private static class TimeoutErrorHandler extends DefaultResponseErrorHandler {
        private TimeoutErrorHandler() {
        }

        public void handleError(ClientHttpResponse response) throws IOException {
            if (response.getStatusCode().value() == 422 || response.getStatusCode().value() == 400) {
                throw new CommonBusinessErrorException();
            }
            else if (response.getStatusCode().value() == 408) {
                throw new TimeOutException();
            } else {
                log.info(response.getStatusCode().value() + " " + response.getBody());
                throw new RuntimeException("Error al acumular puntos, contactese con Puntos Colombia");
            }
        }
    }
}

