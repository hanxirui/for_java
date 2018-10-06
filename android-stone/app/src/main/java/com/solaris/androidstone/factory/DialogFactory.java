package com.solaris.androidstone.factory;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.solaris.androidstone.R;
import com.solaris.androidstone.model.Student;
import com.solaris.androidstone.model.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanxirui on 2016/12/23.
 */

public class DialogFactory {

    private DialogFactory(){

    }

    public static AlertDialog getStudentDialog(Context context, final Teacher teacher, final List<Student> students){
        int totalStudentSize = students.size();

        List<Student> checkedStudents = teacher.getStudents();
        List<String> items = new ArrayList<>(totalStudentSize);
        boolean[] checkedItems = new boolean[totalStudentSize];

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            String displayName = student.getName();
            if(student.getTeacher() != null){
                displayName += " - " + student.getTeacher().getName();
            }

            items.add(displayName);
            if(checkedStudents.contains(student)){
                checkedItems[i] = true;
            }else{
                checkedItems[i] = false;
            }
        }


        return new AlertDialog.Builder(context)
                .setMultiChoiceItems(items.toArray(new String[totalStudentSize]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean checked) {
                        if(checked){
                            students.get(i).setTeacher(teacher);
                            //TODO update to DB
                            students.get(i).update();
                        }
                    }
                }).setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

}
