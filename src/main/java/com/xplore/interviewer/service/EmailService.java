package com.xplore.interviewer.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendInterviewScheduledEmail(
            String candidateEmail,
            String candidateName,
            String interviewerName,
            LocalDateTime interviewTime,
            int durationMinutes,
            String round) {
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(candidateEmail);
            message.setSubject("Interview Scheduled - " + round);
            
            String emailBody = buildCandidateEmailBody(
                    candidateName, interviewerName, interviewTime, durationMinutes, round);
            
            message.setText(emailBody);
            mailSender.send(message);
            
            logger.info("Interview scheduled email sent to candidate: {}", candidateEmail);
        } catch (Exception e) {
            logger.error("Failed to send email to candidate {}: {}", candidateEmail, e.getMessage());
        }
    }
    
    public void sendInterviewerNotificationEmail(
            String interviewerEmail,
            String interviewerName,
            String candidateName,
            String candidateEmail,
            LocalDateTime interviewTime,
            int durationMinutes,
            String round) {
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(interviewerEmail);
            message.setSubject("New Interview Assignment - " + round);
            
            String emailBody = buildInterviewerEmailBody(
                    interviewerName, candidateName, candidateEmail, interviewTime, durationMinutes, round);
            
            message.setText(emailBody);
            mailSender.send(message);
            
            logger.info("Interview notification email sent to interviewer: {}", interviewerEmail);
        } catch (Exception e) {
            logger.error("Failed to send email to interviewer {}: {}", interviewerEmail, e.getMessage());
        }
    }
    
    public void sendFeedbackSubmittedEmail(
            String candidateEmail,
            String candidateName,
            String round) {
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(candidateEmail);
            message.setSubject("Interview Feedback Submitted - " + round);
            
            String emailBody = String.format(
                    "Dear %s,\n\n" +
                    "Thank you for attending the %s interview with us.\n\n" +
                    "Your interviewer has submitted their feedback. Our recruitment team will review it and get back to you soon.\n\n" +
                    "Best regards,\n" +
                    "Xplore Recruitment Team",
                    candidateName, round);
            
            message.setText(emailBody);
            mailSender.send(message);
            
            logger.info("Feedback submitted notification sent to candidate: {}", candidateEmail);
        } catch (Exception e) {
            logger.error("Failed to send feedback notification to candidate {}: {}", candidateEmail, e.getMessage());
        }
    }
    
    private String buildCandidateEmailBody(
            String candidateName,
            String interviewerName,
            LocalDateTime interviewTime,
            int durationMinutes,
            String round) {
        
        return String.format(
                "Dear %s,\n\n" +
                "Your interview has been scheduled!\n\n" +
                "Interview Details:\n" +
                "Round: %s\n" +
                "Interviewer: %s\n" +
                "Date & Time: %s\n" +
                "Duration: %d minutes\n\n" +
                "Please ensure you are available 5 minutes before the scheduled time.\n\n" +
                "If you need to reschedule, please contact our recruitment team immediately.\n\n" +
                "Best of luck!\n\n" +
                "Best regards,\n" +
                "Xplore Recruitment Team",
                candidateName,
                round,
                interviewerName,
                interviewTime.format(DATE_FORMATTER),
                durationMinutes
        );
    }
    
    private String buildInterviewerEmailBody(
            String interviewerName,
            String candidateName,
            String candidateEmail,
            LocalDateTime interviewTime,
            int durationMinutes,
            String round) {
        
        return String.format(
                "Dear %s,\n\n" +
                "You have been assigned a new interview!\n\n" +
                "Interview Details:\n" +
                "Round: %s\n" +
                "Candidate: %s\n" +
                "Candidate Email: %s\n" +
                "Date & Time: %s\n" +
                "Duration: %d minutes\n\n" +
                "Please review the candidate's profile before the interview and ensure you're available at the scheduled time.\n\n" +
                "After the interview, please submit your feedback through the system.\n\n" +
                "Best regards,\n" +
                "Xplore Recruitment Team",
                interviewerName,
                round,
                candidateName,
                candidateEmail,
                interviewTime.format(DATE_FORMATTER),
                durationMinutes
        );
    }
}
