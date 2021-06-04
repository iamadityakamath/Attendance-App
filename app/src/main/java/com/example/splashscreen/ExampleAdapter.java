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

/*    public ExampleAdapter(@NonNull @NotNull FirestoreRecyclerOptions<ExampleItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ExampleAdapter.ExampleHolder holder, int position, @NonNull @NotNull ExampleItem model) {
        ExampleItem current = .get(position);

        holder.textViewName.setText(model.getEmployee_Name());
        Picasso.get().load(current.getImageUrl()).into(holder.mimage);
    }



    @NonNull
    @NotNull
    @Override
    public ExampleHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }


    class ExampleHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        ImageView imageViewPic;

        public ExampleHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.Employee_Name);
            imageViewPic = itemView.findViewById(R.id.Employee_Pic);
        }
    }*/


    private ArrayList<ExampleItem> mExampleList;
    private ArrayList<ExampleItem> exampleListFull;
    private OnItemClickListener mListener;
    private Context context;


    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
        this.exampleListFull = mExampleList;

    }

    /*@Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExampleItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ExampleItem item : exampleListFull) {
                    if (item.getFname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };*/


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
                    for(ExampleItem row: mExampleList){
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        TextView email,fname,phone,isUser,UserID;
        ImageView Employee_Pic;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.Employee_Name);
            Employee_Pic = itemView.findViewById(R.id.Employee_Pic);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            Intent i = new Intent (itemView.getContext(), EmployerSeeEmployeeDetails.class);
                            i.putExtra("fname", fname.getText().toString());
                            i.putExtra("email",email.getText().toString());
                            i.putExtra("phone",phone.getText().toString());
                            itemView.getContext().startActivity(i);
                        }
                    }
                }
            });*/
        }
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ExampleAdapter.ExampleViewHolder(v, mListener);
        /*ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;*/
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem exampleItem = exampleListFull.get(position);
        holder.mTextView1.setText(exampleItem.getFname());
        //Picasso.get().load(exampleItem.getEmployee_Pic()).placeholder(R.drawable.ic_employee_profile_home_img).into(holder.Employee_Pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), EmployerSeeEmployeeDetails.class);
                intent.putExtra("details",exampleListFull.get(position));
                /*intent.putExtra("email",exampleItem.getEmail());
                intent.putExtra("phone",exampleItem.getPhone());*/

                //intent.putExtra("userId", users.getUserId());

                v.getContext().startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return exampleListFull.size();
    }
}