<h1>Interviewer Service</h1>

<p>
  <strong>Part of the Xplore Interview Automation Platform</strong> — This microservice manages interview slots, including creation, retrieval, and status tracking.
</p>

<hr>

<h2>🛠️ Tech Stack</h2>
<ul>
  <li>Java 21</li>
  <li>Spring Boot 3.5.3</li>
  <li>Spring Data JPA</li>
  <li>MySQL</li>
  <li>Lombok</li>
  <li>Maven</li>
</ul>

<h2>📦 Features</h2>
<ul>
  <li>Create interview slots</li>
  <li>List all slots</li>
  <li>Filter slots by status (e.g., AVAILABLE, BOOKED)</li>
  <li>Ready for extension to include booking/reservation</li>
</ul>

<h2>📁 Project Structure</h2>

<pre>
interviewer-service/
├── src/
│   ├── main/
│   │   ├── java/com/xplore/interviewer/
│   │   │   ├── controller/          # REST APIs
│   │   │   ├── entity/              # JPA Entity
│   │   │   ├── repository/          # Spring Data JPA Repositories
│   │   │   ├── service/             # Service layer
│   │   │   └── InterviewerServiceApplication.java
│   └── resources/
│       ├── application.properties
├── pom.xml
└── README.md
</pre>

<h2>🧪 API Endpoints</h2>

<h3>➕ Create Interview Slot</h3>
<pre><code>POST /api/slots</code></pre>

<p><strong>Request Body (JSON):</strong></p>
<pre>
{
  "interviewerName": "Alice",
  "dateTime": "2025-07-25 10:00",
  "status": "AVAILABLE",
  "candidateName": ""
}
</pre>

<h3>📄 Get All Slots</h3>
<pre><code>GET /api/slots</code></pre>
<p>Returns a list of all interview slots.</p>

<h2>🧰 Setup Instructions</h2>

<h3>1. Clone the Repository</h3>
<pre><code>git clone https://github.com/your-username/interviewer-service.git
cd interviewer-service</code></pre>

<h3>2. Configure Database</h3>
<p>Ensure MySQL is running and create a database:</p>
<pre><code>CREATE DATABASE xplore;</code></pre>

<p>Update <code>application.properties</code>:</p>
<pre>
spring.datasource.url=jdbc:mysql://localhost:3306/xplore
spring.datasource.username=root
spring.datasource.password=your_password
</pre>

<h3>3. Build & Run</h3>
<pre><code>./mvnw clean install
./mvnw spring-boot:run</code></pre>

<h2>🚀 Future Enhancements</h2>
<ul>
  <li>Slot booking & reservation endpoints</li>
  <li>Integration with candidate service</li>
  <li>Email/SMS notifications</li>
  <li>Swagger/OpenAPI documentation</li>
  <li>Docker support</li>
</ul>

<h2>📫 Contact</h2>
<p>
  For questions, ideas, or contributions:<br>
  📧 <a href="mailto:shaikhusama745@gmail.com">shaikhusama745@gmail.com</a><br>
  🔗 <a href="https://github.com/usamashaikh13" target="_blank">GitHub</a>
</p>
