package com.lf.ninghaisystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.entity.Project;

import java.util.List;

/**
 * Created by admin on 2017/11/7.
 */

public class ProjectListAdapter extends ArrayAdapter<Project> {

    List<Project> projectList;
    Context context;
    int resource;
    int flag;

    /**
     *
     * @param context
     * @param resource
     * @param objects
     */
    public ProjectListAdapter(Context context, int resource, List<Project> objects) {
        super(context, resource, objects);

        this.context = context;
        projectList = objects;
        this.resource = resource;
        this.flag = flag;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;
        Project project = projectList.get(position);

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resource,null);
        } else {
            view = convertView;
        }

        TextView tv_name = view.findViewById(R.id.project_name);
        TextView tv_team = view.findViewById(R.id.project_team);
        TextView project_name_title = view.findViewById(R.id.project_name_title);
        ImageView iv_head = view.findViewById(R.id.project_img);
        ImageView iv_tag = view.findViewById(R.id.img_tag);

        if (project.getHasSubProject()==0){
            project_name_title.setText("子项目名称:");
        }else {
            project_name_title.setText("项目名称:");
        }

        if(project.getIsOwn() == 1 && project.getHasSubProject() == 1) {
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setImageResource(R.mipmap.project_tag_together);
        } else if(project.getHasSubProject() == 1) {
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setImageResource(R.mipmap.project_tag);
        } else if(project.getIsOwn() == 1) {
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setImageResource(R.mipmap.project_tag_mine);
        } else {
            iv_tag.setVisibility(View.INVISIBLE);
        }

        tv_name.setText(project.getpName());
        tv_team.setText(project.getTeamName());
        Glide.with(context).load(project.getImgUrl()).into(iv_head);

        return view;
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    public void updateItem(List<Project> datas) {
        this.projectList = datas;
        notifyDataSetChanged();
    }

}
