package model;

import javax.management.StringValueExp;
import java.util.*;

/**
 * Represents a teaching requirement (course demand) for a specific term
 * 
 * Responsibilities:
 * - Store teaching requirement data (course, term, required skills, etc.)
 * - Provide validation methods
 * - Track fulfillment status (how many teachers needed vs. assigned)
 * - Provide display format for CLI output
 * - Provide file format for storage
 */
public class TeachingRequirement {

    private final String requirementId;
    private String courseName;
    private String term;
    private final Set<String> requiredSkills;
    private int teachersNeeded;
    private int assignedCount;  //track how many teachers already assigned

    /**
     * Creates a new TeachingRequirement with auto-generated ID
     *
     * @param courseName     Name of the course
     * @param term           Academic term (e.g., "2025春")
     * @param requiredSkills Set of skills needed
     * @param teachersNeeded Number of teachers required
     */
    public TeachingRequirement(String courseName, String term,
                               Set<String> requiredSkills, int teachersNeeded) {
        this.requirementId = generateRequirementId();
        setCourseName(courseName);
        setTerm(term);
        this.requiredSkills = normaliseSet(requiredSkills);
        setTeachersNeeded(teachersNeeded);
        this.assignedCount = 0;
    }

    /**
     * Full constructor for loading from file
     *
     * @param requirementId  Existing requirement ID
     * @param courseName     Name of the course
     * @param term           Academic term (e.g., "2025春")
     * @param requiredSkills Set of skills needed
     * @param teachersNeeded Number of teachers required
     * @param assignedCount  Current number of assigned teachers
     */
    public TeachingRequirement(String requirementId, String courseName,
                               String term, Set<String> requiredSkills,
                               int teachersNeeded, int assignedCount) {
        this.requirementId = requirementId;
        setCourseName(courseName);
        setTerm(term);
        this.requiredSkills = normaliseSet(requiredSkills);
        setTeachersNeeded(teachersNeeded);
        setAssignedCount(assignedCount);
    }

    //==============================  Getters  ==============================

    public String getRequirementId() {
        return requirementId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTerm() {
        return term;
    }

    /**
     * Returns an unmodifiable view of required skills
     */
    public Set<String> getRequiredSkills() {
        return Collections.unmodifiableSet(requiredSkills);
    }

    public int getTeachersNeeded() {
        return teachersNeeded;
    }

    public int getAssignedCount() {
        return assignedCount;
    }

    /**
     * Checks if the requirement is fully staffed
     *
     * @return true if assignedCount >= teachersNeeded
     */
    public boolean isFulfilled() {
        return assignedCount >= teachersNeeded;
    }

    /**
     * Returns how many more teachers are needed
     *
     * @return teachersNeeded -assignedCount
     */
    public int getRemainingNeeded() {
        return Math.max(0, teachersNeeded - assignedCount);
    }

    //==============================  Setters with validation  ==============================

    public void setCourseName(String courseName) {
        validateCourseName(courseName);
        this.courseName = courseName.trim();
    }

    public void setTerm(String term) {
        validateTerm(term);
        this.term = term.trim();
    }

    public void setTeachersNeeded(int teachersNeeded) {
        if (teachersNeeded <= 0) {
            throw new IllegalArgumentException("Teachers needed must be positive, got: " + teachersNeeded);
        }
        this.teachersNeeded = teachersNeeded;
    }

    /**
     * Sets assigned count with validation
     * Used when loading from file
     */
    public void setAssignedCount(int assignedCount) {
        if (assignedCount <= 0) {
            throw new IllegalArgumentException("Assigned cannot be negative: " + assignedCount);
        }
        if (assignedCount > teachersNeeded) {
            throw new IllegalArgumentException("Assigned count (" +
                    assignedCount + ") cannot exceed teachers needed (" + teachersNeeded + ")");
        }
        this.assignedCount = assignedCount;
    }

    //==============================  Business Methods  ==============================

    /**
     * Checks if a specific skill is required
     *
     * @param skill Skill to check
     * @return true if skill is required
     */
    public boolean requiresSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        return requiredSkills.contains(skill.trim().toLowerCase());
    }

    /**
     * Adds a required skill
     *
     * @param skill Skill to add
     * @return true if added successfully
     */
    public boolean addRequiredSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        return requiredSkills.add(skill.trim().toLowerCase());
    }

    /**
     * Removes a required skill
     *
     * @param skill Skill to remove
     * @return true if removed successfully
     */
    public boolean removeRequiredSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        return requiredSkills.remove(skill.trim().toLowerCase());
    }

    /**
     * Increments assigned count when a teacher is assigned
     * Call by AssignmentService
     *
     * @return true if increment was successful (not already fulfilled)
     */
    public boolean incrementAssignedCount() {
        if (isFulfilled()) {
            return false;
        }
        assignedCount++;
        return true;
    }

    /**
     * Decrements assigned count when an assignment is cancelled
     *
     * @return true if decrement was successful
     */
    public boolean decrementAssignedCount() {
        if (assignedCount <= 0) {
            return false;
        }
        assignedCount--;
        return true;
    }

    //==============================  Display Methods  ==============================

    /**
     * Returns a human-readable string for CLI display
     */
    public String toDisplayString() {
        return String.format("requirementId: %s | courseName: %s | term: %s | requiredSkills: %s | teachersNeeded: %d/%d %s",
                requirementId,
                courseName,
                term,
                requiredSkills.isEmpty() ? "无" : requiredSkills,
                assignedCount,
                teachersNeeded,
                isFulfilled() ? "已满" : "缺" + getRemainingNeeded()
        );
    }

    /**
     * Brief display format for lists
     */
    public String toShortString() {
        return String.format("[%s] %s (%s) %d/%d",
                requirementId, courseName, term, assignedCount, teachersNeeded);
    }

    /**
     * File storage format for RequirementRepository
     * Format: requirementId|courseName|term|skill1,skill2|teachersNeeded|assignedCount
     */
    public String toFileString() {
        String skillsStr = String.join(",", requiredSkills);
        return String.join("|",
                requirementId,
                courseName,
                term,
                skillsStr,
                String.valueOf(teachersNeeded),
                String.valueOf(assignedCount)
        );
    }

    /**
     * Creates a TeachingRequirement from file string
     */
    public static TeachingRequirement fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid requirement file format: " + line);
        }

        String requirementId = parts[0];
        String courseName = parts[1];
        String term = parts[2];

        Set<String> skills = new HashSet<>();
        if (!parts[3].isEmpty()) {
            skills.addAll(Set.of(parts[3].split(",")));
        }

        int teachersNeeded = Integer.parseInt(parts[4]);
        int assignedCount = Integer.parseInt(parts[5]);

        return new TeachingRequirement(requirementId, courseName, term,
                skills, teachersNeeded, assignedCount);
    }

    //==============================  Utility Methods  ==============================

    /**
     * Generates a unique requirement ID
     * Format: R + 6 digits/letters
     */
    private static String generateRequirementId() {
        return "R" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private Set<String> normaliseSet(Set<String> values) {
        Set<String> result = new HashSet<>();
        if (values == null) return result;

        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                result.add(value.trim().toLowerCase());
            }
        }
        return result;
    }

    //==============================  Validation Methods  ==============================

    private void validateCourseName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
    }

    private void validateTerm(String term) {
        if (term == null || term.trim().isEmpty()) {
            throw new IllegalArgumentException("Term cannot be null or empty");
        }
    }

    //==============================  Object overrides  ==============================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeachingRequirement that = (TeachingRequirement) o;
        return Objects.equals(requirementId, that.requirementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirementId);
    }

    @Override
    public String toString() {
        return toDisplayString();
    }
}
