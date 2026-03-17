package model;

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

    public Set<String> getRequiredSkills() {
        return Collections.unmodifiableSet(requiredSkills);
    }

    public int getTeachersNeeded() {
        return teachersNeeded;
    }

    public int getAssignedCount() {
        return assignedCount;
    }

    public boolean isFulfilled() {
        return assignedCount >= teachersNeeded;
    }

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

    public void setAssignedCount(int assignedCount) {
        if (assignedCount < 0) {
            throw new IllegalArgumentException("Assigned cannot be negative: " + assignedCount);
        }
        if (assignedCount > teachersNeeded) {
            throw new IllegalArgumentException("Assigned count cannot exceed teachers needed");
        }
        this.assignedCount = assignedCount;
    }

    //==============================  Business Methods  ==============================

    public boolean requiresSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        return requiredSkills.contains(skill.trim().toLowerCase());
    }

    public boolean addRequiredSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        return requiredSkills.add(skill.trim().toLowerCase());
    }

    public boolean removeRequiredSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        return requiredSkills.remove(skill.trim().toLowerCase());
    }

    public boolean incrementAssignedCount() {
        if (isFulfilled()) return false;
        assignedCount++;
        return true;
    }

    public boolean decrementAssignedCount() {
        if (assignedCount <= 0) return false;
        assignedCount--;
        return true;
    }

    //==============================  Display Methods  ==============================

    public String toDisplayString() {
        return String.format("requirementId: %s | courseName: %s | term: %s | requiredSkills: %s | teachersNeeded: %d/%d %s",
                requirementId, courseName, term,
<<<<<<< HEAD
                requiredSkills.isEmpty() ? "无" : requiredSkills,
                assignedCount, teachersNeeded,
                isFulfilled() ? "已满" : "缺" + getRemainingNeeded()
=======
                requiredSkills.isEmpty() ? "empty" : requiredSkills,
                assignedCount, teachersNeeded,
                isFulfilled() ? "full" : "lack" + getRemainingNeeded()
>>>>>>> 1fd0404 (test)
        );
    }

    public String toShortString() {
        return String.format("[%s] %s (%s) %d/%d",
                requirementId, courseName, term, assignedCount, teachersNeeded);
    }

    /**
     * File storage format for RequirementRepository
     */
    public String toFileString() {
        String skillsStr = String.join(",", requiredSkills);
        return String.join("|",
                requirementId, courseName, term, skillsStr,
                String.valueOf(teachersNeeded), String.valueOf(assignedCount)
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