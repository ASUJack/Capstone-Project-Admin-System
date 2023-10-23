package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Project;
import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Student;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.ProjectRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.StudentRepo;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        studentRepo.deleteById(id);
    }

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
        projectRepo.deleteById(id);
    }
}
