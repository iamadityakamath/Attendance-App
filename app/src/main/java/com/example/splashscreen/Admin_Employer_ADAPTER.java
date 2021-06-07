package com.example.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Admin_Employer_ADAPTER extends RecyclerView.Adapter<Admin_Employer_ADAPTER.AdminEmployerViewHolder> implements Filterable {

    private Context mCtx;
    public ArrayList<Admin_Employer> adminemployerList;
    //private Admin_Employer_ADAPTER.OnItemClickListener mListener;

    public static class AdminEmployerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName, textViewEmail, textViewPassword, textViewPhone, textViewuserID;

        public AdminEmployerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.Admin_List_Employer_Pic);
            textViewName = itemView.findViewById(R.id.Admin_List_Employer_Name);


        }

    }

    public Admin_Employer_ADAPTER(ArrayList<Admin_Employer> adminemployerList) {
        //this.mCtx = mCtx;
        this.adminemployerList = adminemployerList;
    }

    /*public void  setOnItemClickListener(ExampleAdapter.OnItemClickListener listener){
        mListener = (Admin_Employer_ADAPTER.OnItemClickListener) listener;
    }*/

    /*public interface OnItemClickListener {
        void onDeleteClick(int position);
    }*/

    @NonNull
    @NotNull
    @Override
    public AdminEmployerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.admin_list_layout,parent,false);
        return new Admin_Employer_ADAPTER.AdminEmployerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull @NotNull Admin_Employer_ADAPTER.AdminEmployerViewHolder holder, int position) {
        Admin_Employer AE = adminemployerList.get(position);
        holder.textViewName.setText(AE.getFname());
        Admin_Employer current = adminemployerList.get(position);
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateViewDelete_Employer.class);
                intent.putExtra("Admin_Employer_Details", adminemployerList.get(position));
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return adminemployerList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


        /*
        @Override
        public void onClick(View v) {
            Admin_Employer adminEmployer = adminemployerList.get(getAdapterPosition());
            Intent intent = new Intent(mCtx,UpdateViewDelete_Employer.class);
            intent.putExtra("Admin_Employer_Details",adminemployerList.get(getAdapterPosition()));
            mCtx.startActivity(intent);
        }*/

}
