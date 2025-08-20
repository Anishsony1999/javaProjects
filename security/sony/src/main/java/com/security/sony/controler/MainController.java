package com.security.sony.controler;

import com.security.sony.model.Student;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MainController {

    private  List<Student> students =  new ArrayList<>(Arrays.asList(
            new Student(1,"Anish","99"),
            new Student(2,"Anusha","98"),
            new Student(3,"Anisha","100")
    ));

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }

    @GetMapping("/students")
    public List<Student> getAllStudents(){
        return students;
    }

    @GetMapping("/csrf")
    public CsrfToken token(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/students")
    public Student adding(@RequestBody Student student){
        students.add(student);
        return student;
    }
}
