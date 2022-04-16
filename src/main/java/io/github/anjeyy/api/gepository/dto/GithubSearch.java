package io.github.anjeyy.api.gepository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class GithubSearch {

    @JsonProperty("total_count")
    private long totalCount;

    @JsonProperty("incomplete_results")
    private boolean incompleteResults;

    @JsonProperty("items")
    private List<GithubRepository> gepositories;

    public GithubSearch() {}

    public GithubSearch(
        long totalCount,
        boolean incompleteResults,
        List<GithubRepository> gepositories
    ) {
        this.totalCount = totalCount;
        this.incompleteResults = incompleteResults;
        this.gepositories = gepositories;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<GithubRepository> getGepositories() {
        if (gepositories == null) {
            return new ArrayList<>();
        }
        return gepositories;
    }

    public void setGepositories(List<GithubRepository> gepositories) {
        this.gepositories = gepositories;
    }
}
