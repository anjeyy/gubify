package io.github.anjeyy.api.gepository.client;

import static org.mockito.BDDMockito.given;

import io.github.anjeyy.api.gepository.dto.GithubRepository;
import io.github.anjeyy.api.gepository.dto.GithubSearch;
import io.github.anjeyy.infrastructure.exception.GithubRestException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class GithubRestClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubRestClient uut;

    @Test
    void givenGithubRestCall_outsideOfParameterRange_returnsEmptyResponse() {
        // given
        int limit = 1;
        int offset = 1;
        String url = "https://api.github.com/search/repositories?q=sort=stars&order=desc&per_page={per_page}&page={page}";

        given(restTemplate.getForEntity(url, GithubSearch.class, Map.of("per_page", 1L, "page", 1L)))
            .willThrow(
                new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Simulate result range exceeded.")
            );

        // when
        List<GithubRepository> actual = uut.getPopularRepositoriesPaginated(limit, offset);

        // then
        Assertions.assertThat(actual).isNotNull().isEmpty();
    }

    @Test
    void givenGithubRestCall_afterTooManyRequests_throwsException() {
        // given
        int limit = 1;
        int offset = 1;
        String url = "https://api.github.com/search/repositories?q=sort=stars&order=desc&per_page={per_page}&page={page}";

        given(restTemplate.getForEntity(url, GithubSearch.class, Map.of("per_page", 1L, "page", 1L)))
            .willThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN, "Simulate Rate Limit error."));

        // when
        ThrowingCallable expectedThrow = () -> uut.getPopularRepositoriesPaginated(limit, offset);

        // then
        Assertions.assertThatThrownBy(expectedThrow)
                  .isInstanceOf(GithubRestException.class)
                  .hasMessage("403 Simulate Rate Limit error.");
    }

    @Test
    void givenGithubRestCall_withEmptyBody_returnsEmptyResponse() {
        // given
        int limit = 1;
        int offset = 1;
        String url = "https://api.github.com/search/repositories?q=sort=stars&order=desc&per_page={per_page}&page={page}";

        given(restTemplate.getForEntity(url, GithubSearch.class, Map.of("per_page", 1L, "page", 1L)))
            .willReturn(ResponseEntity.of(Optional.of(new GithubSearch())));

        // when
        List<GithubRepository> actual = uut.getPopularRepositoriesPaginated(limit, offset);

        // then
        Assertions.assertThat(actual).isNotNull().isEmpty();
    }

    @Test
    void givenDateGithubRestCall_withEmptyBody_returnsEmptyResponse() {
        // given
        LocalDate created = LocalDate.now();
        int limit = 1;
        int offset = 1;
        String url = "https://api.github.com/search/repositories?q=created:>={created}&sort=stars&order=desc&per_page={per_page}&page={page}";

        given(restTemplate.getForEntity(
            url,
            GithubSearch.class,
            Map.of("created", created.toString(), "per_page", 1L, "page", 1L))
        )
            .willReturn(ResponseEntity.of(Optional.of(new GithubSearch())));

        // when
        List<GithubRepository> actual = uut.getPopularRepositoriesWith(created, limit, offset);

        // then
        Assertions.assertThat(actual).isNotNull().isEmpty();
    }

}
