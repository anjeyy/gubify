# gubify

> To do..

<br>
<br>

- only the first 1000 matches are availible
    - `https://api.github.com/search/repositories?q=stars&order=desc&page=1000`

```json
{
  "message": "Only the first 1000 search results are available",
  "documentation_url": "https://docs.github.com/v3/search/"
}
```

- rate limit for unauthenticated calls (10 calls per minute)
    - `https://docs.github.com/en/rest/reference/search#rate-limit`
