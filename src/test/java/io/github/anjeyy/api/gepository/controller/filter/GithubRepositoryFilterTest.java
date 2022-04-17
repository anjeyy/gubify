package io.github.anjeyy.api.gepository.controller.filter;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.anjeyy.api.datagenerator.GithubRepositoryGenerator;
import io.github.anjeyy.api.gepository.controller.GithubRepositoryController;
import io.github.anjeyy.api.gepository.controller.GlobalRestAdvice;
import io.github.anjeyy.api.gepository.dto.ErrorResponse;
import io.github.anjeyy.api.gepository.dto.GithubRepository;
import io.github.anjeyy.api.gepository.service.GithubRepositoryService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryFilterTest {

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
    void givenCorrectPath_withInvalidParams_willResultInErrorResponse() throws Exception {
        // given
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .message("Invalid combination of request parameters 'aggregate_results' and 'starting_date'.")
            .hint("Either set 'aggregate_results' to 'false' or remove 'starting_date' request parameter.")
            .build();
        String expectedErrorResponseJson = errorResponseJacksonTester.write(errorResponse).getJson();

        // when-then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/github/repositories")
                    .param("aggregate_results", "true")
                    .param("starting_date", "2022-01-01")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().string(expectedErrorResponseJson));
    }

    @Test
    void givenCorrectPath_withValidParams_willResultInResponse() throws Exception {
        // given
        List<GithubRepository> githubRepositoryList = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRepositoryService.getTheMostPopularGithubRepositories(
            LocalDate.of(2022, 1, 1),
            100,
            1
        )).willReturn(githubRepositoryList);
        String expectedResponseJson = githubListJacksonTester.write(githubRepositoryList).getJson();

        // when-then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/github/repositories")
                    .param("starting_date", "2022-01-01")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseJson));
    }

    @Test
    void givenInCorrectPath_withInValidParams_willIgnoreParameterFilter() throws Exception {
        // given
        List<GithubRepository> githubRepositoryList = GithubRepositoryGenerator.mockSingleGithubResponseAsList();
        given(githubRepositoryService.getTheMostPopularGithubRepositories(100, 1))
            .willReturn(githubRepositoryList);
        String expectedResponseJson = githubListJacksonTester.write(githubRepositoryList).getJson();

        // when-then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/github/repositories/top100")
                    .param("aggregate_results", "true")
                    .param("starting_date", "2022-01-01")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseJson));
    }

}