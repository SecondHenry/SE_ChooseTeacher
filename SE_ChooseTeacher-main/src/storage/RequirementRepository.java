package storage;

import model.TeachingRequirement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository class for managing TeachingRequirement data
 * Uses FileStorage to read from and write to a text file
 * <p>
 * File format: requirementId|courseName|term|skill1,skill2|teachersNeeded|assignedCount
 * Example: R001|Java programming|2025 Spring|Java,Spring|2|1
 */
public class RequirementRepository {

    private static final String FILE_PATH = "data/requirement.txt";
    private List<TeachingRequirement> requirements;

    public RequirementRepository() {
        this.requirements = new ArrayList<>();
    }

    /**
     * Loads All requirements from file
     */
    public void loadAll() {
        requirements.clear();
        List<String> lines = FileStorage.readLines(FILE_PATH);

        for (String line : lines) {
            try {
                TeachingRequirement req = TeachingRequirement.fromFileString(line);
                requirements.add(req);
            } catch (Exception e) {
                System.err.println("Skip invalid requirement records：" + line);
            }
        }
        System.out.println("Loaded " + requirements.size() + "demand records ");
    }

    /**
     * Saves all requirements to file
     */
    public void saveAll() {
        List<String> lines = requirements.stream()
                .map(TeachingRequirement::toFileString)
                .collect(Collectors.toList());
        FileStorage.writeLines(FILE_PATH, lines);
    }

    /**
     * Adds a new requirement
     */
    public void addRequirement(TeachingRequirement requirement) {
        requirements.add(requirement);
        saveAll();
    }

    /**
     * Returns all requirements
     */
    public List<TeachingRequirement> getAllRequirement() {
        return new ArrayList<>(requirements);
    }

    /**
     * Finds requirement by ID
     */
    public TeachingRequirement getRequirementById(String requirementId) {
        return requirements.stream()
                .filter(r -> r.getRequirementId().equals(requirementId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds requirement by term
     */
    public List<TeachingRequirement> getRequirementsByTerm(String term) {
        return requirements.stream()
                .filter(r -> r.getTerm().equals(term))
                .collect(Collectors.toList());
    }

    /**
     * Finds unfulfilled requirements
     */
    public List<TeachingRequirement> getUnfulfilledRequirements() {
        return requirements.stream()
                .filter(r -> !r.isFulfilled())
                .collect(Collectors.toList());
    }

    /**
     * Updates a requirement's assigned count
     * Used by AssignmentService
     */
    public void updateAssignedCount(String requirementId, int newCount) {
        TeachingRequirement req = getRequirementById(requirementId);
        if (req != null) {
            req.setAssignedCount(newCount);
            saveAll();
        }
    }

    /**
     * Deletes a requirement by ID
     */
    public boolean deleteRequirement(String requirementId) {
        boolean removed = requirements.removeIf(r -> r.getRequirementId().equals(requirementId));
        if (removed) {
            saveAll();
        }
        return removed;
    }
}
