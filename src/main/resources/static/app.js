const searchInput = document.getElementById("searchInput");
const searchButton = document.getElementById("searchButton");
const jobResults = document.getElementById("jobResults");

searchButton.addEventListener("click", searchJobs);

async function searchJobs() {
    const query = searchInput.value.trim();

    if (!query) {
        jobResults.innerHTML = "<p>Please enter a search.</p>";
        return;
    }

    jobResults.innerHTML = "<p>Loading...</p>";

    try {
        const response = await fetch(
            `/api/jobs/search?query=${encodeURIComponent(query)}`
        );

        if (!response.ok) {
            throw new Error("Search failed");
        }

        const jobs = await response.json();

        displayJobs(jobs);

    } catch (error) {
        jobResults.innerHTML = "<p>Something went wrong.</p>";
    }
}

function displayJobs(jobs) {
    jobResults.innerHTML = "";

    if (jobs.length === 0) {
        jobResults.innerHTML = "<p>No matching jobs found.</p>";
        return;
    }

    const table = document.createElement("table");

    table.innerHTML = `
        <thead>
            <tr>
                <th>Job</th>
                <th>Company</th>
                <th>Apply</th>
            </tr>
        </thead>

        <tbody>
            ${jobs.map(job => `
                <tr>
                    <td>${job.title}</td>
                    <td>${job.company}</td>
                    <td>
                        <a
                            href="${job.applyUrl}"
                            target="_blank"
                            rel="noopener noreferrer"
                        >
                            View job
                        </a>
                    </td>
                </tr>
            `).join("")}
        </tbody>
    `;

    jobResults.appendChild(table);
}