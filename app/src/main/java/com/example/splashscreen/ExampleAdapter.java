package com.example.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//public class ExampleAdapter extends FirestoreRecyclerAdapter<ExampleItem, ExampleAdapter.ExampleHolder> {

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private ArrayList<ExampleItem> mExampleList;
    private ArrayList<ExampleItem> exampleListFull;
    private Context context;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        TextView email,fname,phone,isUser,UserID;
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

    //search data
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if(key.isEmpty()){
                    exampleListFull = mExampleList;
                }
                else {
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

    /*public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }*/



    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ExampleAdapter.ExampleViewHolder(v);
        /*ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;*/
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem exampleItem = exampleListFull.get(position);
        holder.mTextView1.setText(exampleItem.getFname());
        //Picasso.get().load(exampleItem.getEmployee_Pic()).placeholder(R.drawable.ic_employee_profile_home_img).into(holder.Employee_Pic);
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
}