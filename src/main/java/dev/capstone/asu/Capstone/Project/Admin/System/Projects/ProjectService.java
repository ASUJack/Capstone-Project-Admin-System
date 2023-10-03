package dev.capstone.asu.Capstone.Project.Admin.System.Projects;

import dev.capstone.asu.Capstone.Project.Admin.System.Students.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo) { this.projectRepo = projectRepo; }

    public Project addProject(Project project) { return projectRepo.save(project); }

    public List<Project> getAllProjects() { return projectRepo.findAll(); }

    public Project findById(Long id)
    {
        return projectRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project with id " + id + " not found!"));
    }

    public void deleteProject(Long id) { projectRepo.deleteById(id); }
}
