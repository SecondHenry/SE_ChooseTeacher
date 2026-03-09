package storage;

import model.Teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Repository class for managing Teacher data persistence.
 * Uses FileStorage to read from and write to a text file.
 * Storage format: teacherId,name,skill1;skill2,training1;training2
 */

public class TeacherRepository {

    private static final String FILE_PATH = "teachers.txt";
    private List<Teacher> teachers;

    public TeacherRepository() {
        this.teachers = new ArrayList<>();
    }

    /**
     * Loads all teachers from the text file into memory.
     */
    public void loadAll() {
        teachers.clear();
        List<String> lines = FileStorage.readLines(FILE_PATH);

        for (String line : lines) {
            String[] parts = line.split(",", -1);

            if (parts.length >= 4) {
                String id = parts[0];
                String name = parts[1];

                Set<String> skills = new HashSet<>();
                if (!parts[2].trim().isEmpty()) {
                    skills.addAll(Arrays.asList(parts[2].split(";")));
                }

                Set<String> trainings = new HashSet<>();
                if (!parts[3].trim().isEmpty()) {
                    trainings.addAll(Arrays.asList(parts[3].split(";")));
                }

                // Create the Teacher object
                Teacher teacher = new Teacher(id, name, skills, trainings);
                teachers.add(teacher);
            }
        }
    }

    /**
     * Saves all teachers from memory back to the text file.
     */
    public void saveAll() {
        List<String> lines = new ArrayList<>();

        for (Teacher t : teachers) {
            String skillsStr = String.join(";", t.getSkills());
            String trainingsStr = String.join(";", t.getTrainingsCompleted());

            String line = t.getTeacherId() + "," + t.getName() + "," + skillsStr + "," + trainingsStr;
            lines.add(line);
        }

        FileStorage.writeLines(FILE_PATH, lines);
    }

    /**
     * Adds a new teacher to the repository.
     */
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    /**
     * Returns the list of all teachers.
     */
    public List<Teacher> getAllTeachers() {
        return teachers;
    }

    /**
     * Finds a teacher by their ID.
     */
    public Teacher getTeacherById(String teacherId) {
        for (Teacher t : teachers) {
            if (t.getTeacherId().equals(teacherId)) {
                return t;
            }
        }
        return null;
    }
}