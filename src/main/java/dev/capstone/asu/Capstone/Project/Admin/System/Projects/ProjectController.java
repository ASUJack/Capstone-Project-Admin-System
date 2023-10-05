package dev.capstone.asu.Capstone.Project.Admin.System.Projects;

import dev.capstone.asu.Capstone.Project.Admin.System.Students.Student;
import dev.capstone.asu.Capstone.Project.Admin.System.Students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects()
    {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") Long id)
    {
        Project project = projectService.findById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Project> addStudent(@RequestBody Project project)
    {
        Project newProject = projectService.addProject(project);
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody Project project)
    {
        Project updatedProject = projectService.findById(id);
        updatedProject.setName(project.getName());
        updatedProject.setDuration(project.getDuration());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setSponsorFirstName(project.getSponsorFirstName());
        updatedProject.setSponsorLastName(project.getSponsorLastName());
        updatedProject.setSponsorEmail(project.getSponsorEmail());
        updatedProject.setCoordinatorFirstName(project.getCoordinatorFirstName());
        updatedProject.setCoordinatorLastName(project.getCoordinatorLastName());
        updatedProject.setCoordinatorEmail(project.getCoordinatorEmail());
        updatedProject.setAssignedStudents(project.getAssignedStudents());

        projectService.addProject(updatedProject);
        return new ResponseEntity<>(updatedProject, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") Long id)
    {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("findByYear/{year}")
    public ResponseEntity<List<Project>> findByYear(@PathVariable String year)
    {
        List<Project> projects = projectService.findByYear(year);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
