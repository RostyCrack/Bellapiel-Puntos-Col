package com.hardtech.bellapielpuntoscol.configuration;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import javax.net.ssl.SSLException;
import java.time.Duration;

/**
 * Web client configuration
 */
@Configuration
@Slf4j
public class WebClientConfiguration {
  @Bean("createLinkWebClient")
  public WebClient createLinkWebClient() throws SSLException {
    var sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();
    var httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

    return WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(httpClient))
      .exchangeStrategies(ExchangeStrategies
        .builder()
        .codecs(codecs -> codecs
          .defaultCodecs()
          .maxInMemorySize(60 * 1000 * 1024))
        .build())
      .filter(this.logRequest())
      .filter(this.retryFilter())
      .build();
  }

  /**
   * Log request
   *
   * @return An exchange filter function
   */
  private ExchangeFilterFunction logRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
      log.debug("Request: [{}] {}", clientRequest.method(), clientRequest.url());
      clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{}={}", name, value)));
      return Mono.just(clientRequest);
    });
  }

  /**
   * Retry filter
   *
   * @return An exchange filter function
   */
  private ExchangeFilterFunction retryFilter() {
    return (request, next) -> next.exchange(request)
      .timeout(Duration.ofSeconds(20))
      .retryWhen(Retry.backoff(3,
          Duration.ofSeconds(30))
        .doAfterRetry(retrySignal -> log.info("#{} - Retrying request for generate payment link in URL: {}",
          retrySignal.totalRetriesInARow(), request.url())));
  }
}
