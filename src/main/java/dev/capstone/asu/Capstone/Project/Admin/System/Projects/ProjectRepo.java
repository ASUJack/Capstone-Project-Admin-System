package dev.capstone.asu.Capstone.Project.Admin.System.Projects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long>
{
    default List<Project> findAllByYear(String year)
    {
        List<Project> allProjects = this.findAll();
        List<Project> allFromYear = new ArrayList<Project>();

        for(int i = 0; i < allProjects.size(); i++)
        {
            if(allProjects.get(i).getCohort().contains(year))
            {
                allFromYear.add(allProjects.get(i));
            }
        }

        return allFromYear;
    }
}
