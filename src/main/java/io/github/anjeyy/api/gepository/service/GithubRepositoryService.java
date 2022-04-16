package io.github.anjeyy.api.gepository.service;

import io.github.anjeyy.api.gepository.client.GithubRestClient;
import io.github.anjeyy.api.gepository.dto.GithubRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class GithubRepositoryService {

    private static final Logger LOGGER = Logger.getLogger("GithubRepositoryService");

    private final GithubRestClient githubRestClient;

    public GithubRepositoryService(GithubRestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    /**
     * Get a specific page of the githubs resultset with given amount to display per page.
     *
     * @param limit  display results per page
     * @param offset current page to view
     * @return the 'limit' most popular repositories of page 'offset' sorted by starcount
     */
    public List<GithubRepository> getTheMostPopularGithubRepositories(int limit, int offset) {
        LOGGER.info(String.format("Calling popular repositories for page %d and amount per page %d.", offset, limit));
        List<GithubRepository> result = githubRestClient.getPopularRepositoriesPaginated(limit, offset);
        Collections.sort(result);
        return result;
    }

    public List<GithubRepository> getTheMostPopularGithubRepositories(LocalDate startingDate, int limit, int offset) {
        LOGGER.info(
            String.format("Calling popular repositories starting from %s for page %d and amount per page %d.",
                          startingDate, offset, limit)
        );
        List<GithubRepository> githubRepositories =
            githubRestClient.getPopularRepositoriesWith(startingDate, limit, offset);
        Collections.sort(githubRepositories);
        return githubRepositories;
    }

    /**
     * Get the most popular github repositories, currently the first <i>1000</i>.
     *
     * @return 1000 most popular github repositories
     * @see <a href="https://docs.github.com/en/rest/reference/search#rate-limit">Rate limit</a>
     */
    public List<GithubRepository> getTheMostPopularGithubRepositories() {
        List<GithubRepository> result = new ArrayList<>();
        LOGGER.info("Aggregating ALL github repositories.");

        int perPage = 100;
        int currentPage = 1;
        boolean searchEnabled = true;

        while (searchEnabled) {
            List<GithubRepository> githubRepositories =
                githubRestClient.getPopularRepositoriesPaginated(perPage, currentPage);
            boolean isRateLimitExceeded = currentPage == 10;
            if (githubRepositories.isEmpty() || isRateLimitExceeded) {
                searchEnabled = false;
            }
            result.addAll(githubRepositories);
            currentPage++;
            LOGGER.info(String.format("Page %d completed..", currentPage));
        }
        Collections.sort(result);
        return result;
    }


}
