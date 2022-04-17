package io.github.anjeyy.api.gepository.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GithubRepository implements Comparable<GithubRepository> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("url")
    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("stargazers_count")
    private long stargazersCount;

    @JsonProperty("forks_count")
    private long forksCount;

    @JsonProperty("language")
    private String language;

    @JsonProperty("topics")
    private List<String> topics;

    public GithubRepository() {}

    public GithubRepository(
        String name,
        String fullName,
        String url,
        String htmlUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long stargazersCount,
        long forksCount,
        String language,
        List<String> topics
    ) {
        this.name = name;
        this.fullName = fullName;
        this.url = url;
        this.htmlUrl = htmlUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stargazersCount = stargazersCount;
        this.forksCount = forksCount;
        this.language = language;
        this.topics = topics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GithubRepository)) {
            return false;
        }
        GithubRepository that = (GithubRepository) o;
        return fullName.equals(that.fullName) && htmlUrl.equals(that.htmlUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, htmlUrl);
    }

    @Override
    public int compareTo(GithubRepository o) {
        if (o == null) {
            return -1;
        }
        return Long.compare(this.stargazersCount, o.stargazersCount) * -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(long stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public long getForksCount() {
        return forksCount;
    }

    public void setForksCount(long forksCount) {
        this.forksCount = forksCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
