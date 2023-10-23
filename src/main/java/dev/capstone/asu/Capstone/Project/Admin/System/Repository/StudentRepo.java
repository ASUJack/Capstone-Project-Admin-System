package dev.capstone.asu.Capstone.Project.Admin.System.Repository;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long>
{

}
