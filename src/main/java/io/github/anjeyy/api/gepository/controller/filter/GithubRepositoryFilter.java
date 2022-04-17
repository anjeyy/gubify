package io.github.anjeyy.api.gepository.controller.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.anjeyy.api.gepository.controller.GithubRepositoryController;
import io.github.anjeyy.api.gepository.dto.ErrorResponse;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * A request filter to be applied for {@link GithubRepositoryController}.
 */
@Component
public class GithubRepositoryFilter implements Filter {

    private final ObjectMapper objectMapper;

    public GithubRepositoryFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {

        String path = ((HttpServletRequest) servletRequest).getRequestURI();
        if (!path.equals("/github/repositories")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        boolean requestParamsInValid = isNotValid(servletRequest);
        if (requestParamsInValid) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            byte[] errorResponse = buildErrorResponseAsByte();
            servletResponse.getOutputStream().write(errorResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static boolean isNotValid(ServletRequest servletRequest) {
        String aggregateResultsRawParam = servletRequest.getParameter("aggregate_results");
        String startingDateRawParam = servletRequest.getParameter("starting_date");

        boolean aggregateResultsParam = false;
        if (aggregateResultsRawParam != null) {
            aggregateResultsParam = Boolean.parseBoolean(aggregateResultsRawParam);
        }
        return aggregateResultsParam && startingDateRawParam != null;
    }

    private byte[] buildErrorResponseAsByte() throws JsonProcessingException {
        ErrorResponse errorResponse = buildErrorResponse();
        return objectMapper.writeValueAsBytes(errorResponse);
    }

    private static ErrorResponse buildErrorResponse() {
        return new ErrorResponse.ErrorResponseBuilder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .message("Invalid combination of request parameters 'aggregate_results' and 'starting_date'.")
            .hint("Either set 'aggregate_results' to 'false' or remove 'starting_date' request parameter.")
            .build();
    }

}
