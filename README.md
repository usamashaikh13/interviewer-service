# Interviewer Service

Interviewer Service is part of the Xplore hiring platform. It manages interviewers, interviewer availability, interview slots, slot booking, interview feedback, and email notifications.

## Tech Stack

- Java 21
- Spring Boot 3.1.5
- Spring Web
- Spring Data JPA
- H2 in-memory database
- Lombok
- Maven

## Responsibilities

- Create and manage interviewer profiles.
- Maintain interviewer availability blocks.
- Create, list, search, and book interview slots.
- Prevent interviewer double-booking with slot conflict detection.
- Reschedule, cancel, decline, and mark no-show for interview slots.
- Generate `.ics` calendar invites for scheduled interviews.
- Match available slots by skills, experience, and interview round.
- Capture interview feedback.
- Notify candidates and interviewers by email.
- Notify Recruitment Service when feedback marks an interview as completed.

## Runtime Configuration

The service runs on port `8081` by default.

Important properties:

```properties
server.port=8081
spring.application.name=interviewer-service
spring.datasource.url=jdbc:h2:mem:xplore
spring.jpa.hibernate.ddl-auto=create-drop
recruitment.service.url=${RECRUITMENT_SERVICE_URL:http://localhost:8082/api/recruitments}
```

Email settings are loaded from environment variables:

```bash
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=your-email@example.com
export MAIL_PASSWORD=your-app-password
```

Do not commit real email credentials to source control.

## Main APIs

### Interviewers

```http
POST   /api/interviewers
GET    /api/interviewers
GET    /api/interviewers/{id}
PUT    /api/interviewers/{id}
DELETE /api/interviewers/{id}
GET    /api/interviewers/search?skills=Java,Spring&minExperience=3
```

Example interviewer:

```json
{
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "phone": "9999999999",
  "technicalSkills": ["Java", "Spring Boot"],
  "yearsExperience": 6,
  "designation": "Senior Engineer",
  "department": "Engineering",
  "bio": "Backend interviewer"
}
```

### Availability

```http
POST   /api/availability
GET    /api/availability
GET    /api/availability/{id}
DELETE /api/availability/{id}
```

### Interview Slots

```http
POST /api/slots
GET  /api/slots
GET  /api/slots/{id}
GET  /api/slots/available?skills=Java,Spring&minExperience=3&round=L1
GET  /api/slots/available-slots
POST /api/slots/{id}/book?candidateId=1
PATCH /api/slots/{id}/reschedule
PATCH /api/slots/{id}/cancel
PATCH /api/slots/{id}/decline
PATCH /api/slots/{id}/no-show
GET   /api/slots/{id}/calendar.ics
```

Example slot:

```json
{
  "interviewerId": 1,
  "interviewerName": "Alice Johnson",
  "technicalSkills": ["Java", "Spring Boot"],
  "minYearsExperience": 3,
  "startTime": "2026-05-20T10:00:00",
  "endTime": "2026-05-20T11:00:00",
  "round": "L1",
  "meetingLink": "https://meet.example.com/interview-1",
  "status": "AVAILABLE"
}
```

Slot statuses:

```text
AVAILABLE, BOOKED, RESERVED, COMPLETED, SCHEDULED, CANCELLED, NO_SHOW
```

Example reschedule request:

```json
{
  "startTime": "2026-05-20T12:00:00",
  "endTime": "2026-05-20T13:00:00",
  "durationMinutes": 60,
  "reason": "Candidate requested a later slot"
}
```

Example cancel or decline request:

```json
{
  "reason": "Interviewer unavailable"
}
```

### Feedback

```http
POST /api/feedback
GET  /api/feedback/slot/{slotId}
GET  /api/feedback/interviewer/{interviewerId}
GET  /api/feedback/candidate/{candidateId}
GET  /api/feedback/recruitment/{recruitmentId}
```

Example feedback:

```json
{
  "interviewSlotId": 1,
  "interviewerId": 1,
  "candidateId": 1,
  "recruitmentId": 1,
  "technicalRating": 4,
  "communicationRating": 4,
  "problemSolvingRating": 5,
  "overallRating": 4,
  "recommendation": "HIRE",
  "strengths": "Strong backend fundamentals",
  "weaknesses": "Needs more cloud depth",
  "detailedComments": "Good fit for the role."
}
```

### Email Notifications

```http
POST /api/email/candidate
POST /api/email/interviewer
```

Recruitment Service calls these endpoints after scheduling an interview.

## Run Locally

```bash
./mvnw clean test
./mvnw spring-boot:run
```

H2 console:

```text
http://localhost:8081/h2-console
JDBC URL: jdbc:h2:mem:xplore
User: sa
Password:
```

## Service Integration

This service expects Recruitment Service to be available at:

```text
http://localhost:8082/api/recruitments
```

Override it with:

```bash
export RECRUITMENT_SERVICE_URL=http://localhost:8082/api/recruitments
```

When feedback is submitted, the service updates the related recruitment record to `COMPLETED`.
