package io.github.anjeyy.api.gepository.controller;

import io.github.anjeyy.api.gepository.controller.filter.GithubRepositoryFilter;
import io.github.anjeyy.api.gepository.dto.GithubRepository;
import io.github.anjeyy.api.gepository.service.GithubRepositoryService;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class GithubRepositoryController {

    private final GithubRepositoryService githubRepositoryService;

    public GithubRepositoryController(
        GithubRepositoryService githubRepositoryService) {
        this.githubRepositoryService = githubRepositoryService;
    }

    /**
     * Entry point for a web request to get the most popular github repositories.
     * <p>
     * <b>Hint: </b> {@link GithubRepositoryFilter} checks for a valid request parameter combination,
     * reducing logic inside this controller class.
     * </p>
     *
     * @param aggregateResults if provided aggregating the results
     * @param limit            amount of repositories displayed per page
     * @param offset           page number
     * @param startingDate     if provided starting date for repositories
     * @return most popular github repositories
     * @see GithubRepositoryFilter
     */
    @GetMapping(path = "/github/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GithubRepository>> searchForAllGithubRepositories(
        @RequestParam(name = "aggregate_results", defaultValue = "false") boolean aggregateResults,
        @RequestParam(defaultValue = "100") @Min(1) @Max(100) int limit,
        @RequestParam(defaultValue = "1") @Min(1) @Max(1000) int offset,
        @RequestParam(name = "starting_date", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startingDate
    ) {
        List<GithubRepository> mostPopularGithubRepositories;

        if (startingDate != null) {
            mostPopularGithubRepositories =
                githubRepositoryService.getTheMostPopularGithubRepositories(startingDate, limit, offset);
            return ResponseEntity.ok(mostPopularGithubRepositories);
        }

        if (aggregateResults) {
            mostPopularGithubRepositories = githubRepositoryService.getTheMostPopularGithubRepositories();
        } else {
            mostPopularGithubRepositories = githubRepositoryService.getTheMostPopularGithubRepositories(limit, offset);
        }
        return ResponseEntity.ok(mostPopularGithubRepositories);
    }

    @GetMapping(path = "/github/repositories/top{top}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GithubRepository>> searchForTop10GithubRepositories(
        @PathVariable @Min(1) @Max(100) Integer top
    ) {
        List<GithubRepository> mostPopularGithubRepositories =
            githubRepositoryService.getTheMostPopularGithubRepositories(top, 1);
        return ResponseEntity.ok(mostPopularGithubRepositories);
    }

}
