# gubify

An application to host as a webservice, serving to search for the most popular github repositories

## table of content

- [how to run](#how-to-run)
- [available endpoints](#available-endpoints)
    - [top X](#top-x)
    - [paginated top results](#paginated-top-results)
    - [aggregated top results](#aggregated-top-results)
    - [creation date top results](#creation-date-top-results)
- [general](#general)
    - [searching restrictions](#searching-restriction)
    - [rate limit](#rate-limit)

<br>

## how to run

Run webservice within a docker container

- `docker run --name gubify -p 8080:8080 anjeyy/gubify:latest`

After successful launch you will see something like the following image.

![img](/docs/docker-run-example.png)

<br>

## available endpoints

The response is always a `JSON` payload representing results or an occurring error.

### top X

Get the `X` most popular GitHub repositories sorted by their stars.

- `/github/repositories/top{top}`
    - replace `{top}` with a number `x` for `0 < x < 101`

### paginated top results

Get the most popular GitHub repositories sorted by their stars, results are _paginated_.

- `/github/repositories?limit={limit}&offset={offset}`
    - default: `{limit}=100`, `{offset=1}`
    - replace `{limit}` with a number `x` for `0 < x < 101`
    - replace `{offset}` with a number `x` for `0 < x < 1001`

**Note**: Only the first 1000 matches are available from original GitHub
API. [Example Query](https://api.github.com/search/repositories?q=stars&order=desc&page=1000)

### aggregated top results

Get the most popular GitHub repositories, aggregated and sorted by their stars, 1000 at the
most (see below).

- `/github/repositories?aggregate_results=true`

**Note**: Only the first 1000 matches are available from original GitHub
API. [Example Query](https://api.github.com/search/repositories?q=stars&order=desc&page=1000)

### creation date top results

Get the most popular GitHub repositories, with a creation date given and results are paginated
and sorted by their stars.

- `/github/repositories?starting_date={creation}`
    - default: `{limit}=100`, `{offset=1}`
    - replace `{creation}` with a date in the format like `2022-01-01`

**Note**: Only the first 1000 matches are available from original GitHub
API. [Example Query](https://api.github.com/search/repositories?q=stars&order=desc&page=1000)

<br>

## general

### searching restriction

Like above mentioned, GitHub provides the first 1000 results.
Otherwise, the GitHub API returns following JSON response.

```json
{
  "message": "Only the first 1000 search results are available",
  "documentation_url": "https://docs.github.com/v3/search/"
}
```

### rate limit

Since GitHub has a rate limit for unauthenticated calls, with _10_ calls per minute,
this webservice provides also only 10 calls per minute to search for the most popular repositories.

Such a rate limit error will be displayed with a `429 Too Many Requests` HTTP Error as a JSON response.

```json
{
  "http_status": "TOO_MANY_REQUESTS",
  "message": "403 rate limit exceeded: \"{\"message\":\"API rate limit exceeded for xxx.xxx.xxx.xxx. (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.)\",\"documentation_url\":\"https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting\"}<EOL>\"",
  "hint": "Please wait for another 60s to refresh your requests."
}
```

For further improvement an authentication can be provided to extend the rate limit.
Please open an PR in case of improvement.
