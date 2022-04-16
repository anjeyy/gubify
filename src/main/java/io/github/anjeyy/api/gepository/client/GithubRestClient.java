package io.github.anjeyy.api.gepository.client;

import io.github.anjeyy.api.gepository.dto.GithubRepository;
import io.github.anjeyy.api.gepository.dto.GithubSearch;
import io.github.anjeyy.infrastructure.exception.GithubRestException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubRestClient {

    private static final Logger LOGGER = Logger.getLogger("GithubRestClient");
    private static final String GITHUB_SEARCH_URL =
        "https://api.github.com/search/repositories?q=sort=stars&order=desc&per_page={per_page}&page={page}";
    private static final String GITHUB_SEARCH_DATE_URL =
        "https://api.github.com/search/repositories?q=created:>={created}&sort=stars&order=desc&per_page={per_page}&page={page}";

    private final RestTemplate restTemplate;


    public GithubRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Actual <b>GET</b> ReST call to retrieve the most popular <i>github</i> repositories,
     * sorted descending by current stars.
     *
     * @param perPage displayed repositories per page
     * @param page    current page
     * @return most popular github repositories
     * @see <a href="https://docs.github.com/en/rest/reference/search#search-repositories">Github docs</a>
     */
    public List<GithubRepository> getPopularRepositoriesPaginated(long perPage, long page) {
        Map<String, Long> uriVariables = Map.of("per_page", perPage, "page", page);
        ResponseEntity<GithubSearch> responseEntity = retrievePaginatedResultsSafely(GITHUB_SEARCH_URL, uriVariables);
        handleErrors(responseEntity);
        GithubSearch githubSearch = Objects.requireNonNullElse(responseEntity.getBody(), new GithubSearch());
        return githubSearch.getGepositories();
    }

    public List<GithubRepository> getPopularRepositoriesWith(LocalDate created, long perPage, long page) {
        Map<String, ?> uriVariables = Map.of("created", created.toString(), "per_page", perPage, "page", page);
        ResponseEntity<GithubSearch> responseEntity =
            retrievePaginatedResultsSafely(GITHUB_SEARCH_DATE_URL, uriVariables);
        handleErrors(responseEntity);
        GithubSearch githubSearch = Objects.requireNonNullElse(responseEntity.getBody(), new GithubSearch());
        return githubSearch.getGepositories();
    }

    private ResponseEntity<GithubSearch> retrievePaginatedResultsSafely(String url, Map<String, ?> uriVariables) {
        try {
            return restTemplate.getForEntity(url, GithubSearch.class, uriVariables);
        } catch (HttpClientErrorException e) {
            LOGGER.warning(e.getMessage());
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                return ResponseEntity.of(Optional.of(new GithubSearch()));
            }
            throw new GithubRestException(e.getMessage(), e);
        }
    }

    private static <T> void handleErrors(ResponseEntity<T> responseEntity) {
        if (responseEntity.getStatusCode().isError()) {
            throw new GithubRestException("Error during request: \n" + responseEntity.getBody());
        }
    }

}
