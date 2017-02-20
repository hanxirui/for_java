package app.service;

import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import app.dao.StudentDao;
import app.entity.Student;


@Service
public class StudentService extends CrudService<Student, StudentDao> {
}
