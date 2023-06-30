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
import java.net.http.HttpTimeoutException;
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

  /**
   * PASO 1:
   * Metodo que se encarga de enviar la peticion de token a Puntos Colombia.
   * @return TokenResponse
   */
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
              tokenResponse.setExpiration();
          }

          return tokenResponse;
      } catch (HttpServerErrorException | HttpClientErrorException var8) {
          log.error(var8.getMessage());
          throw var8;
      }
  }

  /**
   * PASO 2:
   * Metodo que se encarga de enviar la peticion de acumulacion de puntos a Puntos Colombia.
   * @param documentNo Numero de documento del cliente
   * @param documentType Tipo de documento del cliente
   * @return AccountResponse
   */
  public AccountResponse sendShortBalanceRequest(String documentNo, String documentType) {
      AccountResponse accountResponse = null;
      RestTemplate restTemplate = buildRestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(tokenResponse.getAccessToken());
      headers.set("CA-CHANNEL", this.caChannel);
      headers.set("X-REMOTE-IP", this.xRemoteIp);

      UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.url + "/pos-management/" + this.versionAccount + "/accounts/" + documentNo + "/shortBalance").queryParam("partnerCode", this.partnerCode).queryParam("LocationCode", this.locationCode).queryParam("documentType", documentType);

      try {
          ResponseEntity<AccountResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), AccountResponse.class);
          if (response.getStatusCode().is2xxSuccessful()) {
              accountResponse = response.getBody();
              assert accountResponse != null;
              log.info(accountResponse.toString());
          }
      } catch (Exception var11) {
          log.error(var11.getMessage());
          throw new RuntimeException("Intente nuevamente");
      }

      return accountResponse;
  }


    /**
     * PASO 3: Metodo que se encarga de acumular una transaccion en Puntos Colombia.
     * @param numSerie Numero de serie de la factura
     * @param numFactura Numero de factura
     * @return Respuesta puntos colombia
     */
  public String accumulate(String numSerie, int numFactura) {

      log.info("Fetching transaction: " + numSerie + " " + numFactura + "...");
      /*
         * PASO 1: Si el token no existe o esta expirado, se envia una nueva peticion de token
       */
      if (this.tokenResponse == null || !this.tokenResponse.getIsTokenExpired()) {
          log.info("Sending token request...");
          this.tokenResponse = this.sendTokenRequest();
          log.info("Token request sent successfully");
      }

      /*
      Preparacion paso 2
       */
      FacturasVentaCamposLibres transaction = this.facturasVentaCamposLibresRepository.findByNumFacturaAndNumSerie(numFactura, numSerie);
      FacturasVenta factura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(numFactura, numSerie);

      log.info("Fetched factura: " + factura.getNumSerie() + " " + factura.getNumFactura() );

      ClientesCamposLibres cliente = this.clienteCamposLibresRepository.findByCodCliente(factura.getCodCliente());

      String documentNo = this.clientesRepository.findByCodCliente(factura.getCodCliente()).getDocumentNo();
      String documentType = cliente.getTipoDeDocumento();
      if (documentType.contains("13")) {
          documentType = "2";
      } else {
          documentType = "3";
      }

     /*
        Paso 2: Se envia la peticion de cuenta a Puntos Colombia
     */
      log.info("Sending short Balance request...");
      AccountResponse accountResponse;
      try {
          accountResponse = this.sendShortBalanceRequest(documentNo, documentType);
      }catch (Exception e){
          log.error("Error al obtener el balance del cliente");
          transaction.setPuntosAcumulados(0);
          transaction.setFechaAcumulacionPuntos(LocalDateTime.of(1899, 1, 1, 0, 0));
          this.facturasVentaCamposLibresRepository.save(transaction);
          return "Error al acumular puntos: " + e.getMessage();
      }

      /*
      PASO 3: Se envia la peticion de acumulacion de puntos a Puntos Colombia
       */
      try {
          this.flujoAccumulation(transaction, factura, accountResponse, documentNo, documentType);
      } catch (TimeOutException e){
          log.error("Timeout...");
          return "Error al acumular puntos: Timeout";
      } catch (Exception var7) {
          log.error("Error al acumular puntos: " + var7.getMessage());
          return "Error al acumular puntos: " + var7.getMessage();
      }
      return "Acumulacion exitosa";
  }

    /**
     * Metodo que se encarga de realizar el flujo de acumulacion de puntos
     * @param transaction Transaccion a acumular
     * @param factura Factura asociada a la transaccion
     * @param accountResponse Respuesta de la peticion de cuenta
     * @param documentNo Numero de documento del cliente
     * @param documentType Tipo de documento del cliente
     */
  private void flujoAccumulation(FacturasVentaCamposLibres transaction, FacturasVenta factura,
                                 AccountResponse accountResponse, String documentNo, String documentType) {
      AccumulationResponse accumulationResponse = null;
      TransactionIdentifier transactionIdentifier = this.createTransactionIdentifier(factura);
      try {
          accumulationResponse = this.sendAccumulationRequest(transaction, accountResponse, factura, documentNo, documentType, transactionIdentifier);
      }
      catch (HttpServerErrorException | TimeOutException | CommonBusinessErrorException e){
          throw e;
      }
      catch (Exception e){
          log.info("Error acumulacion. Cancelando...");
          TransactionIdentifier newTransactionIdentifier = this.createTransactionIdentifier(factura, LocalDateTime.now());
          try {
              this.sendCancellationRequest(this.buildCancellationJsonRequest(transactionIdentifier, newTransactionIdentifier));
              transactionIdentifier = this.createTransactionIdentifier(factura, LocalDateTime.now());
              log.info("Reintentando acumulacion");
              accumulationResponse = this.sendAccumulationRequest(transaction, accountResponse, factura, documentNo, documentType, transactionIdentifier);
              transaction.setPuntosAcumulados(accumulationResponse.getMainBalance().getPointsEarned());
          }
          catch (Exception e1){
              log.info("Reintentando cancelacion...");
              try{
                  this.sendCancellationRequest(this.buildCancellationJsonRequest(transactionIdentifier, newTransactionIdentifier));
                  transactionIdentifier = this.createTransactionIdentifier(factura, LocalDateTime.now());
                  log.info("Reintentando acumulacion");
                  accumulationResponse = this.sendAccumulationRequest(transaction, accountResponse, factura, documentNo, documentType, transactionIdentifier);
                  transaction.setPuntosAcumulados(accumulationResponse.getMainBalance().getPointsEarned());
              }
              catch (Exception e2){
                  transaction.setPuntosAcumulados(0);
                  transaction.setFechaAcumulacionPuntos(LocalDateTime.of(1899, 1, 1, 0, 0));
                  log.error("Error cancelando. No se volvera a intentar");
                  throw e;
              }
          }
      }

      if(accumulationResponse != null){
          log.info("Puntos acumulados: " + accumulationResponse.getMainBalance().getPointsEarned());
      }

      this.facturasVentaCamposLibresRepository.save(transaction);
  }

    /**
     * Metodo que se encarga de enviar la peticion de acumulacion de puntos a Puntos Colombia
     * @param transaction Transaccion a acumular
     * @param accountResponse Respuesta de la peticion de cuenta
     * @param factura Factura asociada a la transaccion
     * @param documentNo Numero de documento del cliente
     * @param documentType Tipo de documento del cliente
     * @param transactionIdentifier Identificador de la transaccion
     * @return Respuesta de la peticion de acumulacion
     */

  public AccumulationResponse sendAccumulationRequest(FacturasVentaCamposLibres transaction, AccountResponse accountResponse, FacturasVenta factura, String documentNo, String documentType, TransactionIdentifier transactionIdentifier) {
      AccumulationResponse accumulationResponse = null;
      if (accountResponse.getAllowAccrual()) {
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
              transaction.setFechaAcumulacionPuntos(LocalDateTime.parse(transactionIdentifier.getTransactionDate()));
              this.facturasVentaCamposLibresRepository.save(transaction);
              return accumulationResponse;
          } catch (HttpServerErrorException var14) {
              this.flujoCancellation(factura, transactionIdentifier, transaction);
              transaction.setMensajePuntos(var14.getMessage());
              this.facturasVentaCamposLibresRepository.save(transaction);
              throw var14;
          } catch (HttpClientErrorException response) {
              transaction.setMensajePuntos(response.getMessage());
              this.facturasVentaCamposLibresRepository.save(transaction);

              if (response.getStatusCode().value() == 408) {
                  throw new TimeOutException();
              } else if(response.getStatusCode().value() == 422) {
                  throw new CommonBusinessErrorException();
              }
              else {
                  log.error(response.getMessage());
                  throw new RuntimeException("Error al acumular puntos, contactese con Puntos Colombia");
              }
          }
      } else {
          transaction.setMensajePuntos("No estás registrado, descarga la APP de Puntos Colombia y regístrate");
          this.facturasVentaCamposLibresRepository.save(transaction);
          throw new RuntimeException("No estás registrado, descarga la APP de Puntos Colombia y regístrate");
      }
  }

    /**
     * Metodo que se encarga del flujo deuna cancelacion automatica
     * @param factura Factura asociada a la transaccion
     * @param originalTransactionId Identificador de la transaccion original
     * @param transaction Transaccion a cancelar
     * @return Respuesta de la peticion de cancelacion
     */
  public String flujoCancellation(FacturasVenta factura, TransactionIdentifier originalTransactionId, FacturasVentaCamposLibres transaction) {

      if (this.tokenResponse == null || !this.tokenResponse.getIsTokenExpired()) {
          log.info("Sending token request...");
          this.tokenResponse = this.sendTokenRequest();
          log.info("Token request sent successfully");
      }

      TransactionIdentifier newTransactionId = this.createTransactionIdentifier(factura);
      String newJson = this.buildCancellationJsonRequest(originalTransactionId, newTransactionId);
      CancelationResponse cancellationResponse;
      try {
          log.info("Sending Cancellation request for transaction: " + originalTransactionId);
          cancellationResponse = this.sendCancellationRequest(newJson);
          log.info((cancellationResponse).toString());

      }catch (HttpServerErrorException | TimeOutException e){
            log.error("Error al cancelar por parte del serivdor...");
            return "Error al cancelar puntos.";
      }
      catch (Exception var6) {
          log.error("Error al cancelar puntos: " + var6.getMessage());
          log.info("Reintentando cancelacion puntos...");
          try {
              cancellationResponse = this.sendCancellationRequest(newJson);
              log.info(cancellationResponse.toString());
          }
          catch (Exception e) {
              log.error("Error al cancelar puntos, no se volvera a intentar: " + var6.getMessage());
              return "Error al cancelar puntos";
          }
      }finally {
          transaction.setFechaAcumulacionPuntos(LocalDateTime.of(1899, 1, 1, 0, 0));
          transaction.setPuntosAcumulados(transaction.getPuntosAcumulados() * -1);
          this.facturasVentaCamposLibresRepository.save(transaction);
          log.info("Cancellation request sent successfully");
      }
      return "Cancelacion exitosa";
  }

    /**
     * Metodo que se encarga de preparar la peticion de cancelacion manual de puntos a Puntos Colombia
     * @param numSerie Numero de serie de la factura
     * @param numAlbaran Numero de devolucion de la factura
     */
  public String cancellation(String numSerie, int numAlbaran) {
      log.info("Fetching transaction: " + numSerie + " " + numAlbaran + "...");

      /*
        * Se obtiene la factura original a partir de la devolución
       */
      AlbVentaLin devolucion = this.albVentaLinRepository.findByNumAlbaranAndNumSerie(numAlbaran, numSerie);
      String numSerieFactura = devolucion.getNumSerieFactura();
      numSerieFactura = numSerieFactura.substring(1);
      String[] parts = numSerieFactura.split("-");
      String orderNumSerie = parts[0];
      int orderNumFactura = Integer.parseInt(parts[1]);
      AlbVentaCab albVentaCab = this.albVentaCabRepository.findByNumSerieAndNumAlbaran(orderNumSerie, orderNumFactura);
      int originalNumFac = albVentaCab.getNumFactura();
      log.info("Orden a devolver: " + orderNumSerie + " " + originalNumFac);

      /*
          * Se populan los campos de la devolucion
       */
      FacturasVentaCamposLibres transaction = this.facturasVentaCamposLibresRepository.findByNumFacturaAndNumSerie(originalNumFac, orderNumSerie);
      FacturasVentaCamposLibres devolucionCamposLibres = this.facturasVentaCamposLibresRepository.findByNumFacturaAndNumSerie(numAlbaran, numSerie);

      devolucionCamposLibres.setPuntosAcumulados(transaction.getPuntosAcumulados() *-1);
      devolucionCamposLibres.setMensajePuntos("Devolucion");
      devolucionCamposLibres.setFechaAcumulacionPuntos(LocalDateTime.now());
      facturasVentaCamposLibresRepository.save(devolucionCamposLibres);

      /*
       * Se realiza la cancelacion
       */
      FacturasVenta factura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(originalNumFac, orderNumSerie);
      log.info("Fetched.");
      log.info("Creating transactionId...: ");
      TransactionIdentifier oldTransactionId = this.createTransactionIdentifier(factura, transaction.getFechaAcumulacionPuntos());
      return this.flujoCancellation(factura, oldTransactionId, transaction);
  }


    /**
     * Metodo que se encarga de enviar la peticion de cancelacion de puntos a Puntos Colombia
     * @param newJson Json de la peticion de cancelacion
     * @return Respuesta de la peticion de cancelacion
     */
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
          return response.getBody();
      } catch (ResourceAccessException | HttpServerErrorException | HttpClientErrorException var6) {
          log.error(var6.getMessage());
          throw var6;
      }

  }

    /**
     * Metodo que se encarga de construir el json de la peticion de acumulacion de puntos a Puntos Colombia
     * @param factura Factura a la que se le va a realizar la acumulacion
     * @param documentNo Numero de documento de la factura
     * @param documentType Tipo de documento de la factura
     * @param transactionIdentifier Identificador de la transaccion
     * @return Json de la peticion
     */
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

  /**
   * Metodo que se encarga de construir el json de la peticion de cancelacion de puntos a Puntos Colombia
   * @param originalTransactionId Identificador de la transaccion original
   * @param newTransactionId Identificador de la nueva transaccion
   */
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

    /**
     * Metodo que se encarga de crear el Transaction Identifier
     * @param factura Factura a la que se le va a realizar la acumulacion o cancelacion
     * @return Transaction Identifier
     */
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

    /**
     * Metodo que se encarga de crear el Transaction Identifier con una fecha especifica
     * @param factura Factura a la que se le va a realizar la acumulacion o cancelacion
     * @param date Fecha de la transaccion
     * @return Transaction Identifier
     */
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

    /**
     * Metodo que se encarga de crear el Payment Method
     * @param code Codigo del tipo de pago
     * @param amount Monto del tipo de pago
     * @return Payment Method
     */
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

    /**
     * Metodo que se encarga de crear la RestTemplate con timeoute de 13 segundos
     * @return RestTemplate
     */
  private RestTemplate buildRestTemplate() {
      return (new RestTemplateBuilder())
              .setConnectTimeout(Duration.ofSeconds(13))
              .setReadTimeout(Duration.ofSeconds(13))
              .build();
  }

    /**
     * Metodo para acumular transacciones pendientes
     */
    public void accumulatePending() {

        LocalDateTime minDate = LocalDateTime.of(1899, 12, 30, 0, 0);
        log.info("Fetching transactions... ");
        List<FacturasVentaCamposLibres> notAccumulated = this.facturasVentaCamposLibresRepository.findByAccumulatedAfter(minDate);
        if (notAccumulated.isEmpty()) {
            log.info("No transactions to accumulate");

        } else {
            for (FacturasVentaCamposLibres transaction : notAccumulated) {
                FacturasVenta factura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(transaction.getNumFactura(), transaction.getNumSerie());
                log.info("Fetched factura: " + factura.getNumSerie() + " " +factura.getNumFactura() );
                accumulate(transaction.getNumSerie(), transaction.getNumFactura());
            }
        }

    }

}

