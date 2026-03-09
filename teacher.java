import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * Responsibilities:
 * - store teacher-related data
 * - provide teacher-related validation/query methods
 * - provide a display-friendly string for CLI output
 *
 */
public class Teacher {

    private final String teacherId;
    private String name;
    private final Set<String> skills;
    private final Set<String> trainingsCompleted;

    /**
     * Creates a Teacher object.
     *
     * @param teacherId unique teacher identifier
     * @param name teacher name
     * @param skills teacher skills
     * @param trainingsCompleted completed trainings
     */
    public Teacher(String teacherId, String name, Set<String> skills, Set<String> trainingsCompleted) {
        validateTeacherId(teacherId);
        validateName(name);

        this.teacherId = teacherId.trim();
        this.name = name.trim();
        this.skills = normaliseSet(skills);
        this.trainingsCompleted = normaliseSet(trainingsCompleted);
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable copy-like view to protect encapsulation.
     */
    public Set<String> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    /**
     * Returns completed trainings.
     */
    public Set<String> getTrainingsCompleted() {
        return Collections.unmodifiableSet(trainingsCompleted);
    }

    /**
     * Updates teacher name with validation.
     *
     * @param name new teacher name
     */
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    /**
     * Checks whether the teacher has a given skill.
     *
     * @param skill skill name
     * @return true if the teacher has the skill
     */
    public boolean hasSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return false;
        }
        return skills.contains(skill.trim().toLowerCase());
    }

    /**
     * Checks whether the teacher has completed a training.
     *
     * @param training training name
     * @return true if completed
     */
    public boolean hasCompletedTraining(String training) {
        if (training == null || training.trim().isEmpty()) {
            return false;
        }
        return trainingsCompleted.contains(training.trim().toLowerCase());
    }

    /**
     * Adds a skill to the teacher.
     *
     * @param skill skill to add
     * @return true if added successfully, false if invalid or already exists
     */
    public boolean addSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return false;
        }
        return skills.add(skill.trim().toLowerCase());
    }

    /**
     * Adds a completed training record.
     *
     * @param training training to add
     * @return true if added successfully, false if invalid or already exists
     */
    public boolean addTraining(String training) {
        if (training == null || training.trim().isEmpty()) {
            return false;
        }
        return trainingsCompleted.add(training.trim().toLowerCase());
    }

    /**
     * This method is kept simple for the current coursework version.
     * Since your agreed design does not include a timetable/availability model,
     * this method currently returns true for any non-empty term.
     *
     * It preserves interface compatibility for future extension
     * without coupling Teacher to scheduling logic.
     *
     * @param term academic term
     * @return true if term is non-empty
     */
    public boolean isAvailableFor(String term) {
        return term != null && !term.trim().isEmpty();
    }

    /**
     * Returns a human-readable string for CLI display.
     */
    public String toDisplayString() {
        return "Teacher ID: " + teacherId
                + " | Name: " + name
                + " | Skills: " + skills
                + " | Trainings Completed: " + trainingsCompleted;
    }

    @Override
    public String toString() {
        return toDisplayString();
    }

    /**
     * Equality is based on teacherId because it is the unique identifier.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Teacher)) {
            return false;
        }
        Teacher other = (Teacher) obj;
        return Objects.equals(this.teacherId, other.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId);
    }

    // --------------------------
    // Private helper methods
    // --------------------------

    private void validateTeacherId(String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher ID cannot be null or empty.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher name cannot be null or empty.");
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