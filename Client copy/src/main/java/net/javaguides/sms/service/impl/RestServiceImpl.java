package net.javaguides.sms.service.impl;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import net.javaguides.sms.service.RestService;
import reactor.core.publisher.Mono;

@Service
public class RestServiceImpl implements RestService{
	private final WebClient client;

    public RestServiceImpl(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder.baseUrl("http://localhost:8080").build();
    }
    
    public Mono<String> sendMyRequest() {
    	UriSpec<RequestBodySpec> uriSpec = client.post();
    	RequestBodySpec bodySpec = uriSpec.uri("/resource");
    	
    	LinkedMultiValueMap map = new LinkedMultiValueMap();
    	map.add("key1", "value1");
    	map.add("key2", "value2");
    	RequestHeadersSpec<?> headersSpec = bodySpec.body(
    	BodyInserters.fromMultipartData(map));
    	ResponseSpec responseSpec = headersSpec.header(
    		    HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    		  .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
    		  .acceptCharset(StandardCharsets.UTF_8)
    		  .ifNoneMatch("*")
    		  .ifModifiedSince(ZonedDateTime.now())
    		  .retrieve();
    	
    	Mono<String> res = headersSpec.exchangeToMono(response -> {
    		  if (response.statusCode().equals(HttpStatus.OK)) {
    		      return response.bodyToMono(String.class);
    		  } else if (response.statusCode().is4xxClientError()) {
    		      return Mono.just("Error response");
    		  } else {
    		      return response.createException()
    		        .flatMap(Mono::error);
    		  }
    		});
    	return res;
    	
    }
}
