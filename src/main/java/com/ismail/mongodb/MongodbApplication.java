package com.ismail.mongodb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class MongodbApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(MongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate)
    {
        return args ->
        {
            Address address = new Address("England", "London", "NE9");

            Student student = new Student(
                    "Jamila",
                    "Ahmed",
                    "jahmed@gmail.com",
                    Gender.FEMALE,
                    address,
                    List.of("Computer Science", "Math"),
                    BigDecimal.TEN,
                    LocalDateTime.now());


            String email = student.getEmail();

            // Using custom query with mongoTemplate
            //boolean foundStudent = findStudentByEmailUsingTemplateAndQuery(mongoTemplate, email);

            // using Repository
            Optional<Student> studentOptional = repository.findStudentByEmail(email);
            boolean foundStudent = studentOptional.isPresent();

            if (foundStudent)
            {
                System.err.println("Students with email [" + student.getEmail() + "] already exists!");

                //throw new IllegalStateException("Email already exists: " + student.getEmail());
            }
            else
            {
                repository.insert(student);
            }


        };

    }

    private boolean findStudentByEmailUsingTemplateAndQuery(MongoTemplate mongoTemplate, String email)
    {
        // Running custom queries with MongoTemplate
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        boolean foundStudent = (students.size() > 0);

        return foundStudent;
    }
}
