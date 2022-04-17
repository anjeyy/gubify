package io.github.anjeyy.api.gepository.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.anjeyy.api.datagenerator.GithubRepositoryGenerator;
import io.github.anjeyy.api.gepository.controller.filter.GithubRepositoryFilter;
import io.github.anjeyy.api.gepository.dto.ErrorResponse;
import io.github.anjeyy.api.gepository.dto.GithubRepository;
import io.github.anjeyy.api.gepository.service.GithubRepositoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryControllerTest {

    @Mock
    private GithubRepositoryService githubRepositoryService;

    @InjectMocks
    private GithubRepositoryController githubRepositoryController;

    private JacksonTester<List<GithubRepository>> githubListJacksonTester;
    private JacksonTester<ErrorResponse> errorResponseJacksonTester;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(githubRepositoryController)
                .alwaysDo(MockMvcResultHandlers.print())
                .setControllerAdvice(new GlobalRestAdvice())
                .addFilters(new GithubRepositoryFilter(new ObjectMapper()))
                .build();

        JacksonTester.initFields(this, new ObjectMapper().registerModule(new JavaTimeModule()));
    }

    @Test
    void givenGithubRepositoryRequest_withValidPaginationParams_willResultInResponse() throws Exception {
        // given
        List<GithubRepository> githubRepositoryList = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRepositoryService.getTheMostPopularGithubRepositories(2, 3))
            .willReturn(githubRepositoryList);
        String expectedResponseJson = githubListJacksonTester.write(githubRepositoryList).getJson();

        // when-then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/github/repositories")
                    .param("limit", "2")
                    .param("offset", "3")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseJson));
    }

    @Test
    void givenGithubRepositoryRequest_withValidAggregationParams_willResultInResponse() throws Exception {
        // given
        List<GithubRepository> githubRepositoryList = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRepositoryService.getTheMostPopularGithubRepositories())
            .willReturn(githubRepositoryList);
        String expectedResponseJson = githubListJacksonTester.write(githubRepositoryList).getJson();

        // when-then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/github/repositories")
                    .param("aggregate_results", "true")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseJson));
    }

    @Test
    void givenTopGithubRepositoryRequest_withValidAggregationParams_willResultInResponse() throws Exception {
        // given
        List<GithubRepository> githubRepositoryList = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRepositoryService.getTheMostPopularGithubRepositories(10, 1))
            .willReturn(githubRepositoryList);
        String expectedResponseJson = githubListJacksonTester.write(githubRepositoryList).getJson();

        // when-then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/github/repositories/top10")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseJson));
    }

}