package com.example.splashscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FAdapViewAttendance extends FirestoreRecyclerAdapter<ViewAttendanceAdapter,FAdapViewAttendance.NoteHolder> {

    public FAdapViewAttendance(@NonNull FirestoreRecyclerOptions<ViewAttendanceAdapter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull ViewAttendanceAdapter model) {
        holder.textViewdate.setText(model.getCheckin_date_());
        holder.textViewcheckin.setText(model.getCheckin_time_1());
        holder.textViewcheckout.setText(model.getCheckout_time_2());
        holder.textViewminutes.setText(model.getMinutes().toString());

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewindividualattendance, parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView textViewdate;
        TextView textViewcheckin;
        TextView textViewcheckout;
        TextView textViewminutes;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewdate = itemView.findViewById(R.id.carddate);
            textViewcheckin = itemView.findViewById(R.id.checkincard);
            textViewcheckout= itemView.findViewById(R.id.checkoutcard);
            textViewminutes = itemView.findViewById(R.id.minutescard);
        }
    }
}
