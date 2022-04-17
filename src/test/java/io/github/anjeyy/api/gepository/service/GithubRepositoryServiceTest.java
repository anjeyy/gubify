package io.github.anjeyy.api.gepository.service;

import static org.mockito.BDDMockito.given;

import io.github.anjeyy.api.datagenerator.GithubRepositoryGenerator;
import io.github.anjeyy.api.gepository.client.GithubRestClient;
import io.github.anjeyy.api.gepository.dto.GithubRepository;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryServiceTest {

    @Mock
    private GithubRestClient githubRestClient;

    @InjectMocks
    private GithubRepositoryService uut;

    @Test
    void givenPagination_withValidParams_willReturnResult() {
        // given
        int limit = 1;
        int offset = 2;
        List<GithubRepository> expected = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRestClient.getPopularRepositoriesPaginated(1, 2)).willReturn(expected);

        // when
        List<GithubRepository> actual = uut.getTheMostPopularGithubRepositories(limit, offset);

        // then
        Assertions.assertThat(actual)
                  .hasSize(1)
                  .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void givenDateAndPagination_withValidParams_willReturnResult() {
        // given
        LocalDate created = LocalDate.now();
        int limit = 1;
        int offset = 2;
        List<GithubRepository> expected = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRestClient.getPopularRepositoriesWith(created, 1, 2)).willReturn(expected);

        // when
        List<GithubRepository> actual = uut.getTheMostPopularGithubRepositories(created, limit, offset);

        // then
        Assertions.assertThat(actual)
                  .hasSize(1)
                  .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void givenMostPopularRepo_withValidParams_willReturnResult() {
        // given
        int limit = 100;
        int offset = 1;
        List<GithubRepository> expected = List.of(GithubRepositoryGenerator.mockSingleGithubResponse(),
                                                  GithubRepositoryGenerator.mockSingleGithubResponse());
        List<GithubRepository> singleResponse = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRestClient.getPopularRepositoriesPaginated(limit, offset)).willReturn(singleResponse);
        given(githubRestClient.getPopularRepositoriesPaginated(limit, offset + 1)).willReturn(singleResponse);
        given(githubRestClient.getPopularRepositoriesPaginated(limit, offset + 2)).willReturn(List.of());

        // when
        List<GithubRepository> actual = uut.getTheMostPopularGithubRepositories();

        // then
        Assertions.assertThat(actual)
                  .hasSize(2)
                  .containsExactlyInAnyOrderElementsOf(expected);
    }

}