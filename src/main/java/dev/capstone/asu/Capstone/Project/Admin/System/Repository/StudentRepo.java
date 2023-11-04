package dev.capstone.asu.Capstone.Project.Admin.System.Repository;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long>
{
    default Optional<Student> findByASURite(String asurite)
    {
        for (Student student: this.findAll())
        {
            if(student.getAsuriteID().equals(asurite))
            {
                return Optional.of(student);
            }
        }

        return Optional.empty();
    }
}
