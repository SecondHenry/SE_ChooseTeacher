package model;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * represents an assignment of a teacher to a teachering requirement
 * Responsibilities:
 * - Store assignment relationship data
 * - Provide display format for CLI
 * - Provide file format for storage
 */
public class Assignment {
    private final String assignmentId;
    private final String teacherId;
    private final String requirementId;
    private final LocalDateTime assignTime;
    private final String term;

    // Constructor
    public Assignment(String teacherId, String requirementId, String term) {
        this.assignmentId = generateAssignmentId();
        this.teacherId = teacherId;
        this.requirementId = requirementId;
        this.assignTime = LocalDateTime.now();
        this.term = term;
    }

    // Full Index of Constructor
    public Assignment(String assignmentId, String teacherid, String requirementId, LocalDateTime assignTime, String term) {
        this.assignmentId = assignmentId;
        this.teacherId = teacherid;
        this.requirementId = requirementId;
        this.assignTime = assignTime;
        this.term = term;
    }

    // Getter
    public String getAssignmentId() {
        return assignmentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public LocalDateTime getAssignTime() {
        return assignTime;
    }


    public String getTerm() {
        return term;
    }

    /**
     * One Format
     */
    public String toDisplayString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("assignmentId: %s | teacherId: %s | requirementId: %s | assignTime: %s | term: %s",
                assignmentId, teacherId, requirementId, assignTime.format(formatter), term);
    }

    /**
     * Simply Format for List
     */
    public String toShortString() {
        return String.format("[%s] teacher:%s - require:%s",
                assignmentId, teacherId, requirementId);
    }

    /**
     * Storage Format for D
     * assignmentId|teacherId|requirementId|assignTime|term
     */
    public String toFileString() {
        return String.join("|",
                assignmentId,
                teacherId,
                requirementId,
                assignTime.toString(),
                term
        );
    }

    /**
     * Analysis from String for D
     */
    public static Assignment fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid assignment file format: " + line);
        }

        String assignmentId = parts[0];
        String teacherId = parts[1];
        String requirementId = parts[2];
        LocalDateTime assignTime = LocalDateTime.parse(parts[3]);
        String term = parts[4];

        return new Assignment(assignmentId, teacherId, requirementId, assignTime, term);
    }

    /**
     * Generate Unique Id
     */
    private static String generateAssignmentId() {
        return "ASN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(assignmentId, that.assignmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentId);
    }

    @Override
    public String toString() {
        return toDisplayString();
    }

}
