package dev.capstone.asu.Capstone.Project.Admin.System.Projects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getSponsorFirstName() {
        return sponsorFirstName;
    }

    public String getSponsorLastName() {
        return sponsorLastName;
    }

    public String getSponsorEmail() {
        return sponsorEmail;
    }

    public String getCoordinatorFirstName() {
        return coordinatorFirstName;
    }

    public String getCoordinatorLastName() {
        return coordinatorLastName;
    }

    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    public long[] getAssignedStudents() {
        return assignedStudents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSponsorFirstName(String sponsorFirstName) {
        this.sponsorFirstName = sponsorFirstName;
    }

    public void setSponsorLastName(String sponsorLastName) {
        this.sponsorLastName = sponsorLastName;
    }

    public void setSponsorEmail(String sponsorEmail) {
        this.sponsorEmail = sponsorEmail;
    }

    public void setCoordinatorFirstName(String coordinatorFirstName) {
        this.coordinatorFirstName = coordinatorFirstName;
    }

    public void setCoordinatorLastName(String coordinatorLastName) {
        this.coordinatorLastName = coordinatorLastName;
    }

    public void setCoordinatorEmail(String coordinatorEmail) {
        this.coordinatorEmail = coordinatorEmail;
    }

    public void setAssignedStudents(long[] assignedStudents) {
        this.assignedStudents = assignedStudents;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", description='" + description + '\'' +
                ", sponsorFirstName='" + sponsorFirstName + '\'' +
                ", sponsorLastName='" + sponsorLastName + '\'' +
                ", sponsorEmail='" + sponsorEmail + '\'' +
                ", coordinatorFirstName='" + coordinatorFirstName + '\'' +
                ", coordinatorLastName='" + coordinatorLastName + '\'' +
                ", coordinatorEmail='" + coordinatorEmail + '\'' +
                ", assignedStudents='" + Arrays.toString(assignedStudents) +
                "}";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 32)
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String sponsorFirstName;

    private String sponsorLastName;

    private String sponsorEmail;

    private String coordinatorFirstName;

    private String coordinatorLastName;

    private String coordinatorEmail;

    private long[] assignedStudents;

}
