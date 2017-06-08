package com.solaris.android_third_party.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.solaris.android_third_party.BaseActivity;
import com.solaris.android_third_party.DividerItemDecoration;
import com.solaris.android_third_party.R;
import com.solaris.android_third_party.adapter.TeacherAdapter;
import com.solaris.android_third_party.factory.DialogFactory;
import com.solaris.android_third_party.factory.ServiceFactory;
import com.solaris.android_third_party.factory.TeacherFactory;
import com.solaris.android_third_party.model.Student;
import com.solaris.android_third_party.model.StudentDao;
import com.solaris.android_third_party.model.Teacher;
import com.solaris.android_third_party.model.TeacherDao;
import com.solaris.android_third_party.service.DBService;

import java.util.List;

/**
 * Created by hanxirui on 2016/12/23.
 */

public class TeacherActivity extends BaseActivity {


    private TeacherAdapter teacherAdapter;
    private TeacherFactory teacherFactory;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_teacher));
        setSupportActionBar(toolbar);

        setupRecycleView();
        setupFAB();
        teacherFactory = new TeacherFactory();
        DBService dbService = ServiceFactory.getDbService();
        teacherDao = dbService.getTeacherDao();
    }

    private void setupRecycleView() {
        RecyclerView recycleStudent = (RecyclerView) findViewById(R.id.recycleTeacher);

        teacherAdapter = new TeacherAdapter();
        teacherAdapter.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onClick(Teacher teacher) {
                DBService dbService = ServiceFactory.getDbService();
                StudentDao studentDao = dbService.getStudentDao();
                List<Student> students = studentDao.loadAll();
                //TODO reset to get latest students
                teacher.resetStudents();
                AlertDialog dialog = DialogFactory.getStudentDialog(TeacherActivity.this, teacher, students);
                dialog.show();
            }
        });
        recycleStudent.setAdapter(teacherAdapter);
        recycleStudent.setLayoutManager(new LinearLayoutManager(this));
        recycleStudent.addItemDecoration(new DividerItemDecoration(this));
    }

    private void setupFAB() {
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddTeacher();
            }
        });
    }

    private void onAddTeacher() {
        Teacher teacher = teacherFactory.generateTeacher();
        long id = teacherDao.insert(teacher);

        teacher = teacherDao.load(id);
        teacherAdapter.addTeacher(teacher);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Teacher> teachers = teacherDao.loadAll();
        teacherAdapter.setTeachers(teachers);
    }
}
