package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.*;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.ProjectRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.StudentRepo;
import jakarta.annotation.Nullable;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Optional<Student> updateStudent(Long id, Student student)
    {
        Optional<Student> foundStudent = this.findStudentById(id);
        if (foundStudent.isEmpty()) return foundStudent;
        Student updatedStudent = foundStudent.get();
        updatedStudent.setAsuriteID(student.getAsuriteID());
        updatedStudent.setFirstName(student.getFirstName());
        updatedStudent.setLastName(student.getLastName());
        updatedStudent.setEmail(student.getEmail());
        updatedStudent.setSignupTimestamp(student.getSignupTimestamp());
        updatedStudent.setProjectPreferences(student.getProjectPreferences());
        updatedStudent.setAssignedProject(student.getAssignedProject());
        return Optional.of(studentRepo.save(updatedStudent));
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
        List<Student> students = this.findAllStudents();
        students.sort(new StudentTimestampComparator());

        for (Student student : students)
        {
            System.out.println("Student: " + student.toString());
        }

        studentRepo.saveAll(students);
    }
}
