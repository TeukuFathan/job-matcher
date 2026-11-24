# Job Matcher

<img width="986" height="521" alt="image" src="https://github.com/user-attachments/assets/625cae08-bb08-40c1-b330-05aea4dc5ec9" />

A small Spring Boot application for discovering and reviewing job listings. Enter a search query in the web page or API; the app fetches listings from JSearch, removes some unwanted results, asks Gemini to assess up to five jobs, and returns links to inspect. It assists job discovery and evaluation; it does not submit applications.

## Stack

- Java 21, Spring Boot 4.1.1, Maven Wrapper
- Spring Web MVC and `RestClient` for the API and JSearch request
- Google Gen AI Java SDK 1.70.0 (`gemini-3.5-flash-lite`)
- Plain HTML, CSS, and JavaScript for the search page
- JUnit 5 and Mockito for tests (via Spring Boot's web MVC test starter)

## How a search works

`JSearch → map provider fields to Job → filter → Gemini analysis → results`

JSearch is queried with the entered text and the currently fixed country and language settings (`fr`). Mapping keeps fields such as title, company, description, salary, and apply URL. Filtering removes jobs without a description and jobs containing configured excluded skills or title keywords. Gemini analyzes the title and description of the first **five** remaining jobs. The API then returns each job's title, company, and apply URL. Gemini's decision and explanation are **not** exposed or used to remove jobs from the final response yet.

## Run locally

Prerequisites: JDK 21, internet access, a JSearch API key, and a Gemini API key. The included Maven Wrapper downloads Maven if needed.

From the directory containing this README, set these environment variables in PowerShell:

```powershell
$env:JSEARCH_API_KEY = "<your-jsearch-api-key>"
$env:JSEARCH_API_BASE_URL = "https://api.openwebninja.com"
$env:GEMINI_API_KEY = "<your-gemini-api-key>"
./mvnw.cmd spring-boot:run
```

Spring maps these variables to `jsearch.api.key`, `jsearch.api.base-url`, and `gemini.api.key`. The project activates its `local` Spring profile by default; environment variables override its local properties. Keep API keys out of committed files. Open `http://localhost:8080/` after startup.

## Search profile

Edit [job-search-profile.json](./src/main/resources/config/job-search-profile.json) and restart the app; the profile is loaded at startup. Its fields are `targetRoles`, `skills`, `locations`, `remoteAccepted`, `minimumSalary`, `excludedSkills`, and `excludedTitleKeywords`.

**Currently active in filtering:** `excludedSkills` (case-insensitive substring in title or description) and `excludedTitleKeywords` (case-insensitive substring in title). Jobs also need a nonblank description. The other profile fields are stored and returned by the profile endpoint, but do not currently change the JSearch request, filtering, or Gemini prompt. The Gemini prompt is loaded separately from [job-matching.txt](./src/main/resources/prompts/job-matching.txt). The minimum salary filter is currently disabled.

## Endpoints

| Request | Response |
| --- | --- |
| `GET /` | HTML search page |
| `GET /api/jobs/search?query=java%20developer` | JSON array of simplified jobs; `[]` when none remain |
| `GET /api/profile` | JSON for the loaded search profile |
| `GET /ping` | Plain text `pong` |
| `GET /test/getTestGemini` | Gemini-generated plain text test response; uses API quota |

Example search:

```http
GET /api/jobs/search?query=java%20developer
```

```json
[
  {
    "title": "Java Developer",
    "company": "Example Company",
    "applyUrl": "https://example.com/job"
  }
]
```

Example profile request and response (illustrative values):

```http
GET /api/profile
```

```json
{
  "targetRoles": ["Developer"],
  "skills": ["Java"],
  "locations": ["France"],
  "remoteAccepted": true,
  "minimumSalary": null,
  "excludedSkills": ["Example excluded skill"],
  "excludedTitleKeywords": ["Senior"]
}
```

`GET /ping` returns `pong`. `GET /test/getTestGemini` returns generated text that varies by request. A blank search query returns HTTP 400 with `{"error":"Search query must not be blank"}`. JSearch failures return HTTP 502; a recognized Gemini quota error returns HTTP 429.

## Tests

```powershell
./mvnw.cmd test
```

Most tests use mocks. `JobMatchingServiceTest` calls Gemini with the `local` profile, so the full suite needs a valid Gemini key and available quota; its AI-dependent assertion can vary with provider output.

## Limitations

- Many listings have missing salary data, and the minimum salary filter is disabled.
- JSearch can return incomplete or imperfect results; searches are currently fixed to French country and language parameters.
- Gemini assessments can be inaccurate or vary. The public result does not show its reasoning or apply its match decision.
- JSearch and Gemini availability, rate limits, and quotas affect searches. Each search can make up to five Gemini calls.

## Roadmap

Planned ideas, **not implemented**: use more profile fields in search and filtering, show Gemini's assessment alongside each job, and make country and language configurable.
