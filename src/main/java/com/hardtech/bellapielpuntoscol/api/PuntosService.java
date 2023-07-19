package com.hardtech.bellapielpuntoscol.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hardtech.bellapielpuntoscol.context.domain.account.AccountResponse;
import com.hardtech.bellapielpuntoscol.context.domain.accumulation.AccumulationResponse;
import com.hardtech.bellapielpuntoscol.context.domain.accumulation.RequestBody;
import com.hardtech.bellapielpuntoscol.context.domain.accumulation.exceptions.CommonBusinessErrorException;
import com.hardtech.bellapielpuntoscol.context.domain.accumulation.exceptions.DuplicateTransactionException;
import com.hardtech.bellapielpuntoscol.context.domain.cancelation.CancelationRequestBody;
import com.hardtech.bellapielpuntoscol.context.domain.cancelation.CancelationResponse;
import com.hardtech.bellapielpuntoscol.context.domain.cancelation.exceptions.TimeOutException;
import com.hardtech.bellapielpuntoscol.context.domain.shared.DocPrinted;
import com.hardtech.bellapielpuntoscol.context.domain.token.TokenResponse;
import com.hardtech.bellapielpuntoscol.infrastructure.*;
import lombok.SneakyThrows;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
          try{
              this.tokenResponse = this.sendTokenRequest();
          }catch (HttpServerErrorException response) {
              return ("Error en el servidor de Puntos Colombia");
          }catch (HttpClientErrorException response) {
              return ("Credenciales invalidas, no se puede recibir token");
          }
          log.info("Token request sent successfully");
      }
      log.info("Token: " + this.tokenResponse.getIsTokenExpired() + " " + this.tokenResponse.getExpiresAt());

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
          return "Error al acumular puntos: no estas registrado en Puntos Colombia";
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

      if (this.tokenResponse == null || !this.tokenResponse.getIsTokenExpired()) {
          log.info("Sending token request...");
          try{
              this.tokenResponse = this.sendTokenRequest();
          }catch (HttpServerErrorException response) {
              throw new RuntimeException ("Error en el servidor de Puntos Colombia");
          }catch (HttpClientErrorException response) {
              throw new RuntimeException ("Credenciales invalidas, no se puede recibir token");
          }
      }
      AccumulationResponse accumulationResponse;
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
          }
          catch (Exception e1){
              log.info("Reintentando cancelacion...");
              try{
                  this.sendCancellationRequest(this.buildCancellationJsonRequest(transactionIdentifier, newTransactionIdentifier));
                  transactionIdentifier = this.createTransactionIdentifier(factura, LocalDateTime.now());
                  log.info("Reintentando acumulacion");
                  accumulationResponse = this.sendAccumulationRequest(transaction, accountResponse, factura, documentNo, documentType, transactionIdentifier);
              }
              catch (Exception e2){
                  transaction.setPuntosAcumulados(0);
                  transaction.setFechaAcumulacionPuntos(LocalDateTime.of(1899, 1, 1, 0, 0));
                  transaction.setMensajePuntos(e.getMessage());
                  log.error("Error cancelando. No se volvera a intentar");
                  throw e;
              }
          }
      }
      transaction.setPuntosAcumulados(accumulationResponse.getMainBalance().getPointsEarned());
      log.info("Puntos acumulados: " + accumulationResponse.getMainBalance().getPointsEarned());

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
      AccumulationResponse accumulationResponse;
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
              this.flujoCancellation( transactionIdentifier, transaction, factura);
              transaction.setMensajePuntos(var14.getMessage());
              this.facturasVentaCamposLibresRepository.save(transaction);
              throw var14;
          } catch (HttpClientErrorException response) {
              transaction.setMensajePuntos(response.getMessage());
              this.facturasVentaCamposLibresRepository.save(transaction);

              if (response.getStatusCode().value() == 408) {
                  throw new TimeOutException();
              } else if(response.getMessage().contains("COMMON_BUSINESS_ERROR")) {
                  throw new CommonBusinessErrorException();
              }else if(response.getMessage().contains("POS_DUPLICATE_TRANSACTION")) {
                  throw new DuplicateTransactionException();
              }
              else {
                  log.error(response.getMessage());
                  throw new RuntimeException("Contactese con Puntos Colombia");
              }
          }
      } else {
          transaction.setMensajePuntos("Cuenta no permitada para acumular puntos");
          this.facturasVentaCamposLibresRepository.save(transaction);
          throw new RuntimeException("Contactese con Puntos Colombia");
      }
  }

    /**
     * Metodo que se encarga del flujo deuna cancelacion automatica
     * @param originalTransactionId Identificador de la transaccion original
     * @param transaction Transaccion a cancelar
     * @return Respuesta de la peticion de cancelacion
     */
  public String flujoCancellation(TransactionIdentifier originalTransactionId, FacturasVentaCamposLibres transaction, FacturasVenta facturaDevolucion) {

      if (this.tokenResponse == null || !this.tokenResponse.getIsTokenExpired()) {
          log.info("Sending token request...");
          try{
              this.tokenResponse = this.sendTokenRequest();
          }catch (HttpServerErrorException response) {
              return ("Error en el servidor de Puntos Colombia");
          }catch (HttpClientErrorException response) {
              return ("Credenciales invalidas, no se puede recibir token");
          }
      }
      LocalDateTime now = LocalDateTime.now();
      TransactionIdentifier newTransactionId = this.createTransactionIdentifier(facturaDevolucion, now);
      String newJson = this.buildCancellationJsonRequest(originalTransactionId, newTransactionId);
      CancelationResponse cancellationResponse;
      try {
          log.info("Sending Cancellation request for transaction: " + originalTransactionId);
          cancellationResponse = this.sendCancellationRequest(newJson);
          log.info("Cancellation request sent successfully");
          log.info("Approbation number: " + cancellationResponse.getApprobationNumber());

      }catch (HttpServerErrorException | TimeOutException e){
            log.error("Error al cancelar por parte del servidor...");
            transaction.setPuntosAcumulados(0);
            return "Error al cancelar puntos.";
      }catch (HttpClientErrorException.NotFound e){
          log.error("La orden no existe en Puntos Colombia...");
          transaction.setPuntosAcumulados(0);
          return "Orden no existente en Puntos Colombia.";
      }catch (HttpClientErrorException e){
          if(e.getMessage().contains("POS_DUPLICATE_TRANSACTION")) {
              return "La cancelacion ya fue procesada anteriormente.";
          }
          else if(e.getMessage().contains("POS_WRONG_TRANSACTION_STATUS")) {
              return "La cancelacion ya fue procesada anteriormente.";
          }
          else{
              log.error("Error: " + e.getMessage());
              log.info("Reintentando cancelacion puntos...");
              try {
                  cancellationResponse = this.sendCancellationRequest(newJson);
                  log.info("Cancellation request sent successfully");
                  log.info("Approbation number: " + cancellationResponse.getApprobationNumber());}
              catch (Exception ex) {
                  log.error("Error al cancelar puntos, no se volvera a intentar. " + e.getMessage());
                  transaction.setPuntosAcumulados(0);
                  return "Error al cancelar puntos";
              }
          }
      }
      transaction.setFechaAcumulacionPuntos(now);
      this.facturasVentaCamposLibresRepository.save(transaction);
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
      FacturasVentaCamposLibres transactionDevolucion = this.facturasVentaCamposLibresRepository.findByNumFacturaAndNumSerie(numAlbaran, numSerie);

      transactionDevolucion.setPuntosAcumulados(transaction.getPuntosAcumulados()*-1);
      facturasVentaCamposLibresRepository.save(transactionDevolucion);
      /*
       * Se realiza la cancelacion
       */
      FacturasVenta factura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(originalNumFac, orderNumSerie);
      FacturasVenta devolucionFactura = this.facturasVentaRepository.findByNumFacturaAndNumSerie(numAlbaran, numSerie);
      log.info("Fetched.");
      log.info("Creating transactionId...: ");
      TransactionIdentifier oldTransactionId = this.createTransactionIdentifier(factura, transaction.getFechaAcumulacionPuntos());
      return this.flujoCancellation(oldTransactionId, transactionDevolucion, devolucionFactura);
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
        String p12Password = "212738A9-9F64-434E-8D5C-5E0C341E2C60";
        String appDirectory = System.getProperty("user.dir");
        String p12FilePath = appDirectory + "/src/main/resources/pcobella piel-p384sv_key.p12";

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream inputStream = new FileInputStream(p12FilePath)) {
                keyStore.load(inputStream, p12Password.toCharArray());
            }

            String alias = keyStore.aliases().nextElement();
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, p12Password.toCharArray());

            keyStore.setKeyEntry("privateKeyAlias", privateKey, p12Password.toCharArray(), new X509Certificate[]{certificate});

            SSLContext sslContext = SSLContextBuilder.create()
                    .loadKeyMaterial(keyStore, p12Password.toCharArray())
                    .loadTrustMaterial(new TrustSelfSignedStrategy())
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

            RestTemplate restTemplate = (new RestTemplateBuilder())
                    .setConnectTimeout(Duration.ofSeconds(13))
                    .setReadTimeout(Duration.ofSeconds(13))
                    .build();

            restTemplate.setRequestFactory(requestFactory);

            return restTemplate;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build the custom RestTemplate with SSL.", e);
        }
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

    @SneakyThrows
    public Map<String, String> readXML() {
        Map<String, String> facturaMap = new HashMap<>();
        JAXBContext jaxbContext = JAXBContext.newInstance(DocPrinted.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // Get the application's directory
        String appDirectory = System.getProperty("user.dir");


        // Construct the XML file path
        String xmlFilePath = Paths.get(appDirectory, "acumular-puntos.xml").toString();
        log.info("Scanning file: " + xmlFilePath);
        // Create a File object for the XML file
        File xmlFile = new File(xmlFilePath);

        // Unmarshal the XML file into an instance of DocPrinted class
        DocPrinted docPrinted = (DocPrinted) unmarshaller.unmarshal(xmlFile);

        log.info("numSerie: "+ docPrinted.getSerie() +" numFactura: "+ docPrinted.getNumero());
        facturaMap.put("numSerie", docPrinted.getSerie());
        facturaMap.put("numFactura", String.valueOf(docPrinted.getNumero()));

        return facturaMap;
    }
}

