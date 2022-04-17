package io.github.anjeyy.api.datagenerator;

import io.github.anjeyy.api.gepository.dto.GithubRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GithubRepositoryGenerator {

    public GithubRepositoryGenerator() {
        throw new UnsupportedOperationException("No instance allowed");
    }

    public static List<GithubRepository> mockSingleGithubResponseAsList() {
        List<GithubRepository> result = new ArrayList<>();
        result.add(mockSingleGithubResponse());
        return result;
    }

    public static GithubRepository mockSingleGithubResponse() {
        GithubRepository githubRepository = new GithubRepository();
        githubRepository.setName("starship");
        githubRepository.setFullName("starship/starship");
        githubRepository.setUrl("https://api.github.com/repos/starship/starship");
        githubRepository.setHtmlUrl("https://github.com/starship/starship");
        githubRepository.setCreatedAt(LocalDateTime.parse("2019-04-02T03:23:12"));
        githubRepository.setUpdatedAt(LocalDateTime.parse("2022-04-15T08:42:49"));
        githubRepository.setStargazersCount(24530);
        githubRepository.setForksCount(1026);
        githubRepository.setLanguage("Rust");
        githubRepository.setTopics(List.of("bash",
                                           "fish",
                                           "fish-prompt",
                                           "fish-theme",
                                           "hacktoberfest",
                                           "oh-my-zsh",
                                           "powershell",
                                           "rust",
                                           "shell-prompt",
                                           "starship",
                                           "zsh",
                                           "zsh-prompt",
                                           "zsh-theme")
        );
        return githubRepository;
    }
}
