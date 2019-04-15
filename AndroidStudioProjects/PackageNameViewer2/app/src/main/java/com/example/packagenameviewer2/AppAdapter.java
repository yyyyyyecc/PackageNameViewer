package com.example.packagenameviewer2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private List<AppInformation> appInformationList;
    private OnItemClickListener itemClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appImage;
        TextView appName;
        TextView appPackageName;
        View appView;
        public ViewHolder(View view){
            super(view);
            appView=view;
            appImage=(ImageView)view.findViewById(R.id.app_image);
            appName=(TextView)view.findViewById(R.id.app_name);
            appPackageName=(TextView)view.findViewById(R.id.app_packagename);
        }
    }
    public AppAdapter(List<AppInformation> appInformations){
        this.appInformationList = appInformations;
    }

    //点击事件接口
    public interface OnItemClickListener{
        void onItemClick(AppInformation appInformation, int position);
    }
    //设置点击事件的方法
    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        AppInformation information=appInformationList.get(position);
        holder.appImage.setImageDrawable(information.getIcon());
        holder.appPackageName.setText(information.getPackageName());
        holder.appName.setText(information.getAppName());
        holder.appImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null)
                    itemClickListener.onItemClick(appInformationList.get(position), position);
            }
        });
        holder.appPackageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null)
                    itemClickListener.onItemClick(appInformationList.get(position), position);
            }
        });
        holder.appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null)
                    itemClickListener.onItemClick(appInformationList.get(position), position);
            }
        });
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_information,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }



    @Override
    public int getItemCount() {
        return  appInformationList.size();
    }
}
