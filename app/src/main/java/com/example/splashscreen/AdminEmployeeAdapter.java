package com.example.splashscreen;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminEmployeeAdapter extends RecyclerView.Adapter<AdminEmployeeAdapter.AdminEmployeeViewHolder> {

    private Context mCtx;
    private ArrayList<AdminEmployee> adminEmployeeList;
    public ArrayList<AdminEmployee> aerfilteredList;

    public static class AdminEmployeeViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewtitle;

        public AdminEmployeeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewtitle = itemView.findViewById(R.id.Admin_See_Employer_Employee_Name);
            //imageView = imageView.findViewById(R.id.AdminSeeEmployeePic);
        }
    }


    public AdminEmployeeAdapter(Context mCtx, ArrayList<AdminEmployee> adminEmployeeList) {
        //this.mCtx = mCtx;
        this.adminEmployeeList = adminEmployeeList;
        this.aerfilteredList = adminEmployeeList;
    }

    @NonNull
    @NotNull
    @Override
    public AdminEmployeeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.admin_see_employee_layout,parent, false);
        return new AdminEmployeeAdapter.AdminEmployeeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminEmployeeViewHolder holder, int position) {
        AdminEmployee adminEmployee = aerfilteredList.get(position);
        holder.textViewtitle.setText(adminEmployee.getFname());
        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(adminEmployee.getImage(),null));
    }

    @Override
    public int getItemCount() {
        return aerfilteredList.size();
    }

    //search data
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if (key.isEmpty()) {
                    aerfilteredList = adminEmployeeList;
                } else {
                    ArrayList<AdminEmployee> isfiltered = new ArrayList<>();
                    for (AdminEmployee row : adminEmployeeList) {
                        if (row.getFname().toLowerCase().contains(key.toLowerCase())) {
                            isfiltered.add(row);
                        }
                    }
                    aerfilteredList = isfiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = aerfilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                aerfilteredList = (ArrayList<AdminEmployee>) filterResults.values;
                notifyDataSetChanged();

            }
        };


    }


}