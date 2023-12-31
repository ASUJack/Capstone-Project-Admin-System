package dev.capstone.asu.Capstone.Project.Admin.System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {

    public Long getId() { return id; }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCohort() {
        return cohort;
    }

    public void setCohort(String cohort) {
        this.cohort = cohort;
    }

    public String getOrganizationClassification() {
        return organizationClassification;
    }

    public void setOrganizationClassification(String organizationClassification) {
        this.organizationClassification = organizationClassification;
    }

    public String getIntellectualPropertyConcerns() {
        return intellectualPropertyConcerns;
    }

    public void setIntellectualPropertyConcerns(String intellectualPropertyConcerns) {
        this.intellectualPropertyConcerns = intellectualPropertyConcerns;
    }

    public String getProjectResourcesProvided() {
        return projectResourcesProvided;
    }

    public void setProjectResourcesProvided(String projectResourcesProvided) {
        this.projectResourcesProvided = projectResourcesProvided;
    }

    public String getDedicatedContact() {
        return dedicatedContact;
    }

    public void setDedicatedContact(String dedicatedContact) {
        this.dedicatedContact = dedicatedContact;
    }

    public String getProposerOrganization() {
        return proposerOrganization;
    }

    public void setProposerOrganization(String proposerOrganization) {
        this.proposerOrganization = proposerOrganization;
    }

    public String getProposerName() {
        return proposerName;
    }

    public void setProposerName(String proposerName) {
        this.proposerName = proposerName;
    }

    public String getProposerEmail() {
        return proposerEmail;
    }

    public void setProposerEmail(String proposerEmail) {
        this.proposerEmail = proposerEmail;
    }

    public String getProjectContactName() {
        return projectContactName;
    }

    public void setProjectContactName(String projectContactName) {
        this.projectContactName = projectContactName;
    }

    public String getProjectContactEmail() {
        return projectContactEmail;
    }

    public void setProjectContactEmail(String projectContactEmail) {
        this.projectContactEmail = projectContactEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudentLearningExperience() {
        return studentLearningExperience;
    }

    public void setStudentLearningExperience(String studentLearningExperience) {
        this.studentLearningExperience = studentLearningExperience;
    }

    public String getExpectedDeliverables() {
        return expectedDeliverables;
    }

    public void setExpectedDeliverables(String expectedDeliverables) {
        this.expectedDeliverables = expectedDeliverables;
    }

    public String getDesiredBackground() {
        return desiredBackground;
    }

    public void setDesiredBackground(String desiredBackground) {
        this.desiredBackground = desiredBackground;
    }

    public String getProjectFocus() {
        return projectFocus;
    }

    public void setProjectFocus(String projectFocus) {
        this.projectFocus = projectFocus;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public String getRequiredAgreements() {
        return requiredAgreements;
    }

    public void setRequiredAgreements(String requiredAgreements) {
        this.requiredAgreements = requiredAgreements;
    }

    public String getProjectLinks() {
        return projectLinks;
    }

    public void setProjectLinks(String projectLinks) {
        this.projectLinks = projectLinks;
    }

    public String getSponsorName() { return sponsorName; }

    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }

    public String getSponsorEmail() { return sponsorEmail; }

    public void setSponsorEmail(String sponsorEmail) { this.sponsorEmail = sponsorEmail; }

    public List<String> getProposedTeamAsuriteIds() { return proposedTeamAsuriteIds; }

    public void setProposedTeamAsuriteIds(List<String> proposedTeamAsuriteIds) { this.proposedTeamAsuriteIds = proposedTeamAsuriteIds; }

    public List<String> getProposedTeamEmails() { return proposedTeamEmails; }

    public void setProposedTeamEmails(List<String> proposedTeamEmails) { this.proposedTeamEmails = proposedTeamEmails; }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    public void setCoordinatorEmail(String coordinatorEmail) {
        this.coordinatorEmail = coordinatorEmail;
    }

    public List<Long> getAssignedStudents() {
        return assignedStudents;
    }

    public void setAssignedStudents(List<Long> assignedStudents) {
        this.assignedStudents = assignedStudents;
    }

    public boolean addAssignedStudent(Long studentId)
    {
        return this.assignedStudents.add(studentId);
    }

    public boolean atCapacity()
    {
        return (assignedStudents.size() >= maxTeamSize);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    private String timestamp;

    @Column(columnDefinition = "TEXT")
    private String cohort;

    private String organizationClassification;

    private String intellectualPropertyConcerns;

    private String projectResourcesProvided;

    private String dedicatedContact;

    @Column(columnDefinition = "TEXT")
    private String proposerOrganization;

    private String proposerName;

    private String proposerEmail;

    private String projectContactName;

    private String projectContactEmail;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String studentLearningExperience;

    @Column(columnDefinition = "TEXT")
    private String expectedDeliverables;

    @Column(columnDefinition = "TEXT")
    private String desiredBackground;

    @Column(columnDefinition = "TEXT")
    private String projectFocus;

    private int maxTeamSize;

    @Column(columnDefinition = "TEXT")
    private String requiredAgreements;

    @Column(columnDefinition = "TEXT")
    private String projectLinks;

    private String sponsorName;

    private String sponsorEmail;

    private List<String> proposedTeamAsuriteIds;

    private List<String> proposedTeamEmails;

    private String coordinatorName;

    private String coordinatorEmail;

    private List<Long> assignedStudents;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", cohort='" + cohort + '\'' +
                ", organizationClassification='" + organizationClassification + '\'' +
                ", intellectualPropertyConcerns='" + intellectualPropertyConcerns + '\'' +
                ", projectResourcesProvided='" + projectResourcesProvided + '\'' +
                ", dedicatedContact='" + dedicatedContact + '\'' +
                ", proposerOrganization='" + proposerOrganization + '\'' +
                ", proposerName='" + proposerName + '\'' +
                ", proposerEmail='" + proposerEmail + '\'' +
                ", projectContactName='" + projectContactName + '\'' +
                ", projectContactEmail='" + projectContactEmail + '\'' +
                ", description='" + description + '\'' +
                ", studentLearningExperience='" + studentLearningExperience + '\'' +
                ", expectedDeliverables='" + expectedDeliverables + '\'' +
                ", desiredBackground='" + desiredBackground + '\'' +
                ", projectFocus='" + projectFocus + '\'' +
                ", maxTeamSize=" + maxTeamSize +
                ", requiredAgreements='" + requiredAgreements + '\'' +
                ", projectLinks='" + projectLinks + '\'' +
                ", sponsorName='" + sponsorName + '\'' +
                ", sponsorEmail='" + sponsorEmail + '\'' +
                ", proposedTeamAsuriteIds=" + proposedTeamAsuriteIds +
                ", proposedTeamEmails=" + proposedTeamEmails +
                ", coordinatorName='" + coordinatorName + '\'' +
                ", coordinatorEmail='" + coordinatorEmail + '\'' +
                ", assignedStudents=" + assignedStudents +
                '}';
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Project p)) return false;
        return this.getId().equals(p.getId());
    }
}
