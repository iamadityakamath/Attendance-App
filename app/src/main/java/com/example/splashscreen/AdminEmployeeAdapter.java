package com.example.splashscreen;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminEmployeeAdapter extends RecyclerView.Adapter<AdminEmployeeAdapter.AdminEmployeeViewHolder> {

    private Context mCtx;
    private List<AdminEmployee> adminEmployeeList;


    public AdminEmployeeAdapter(Context mCtx, List<AdminEmployee> adminEmployeeList) {
        this.mCtx = mCtx;
        this.adminEmployeeList = adminEmployeeList;
    }

    @NonNull
    @NotNull
    @Override
    public AdminEmployeeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_see_employee_layout, null);
        AdminEmployeeViewHolder holder = new AdminEmployeeViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminEmployeeViewHolder holder, int position) {
        AdminEmployee adminEmployee = adminEmployeeList.get(position);

        holder.textViewtitle.setText(adminEmployee.getFname());
        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(adminEmployee.getImage(),null));
    }

    @Override
    public int getItemCount() {
        return adminEmployeeList.size();
    }


    class AdminEmployeeViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewtitle;

        public AdminEmployeeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewtitle = itemView.findViewById(R.id.AdminSeeEmployer_EmployeeName);
            //imageView = imageView.findViewById(R.id.AdminSeeEmployer_EmployeePic);
        }
    }

}