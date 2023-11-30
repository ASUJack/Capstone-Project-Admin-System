package dev.capstone.asu.Capstone.Project.Admin.System.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProjectAssignmentsToSend
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<Long> newlyAssignedProjects;

    public Long getId() {
        return id;
    }

    public List<Long> getNewlyAssignedProjects() {
        return newlyAssignedProjects;
    }

    public void setNewlyAssignedProjects(List<Long> newlyAssignedProjects) {
        this.newlyAssignedProjects = newlyAssignedProjects;
    }

    @Override
    public String toString() {
        return "ProjectAssignmentsToSend{" +
                "newlyAssignedProjects=" + newlyAssignedProjects +
                '}';
    }
}
