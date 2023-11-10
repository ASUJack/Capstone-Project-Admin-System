package dev.capstone.asu.Capstone.Project.Admin.System.Repository;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Project;
import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long>
{
    default List<Project> findAllByYear(String year)
    {
        List<Project> allProjects = this.findAll();
        List<Project> allFromYear = new ArrayList<Project>();

        for (Project project : allProjects) {
            if (project.getCohort().contains(year)) {
                allFromYear.add(project);
            }
        }

        return allFromYear;
    }

    @Query(value = "SELECT * FROM Project p WHERE cardinality(p.assigned_students) < p.max_team_size", nativeQuery = true)
    Collection<Project> findByAssignable();

}
