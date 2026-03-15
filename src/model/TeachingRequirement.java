package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Responsibilities:
 * - store teaching requirement-related data
 * - provide requirement-related validation/query methods
 * - provide a display-friendly string for CLI output
 */
public class TeachingRequirement {

    private final String requirementId;
    private String courseName;
    private String term;
    private final Set<String> requiredSkills;
    private int teachersNeeded;
    private int hoursPerWeek;

    /**
     * Creates a TeachingRequirement object.
     *
     * @param requirementId unique requirement identifier
     * @param courseName course name
     * @param term academic term
     * @param requiredSkills skills required for this requirement
     * @param teachersNeeded number of teachers needed
     * @param hoursPerWeek teaching hours per week
     */
    public TeachingRequirement(String requirementId, String courseName, String term,
                               Set<String> requiredSkills, int teachersNeeded, int hoursPerWeek) {
        validateRequirementId(requirementId);
        validateCourseName(courseName);
        validateTerm(term);
        validateTeachersNeeded(teachersNeeded);
        validateHoursPerWeek(hoursPerWeek);

        this.requirementId = requirementId.trim();
        this.courseName = courseName.trim();
        this.term = term.trim();
        this.requiredSkills = normaliseSet(requiredSkills);
        this.teachersNeeded = teachersNeeded;
        this.hoursPerWeek = hoursPerWeek;
    }

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
     * Returns an unmodifiable copy-like view to protect encapsulation.
     */
    public Set<String> getRequiredSkills() {
        return Collections.unmodifiableSet(requiredSkills);
    }
    public int getTeachersNeeded() {
        return teachersNeeded;
    }
    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    /**
     * Updates course name with validation.
     *
     * @param courseName new course name
     */
    public void setCourseName(String courseName) {
        validateCourseName(courseName);
        this.courseName = courseName.trim();
    }

    /**
     * Updates term with validation.
     *
     * @param term new academic term
     */
    public void setTerm(String term) {
        validateTerm(term);
        this.term = term.trim();
    }

    /**
     * Updates number of teachers needed.
     *
     * @param teachersNeeded number of teachers
     */
    public void setTeachersNeeded(int teachersNeeded) {
        validateTeachersNeeded(teachersNeeded);
        this.teachersNeeded = teachersNeeded;
    }

    /**
     * Updates teaching hours per week.
     *
     * @param hoursPerWeek hours per week
     */
    public void setHoursPerWeek(int hoursPerWeek) {
        validateHoursPerWeek(hoursPerWeek);
        this.hoursPerWeek = hoursPerWeek;
    }

    /**
     * Checks whether this requirement needs a given skill.
     *
     * @param skill skill name
     * @return true if required
     */
    public boolean requiresSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return false;
        }
        return requiredSkills.contains(skill.trim().toLowerCase());
    }

    /**
     * Adds a required skill.
     *
     * @param skill skill to add
     * @return true if added successfully
     */
    public boolean addRequiredSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return false;
        }
        return requiredSkills.add(skill.trim().toLowerCase());
    }

    /**
     * Removes a required skill.
     *
     * @param skill skill to remove
     * @return true if removed successfully
     */
    public boolean removeRequiredSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return false;
        }
        return requiredSkills.remove(skill.trim().toLowerCase());
    }

    /**
     * Returns a human-readable string for CLI display.
     */
    public String toDisplayString() {
        return "Requirement ID: " + requirementId
                + " | Course: " + courseName
                + " | Term: " + term
                + " | Required Skills: " + requiredSkills
                + " | Teachers Needed: " + teachersNeeded
                + " | Hours/Week: " + hoursPerWeek;
    }

    @Override
    public String toString() {
        return toDisplayString();
    }

    /**
     * Equality is based on requirementId because it is the unique identifier.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TeachingRequirement)) {
            return false;
        }
        TeachingRequirement other = (TeachingRequirement) obj;
        return Objects.equals(this.requirementId, other.requirementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirementId);
    }

    // ----------------------------
    // Private helper methods
    // ----------------------------

    private void validateRequirementId(String requirementId) {
        if (requirementId == null || requirementId.trim().isEmpty()) {
            throw new IllegalArgumentException("Requirement ID cannot be null or empty.");
        }
    }
    private void validateCourseName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }
    }
    private void validateTerm(String term) {
        if (term == null || term.trim().isEmpty()) {
            throw new IllegalArgumentException("Term cannot be null or empty.");
        }
    }
    private void validateTeachersNeeded(int teachersNeeded) {
        if (teachersNeeded <= 0) {
            throw new IllegalArgumentException("Teachers needed cannot be less than zero.");
        }
    }
    private void validateHoursPerWeek(int hoursPerWeek) {
        if (hoursPerWeek <= 0) {
            throw new IllegalArgumentException("Hours per week cannot be less than zero.");
        }
    }

    /**
     * Normalises input strings:
     * - null-safe
     * - trims spaces
     * - converts to lowercase
     * - removes empty values
     */
    private Set<String> normaliseSet(Set<String> values) {
        Set<String> result = new HashSet<>();
        if (values == null) {
            return result;
        }

        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                result.add(value.trim().toLowerCase());
            }
        }

        return result;
    }
}
