package se.jensen.charitha.cloudstore.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.jensen.charitha.cloudstore.model.Product;

@Component
public class ProductClient {

    private final RestTemplate restClient;
    private final String fakestoreServiceUrl;

    public ProductClient(@Value("${fakestore.service.url}") String fakestoreServiceUrl) {
        this.restClient = new RestTemplate();
        this.fakestoreServiceUrl = fakestoreServiceUrl;
    }

    public RestTemplate getRestClient() {
        return restClient;
    }

    public String getFakestoreServiceUrl() {
        return fakestoreServiceUrl;
    }

    public Product[] fetchProducts() {
        return restClient.getForObject(fakestoreServiceUrl, Product[].class);
    }
}
