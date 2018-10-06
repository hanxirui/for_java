package com.solaris.androidstone.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.solaris.androidstone.R;
import com.solaris.androidstone.model.Student;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by hanxirui on 2016/12/23.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        notifyItemInserted(students.size() - 1);
    }

    public void setStudents(List<Student> students){
        this.students.clear();
        this.students.addAll(students);
        notifyDataSetChanged();
    }
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student student = students.get(position);

        holder.textName.setText(student.getName());
        holder.textId.setText("" + student.getId());
        holder.textAge.setText(student.getAge() + "歲");

        if(student.getTeacher() != null) {
            holder.textTeacher.setText(student.getTeacher().getName());
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textName;
        private TextView textId;
        private TextView textAge;
        private TextView textTeacher;

        public ViewHolder(View itemView){
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textId = (TextView) itemView.findViewById(R.id.textId);
            textAge = (TextView) itemView.findViewById(R.id.textAge);
            textTeacher = (TextView) itemView.findViewById(R.id.textTeacher);
        }
    }
}
