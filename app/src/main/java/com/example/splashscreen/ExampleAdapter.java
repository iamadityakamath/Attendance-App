package com.example.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    public ArrayList<ExampleItem> mExampleList;
    public ArrayList<ExampleItem> exampleListFull;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        ImageView Employee_Pic;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.Employee_Name);
            Employee_Pic = itemView.findViewById(R.id.Employee_Pic);
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> mExampleList) {
        this.mExampleList = mExampleList;
        this.exampleListFull = mExampleList;

    }

    @NonNull
    @NotNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.example_item, parent, false);
        return new ExampleAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ExampleAdapter.ExampleViewHolder holder, int position) {
        ExampleItem exampleItem = exampleListFull.get(position);
        holder.mTextView1.setText(exampleItem.getFname());
        ExampleItem current = exampleListFull.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), EmployerSeeEmployeeDetails.class);
                intent.putExtra("details",exampleListFull.get(position));
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleListFull.size();
    }

    //search data
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if(key.isEmpty()){
                    exampleListFull = mExampleList;
                    Log.d("search_iss_e", ""+exampleListFull);
                }
                else {
                    exampleListFull = mExampleList;
                    Log.d("search_iss_f", ""+exampleListFull);
                    ArrayList<ExampleItem> isfiltered = new ArrayList<>();
                    for(ExampleItem row: exampleListFull){
                        if(row.getFname().toLowerCase().contains(key.toLowerCase())){
                            isfiltered.add(row);
                        }
                    }
                    exampleListFull = isfiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = exampleListFull;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                exampleListFull = (ArrayList<ExampleItem>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}