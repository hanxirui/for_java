package com.solaris.androidstone.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.solaris.androidstone.R;
import com.solaris.androidstone.base.AppBaseActivity;
import com.solaris.androidstone.model.Teacher;

import java.util.List;

/**
 * adapter模板类,使用Holder保存view,避免重复创建
 * Created by hanxirui on 2017/6/10.
 * <p>
 * <p>
 * <p>
 * <p>
 * <pre>
 * @Override
 * protected void initViews(Bundle savedInstanceState) {
 * setContentView(R.layout.activity_listdemo);
 *
 * lvCinemaList = (ListView) findViewById(R.id.lvCinemalist);
 *
 * CinemaAdapter adapter = new CinemaAdapter(
 * cinemaList, ListDemoActivity.this);
 * lvCinemaList.setAdapter(adapter);
 * lvCinemaList.setOnItemClickListener(
 * new AdapterView.OnItemClickListener() {
 * @Override
 * public void onItemClick(AdapterView<?> parent, View view,
 * int position, long id) {
 * //do something
 * }
 * });
 * }
 * <pre/>
 */

public class TemplateAdapter extends BaseAdapter {

    private List<Teacher> teacherList;

    private AppBaseActivity context;

    public TemplateAdapter(List<Teacher> teacherList, AppBaseActivity context) {
        this.teacherList = teacherList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return teacherList.size();
    }

    @Override
    public Object getItem(int position) {
        return teacherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = context.getLayoutInflater().inflate(
                    R.layout.activity_teacher, null);
            holder.textName = (TextView) convertView
                    .findViewById(R.id.textName);
            holder.textId = (TextView) convertView
                    .findViewById(R.id.textId);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Teacher teacher = teacherList.get(position);
        holder.textName.setText(teacher.getName());
        holder.textId.setText(teacher.getId() + "");
        return convertView;

    }

    class Holder {
        private TextView textName;
        private TextView textId;
    }

}
