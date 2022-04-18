package io.github.anjeyy.api.integrationtest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class GithubRepositoryIntegrationTest {

    private final Integer outsidePort = gubifyContainer.getMappedPort(8080);

    @Container
    public static GenericContainer<?> gubifyContainer =
        new GenericContainer("anjeyy/gubify:latest")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(200));

    @Test
    void givenTop10Request_withValidParameters_willReturnResult() throws IOException, InterruptedException {
        // given
        String url = String.format("http://localhost:%d/github/repositories/top10", outsidePort);
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(uri)
                                         .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // then
        Assertions.assertThat(response)
                  .extracting(HttpResponse::statusCode)
                  .isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response)
                  .extracting(HttpResponse::body)
                  .asString()
                  .isNotNull()
                  .isNotEmpty()
                  .isNotBlank();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void givenTop10Request_withInvalidPathVariable_willReturnBadRequest(int pathVariable)
        throws IOException, InterruptedException {
        // given
        String url = String.format("http://localhost:%d/github/repositories/top%d", outsidePort, pathVariable);
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(uri)
                                         .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // then
        Assertions.assertThat(response)
                  .extracting(HttpResponse::statusCode)
                  .isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response)
                  .extracting(HttpResponse::body)
                  .asString()
                  .isNotNull()
                  .isNotEmpty()
                  .isNotBlank()
                  .isEqualTo(
                      """
                          {"http_status":"BAD_REQUEST","message":"top must be greater than or equal to 1","hint":null}""");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void givenRepositoryRequest_withInvalidLimitParameter_willReturnBadRequest(int limit)
        throws IOException, InterruptedException {
        // given
        String url = String.format("http://localhost:%d/github/repositories?limit=%d", outsidePort, limit);
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(uri)
                                         .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // then
        Assertions.assertThat(response)
                  .extracting(HttpResponse::statusCode)
                  .isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response)
                  .extracting(HttpResponse::body)
                  .asString()
                  .isNotNull()
                  .isNotEmpty()
                  .isNotBlank()
                  .isEqualTo(
                      """
                          {"http_status":"BAD_REQUEST","message":"limit must be greater than or equal to 1","hint":null}""");
    }

    @ParameterizedTest
    @ValueSource(ints = {1001, 1002, 1003})
    void givenRepositoryRequest_withInvalidOffsetParameter_willReturnBadRequest(int offset)
        throws IOException, InterruptedException {
        // given
        String url = String.format("http://localhost:%d/github/repositories?offset=%d", outsidePort, offset);
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(uri)
                                         .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // then
        Assertions.assertThat(response)
                  .extracting(HttpResponse::statusCode)
                  .isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response)
                  .extracting(HttpResponse::body)
                  .asString()
                  .isNotNull()
                  .isNotEmpty()
                  .isNotBlank()
                  .isEqualTo(
                      """
                          {"http_status":"BAD_REQUEST","message":"offset must be less than or equal to 1000","hint":null}""");
    }


    @ParameterizedTest
    @ValueSource(strings = {"2022-01-32", "2022-13-05"})
    void givenRepositoryRequest_withInvalidDateParameter_willReturnBadRequest(String startingDate)
        throws IOException, InterruptedException {
        // given
        String url = String.format("http://localhost:%d/github/repositories?starting_date=%s",
                                   outsidePort, startingDate);
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(uri)
                                         .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // then
        Assertions.assertThat(response)
                  .extracting(HttpResponse::statusCode)
                  .isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response)
                  .extracting(HttpResponse::body)
                  .asString()
                  .isNotNull()
                  .isEmpty();
    }

    @Test
    void givenRepositoryRequest_withInvalidParameterCombination_willReturnBadRequest()
        throws IOException, InterruptedException {
        // given
        LocalDate startingDate = LocalDate.now();
        String url = String.format("http://localhost:%d/github/repositories?aggregate_results=true&starting_date=%s",
                                   outsidePort, startingDate);
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(uri)
                                         .build();

        // when
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // then
        Assertions.assertThat(response)
                  .extracting(HttpResponse::statusCode)
                  .isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response)
                  .extracting(HttpResponse::body)
                  .asString()
                  .isNotNull()
                  .isNotEmpty()
                  .isNotBlank()
                  .isEqualTo(
                      """
                          {"http_status":"BAD_REQUEST","message":"Invalid combination of request parameters 'aggregate_results' and 'starting_date'.","hint":"Either set 'aggregate_results' to 'false' or remove 'starting_date' request parameter."}""");
    }

}
