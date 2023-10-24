package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.*;
import dev.capstone.asu.Capstone.Project.Admin.System.ExceptionHandler.AdminApiRequestError;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.ProjectRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.StudentRepo;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.*;

@Service
public class AdminService {

    private final ProjectRepo projectRepo;

    private final StudentRepo studentRepo;

    @Autowired
    public AdminService(ProjectRepo projectRepo, StudentRepo studentRepo)
    {
        this.projectRepo = projectRepo;
        this.studentRepo = studentRepo;
    }


    // =====================================================
    //  STUDENT METHODS
    // =====================================================

    public Student addStudent(Student student)
    {
        return studentRepo.save(student);
    }

    public List<Student> findAllStudents()
    {
        return studentRepo.findAll();
    }

    public Optional<Student> findStudentById(Long id)
    {
        return studentRepo.findById(id);
    }

    public Student updateStudent(Long id, Student student)
    {
        Optional<Student> foundStudent = this.findStudentById(id);
        if (foundStudent.isEmpty()) throw new EntityNotFoundException("Student not found with id = " + id.toString());
        Student updatedStudent = foundStudent.get();
        if (!updatedStudent.getId().equals(student.getId())) throw new InputMismatchException("Path variable id does not match provided student object id");
        return studentRepo.save(student);
    }

    public void deleteStudent(Long id)
    {
        Optional<Student> toDelete = studentRepo.findById(id);
        Long assignedProjectID;
        if(toDelete.isPresent()) {
            Student student = toDelete.get();
            assignedProjectID = student.getAssignedProject();

            if(assignedProjectID != 0) {
                Optional<Project> toUpdate = projectRepo.findById(assignedProjectID);

                if (toUpdate.isPresent()) {
                    Project project = toUpdate.get();
                    List<Long> assignedStudents = project.getAssignedStudents();
                    assignedStudents.remove(id);
                    project.setAssignedStudents(assignedStudents);
                }
            }
        }
        studentRepo.deleteById(id);
    }


    // =====================================================
    //  PROJECT METHODS
    // =====================================================

    public Project addProject(Project project)
    {
        return projectRepo.save(project);
    }

    public Optional<Project> updateProject(Long id, Project project)
    {
        Optional<Project> foundProject = this.findProjectById(id);
        if (foundProject.isEmpty()) return foundProject;
        Project updatedProject = foundProject.get();
        updatedProject.setTimestamp(project.getTimestamp());
        updatedProject.setCohort(project.getCohort());
        updatedProject.setOrganizationClassification(project.getOrganizationClassification());
        updatedProject.setIntellectualPropertyConcerns(project.getIntellectualPropertyConcerns());
        updatedProject.setProjectResourcesProvided(project.getProjectResourcesProvided());
        updatedProject.setDedicatedContact(project.getDedicatedContact());
        updatedProject.setProposerOrganization(project.getProposerOrganization());
        updatedProject.setProposerName(project.getProposerName());
        updatedProject.setProposerEmail(project.getProposerEmail());
        updatedProject.setProjectContactName(project.getProjectContactName());
        updatedProject.setProjectContactEmail(project.getProjectContactEmail());
        updatedProject.setTitle(project.getTitle());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setStudentLearningExperience(project.getStudentLearningExperience());
        updatedProject.setExpectedDeliverables(project.getExpectedDeliverables());
        updatedProject.setDesiredBackground(project.getDesiredBackground());
        updatedProject.setProjectFocus(project.getProjectFocus());
        updatedProject.setMaxTeamSize(project.getMaxTeamSize());
        updatedProject.setRequiredAgreements(project.getRequiredAgreements());
        updatedProject.setProjectLinks(project.getProjectLinks());
        updatedProject.setCoordinatorName(project.getCoordinatorName());
        updatedProject.setCoordinatorEmail(project.getCoordinatorEmail());
        updatedProject.setAssignedStudents(project.getAssignedStudents());
        return Optional.of(projectRepo.save(updatedProject));
    }

    public List<Project> findAllProjects()
    {
        return projectRepo.findAll();
    }

    public Optional<Project> findProjectById(Long id)
    {
        return projectRepo.findById(id);
    }

    public List<Project> findProjectsByYear(String year)
    {
        return projectRepo.findAllByYear(year);
    }

    public void deleteProject(Long id)
    {
        Optional<Project> toDelete = projectRepo.findById(id);
        if(toDelete.isPresent())
        {
            Project project = toDelete.get();
            List<Long> AssignedStudents = project.getAssignedStudents();

            for(Long studentID : AssignedStudents)
            {
                Optional<Student> toUpdate = studentRepo.findById(studentID);
                if(toUpdate.isPresent())
                {
                    Student student = toUpdate.get();
                    student.setAssignedProject(0L);
                }
            }
        }
        projectRepo.deleteById(id);
    }


    // =====================================================
    //  SCRIPT METHODS
    // =====================================================


    public Optional<List<String>> getEmailsByProject(Long id)
    {
        Optional<Project> foundProject = this.findProjectById(id);
        if (foundProject.isEmpty()) return Optional.empty();

        Project project = foundProject.get();
        List<String> emails = new ArrayList<>();

        emails.add(project.getProposerEmail());
        emails.add(project.getProjectContactEmail());
        emails.add(project.getCoordinatorEmail());

        List<Long> assignedStudents = project.getAssignedStudents();
        for(Long assignedStudent : assignedStudents)
        {
            Optional<Student> student = this.findStudentById(assignedStudent);
            student.ifPresent(value -> emails.add(value.getEmail()));
        }

        return Optional.of(emails);
    }

    public Optional<Student> projectSignup(Long id, List<Long> projectIds)
    {
        Optional<Student> foundStudent = this.findStudentById(id);
        if (foundStudent.isEmpty()) return foundStudent;
        Student student = foundStudent.get();
        student.setProjectPreferences(projectIds);
        student.setSignupTimestamp(Instant.now());
        studentRepo.save(student);
        return Optional.of(student);
    }

    public void assignProjects()
    {
        // Fetch and sort all student objects, fetch project objects
        ArrayList<Student> allStudents = new ArrayList<>(this.findAllStudents());
        allStudents.sort(new StudentTimestampComparator());
        ArrayList<Project> allProjects = new ArrayList<>(this.findAllProjects());

        // Temporary storage
        List<Student> unassignedStudents = new ArrayList<>();
        List<Student> assignedStudents = new ArrayList<>();
        List<Project> fullProjects = new ArrayList<>();

        // Temporary variables
        Project tempProject;
        Student tempStudent;

        // Print all sorted student objects to standard output
        for (Student student : allStudents)
        {
            System.out.println("Student: " + student.toString());
        }

        // Assign all students with a signup timestamp
        while (!allStudents.isEmpty() && !allProjects.isEmpty())
        {
            // If no signup timestamp, move to unassigned array
            tempStudent = allStudents.get(0);
            if (Objects.isNull(tempStudent.getSignupTimestamp()))
            {
                unassignedStudents.add(tempStudent);
                allStudents.remove(tempStudent);
                continue;
            }

            // Search project preferences for first available project
            for (Long projectId : tempStudent.getProjectPreferences())
            {
                tempProject = null;
                for (Project p : allProjects)
                {
                    if (projectId.equals(p.getId()))
                    {
                        tempProject = p;
                        break;
                    }
                }
                if (Objects.isNull(tempProject)) continue;
                if (!tempProject.atCapacity())
                {
                    tempProject.addAssignedStudent(tempStudent.getId());
                    tempStudent.setAssignedProject(projectId);
                    assignedStudents.add(tempStudent);
                    allStudents.remove(tempStudent);
                    if (tempProject.atCapacity())
                    {
                        fullProjects.add(tempProject);
                        allProjects.remove(tempProject);
                    }
                    break;
                }
            }

            // If no preferred project available, move to unassigned array
            if (allStudents.contains(tempStudent))
            {
                unassignedStudents.add(tempStudent);
                allStudents.remove(tempStudent);
            }
        }

        // If there are no more available projects, move remaining students to unassigned array
        unassignedStudents.addAll(allStudents);
        System.out.println("After assigning students with timestamps:");
        for (Student s : unassignedStudents)
        {
            System.out.println("Unassigned Student: " + s.toString());
        }
        allStudents.clear();

        // Assign all remaining students with no timestamp
        //  or with no available preferred projects
        while (!unassignedStudents.isEmpty() && !allProjects.isEmpty())
        {
            tempStudent = unassignedStudents.get(0);
            tempProject = allProjects.get(0);

            // Find first available project
            if (tempProject.atCapacity())
            {
                fullProjects.add(tempProject);
                allProjects.remove(tempProject);
                continue;
            }

            tempProject.addAssignedStudent(tempStudent.getId());
            tempStudent.setAssignedProject(tempProject.getId());
            assignedStudents.add(tempStudent);
            unassignedStudents.remove(tempStudent);
        }

        // Merge all temporary storage back into original arrays
        allStudents.addAll(assignedStudents);
        allStudents.addAll(unassignedStudents);
        allProjects.addAll(fullProjects);

        // Write changes to database
        studentRepo.saveAll(allStudents);
        projectRepo.saveAll(allProjects);
    }

    public void clearAssignedProjects()
    {
        List<Student> students = studentRepo.findAll();
        List<Project> projects = projectRepo.findAll();

        for (Student student : students)
        {
            student.setAssignedProject(0L);
        }

        for (Project project : projects)
        {
            project.setAssignedStudents(new ArrayList<Long>());
        }

        studentRepo.saveAll(students);
        projectRepo.saveAll(projects);
    }

}
