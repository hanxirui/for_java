package com.solaris.android_third_party.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.solaris.android_third_party.BaseActivity;
import com.solaris.android_third_party.DividerItemDecoration;
import com.solaris.android_third_party.R;
import com.solaris.android_third_party.adapter.StudentAdapter;
import com.solaris.android_third_party.factory.ServiceFactory;
import com.solaris.android_third_party.factory.StudentFactory;
import com.solaris.android_third_party.model.Student;
import com.solaris.android_third_party.model.StudentDao;
import com.solaris.android_third_party.service.DBService;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by hanxirui on 2016/12/23.
 */

public class StudentActivity extends BaseActivity {

    private StudentAdapter studentAdapter;
    private StudentDao studentDao;
    private StudentFactory studentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_student));
        setSupportActionBar(toolbar);


        setupRecycleView();
        setupFAB();
        studentFactory = new StudentFactory();
        DBService dbService = ServiceFactory.getDbService();
        studentDao = dbService.getStudentDao();



    }



    private void setupRecycleView() {
        RecyclerView recycleStudent = (RecyclerView) findViewById(R.id.recycleStudent);

        studentAdapter = new StudentAdapter();
        recycleStudent.setAdapter(studentAdapter);
        recycleStudent.setLayoutManager(new LinearLayoutManager(this));
        recycleStudent.addItemDecoration(new DividerItemDecoration(this));
    }

    private void setupFAB(){
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onAddStudent();
            }
        });


        FloatingActionButton btnTeacher = (FloatingActionButton)findViewById(R.id.btnTeacher);
        btnTeacher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startTeacherActivity();
            }
        });


    }

    private void startTeacherActivity(){
        Intent intent = new Intent(this, TeacherActivity.class);
        startActivity(intent);
    }



    private void onAddStudent() {
        Student student = studentFactory.generateStudent();
        long id = studentDao.insert(student);

        student = studentDao.load(id);
        studentAdapter.addStudent(student);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Student> students = studentDao.loadAll();
        studentAdapter.setStudents(students);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_age:
                orderByAge();
                return true;
            case R.id.menu_wang:
                queryByWang();
                return true;
            case R.id.menu_old_chang:
                queryByOldChang();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void queryByOldChang() {
        QueryBuilder<Student> studentQueryBuilder = studentDao.queryBuilder();
        Property nameProperty = StudentDao.Properties.Name;
        Property ageProperty = StudentDao.Properties.Age;

        List<Student> students = studentQueryBuilder.where(
                studentQueryBuilder.and(nameProperty.like("陳%"), ageProperty.ge(16)))
                .list();

        studentAdapter.setStudents(students);
    }

    private void queryByWang() {
        QueryBuilder<Student> studentQueryBuilder = studentDao.queryBuilder();
        Property nameProperty = StudentDao.Properties.Name;

        List<Student> wangStudents = studentQueryBuilder.where(nameProperty.like("王%"))
                .list();

        studentAdapter.setStudents(wangStudents);
    }

    private void orderByAge() {
        QueryBuilder<Student> studentQueryBuilder = studentDao.queryBuilder();
        Property ageProperty = StudentDao.Properties.Age;

        List<Student> orderedStudents = studentQueryBuilder.orderAsc(ageProperty)
                .list();

        studentAdapter.setStudents(orderedStudents);
    }
}
