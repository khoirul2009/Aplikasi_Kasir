package com.khoirul.aplikasikasir.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.khoirul.aplikasikasir.DetailActivity;
import com.khoirul.aplikasikasir.R;
import com.khoirul.aplikasikasir.model.Data;

import org.w3c.dom.Text;

import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private Context context;
    private List<Data> list;
    private Dialog dialog;

    public interface Dialog {
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public DataAdapter(Context context, List<Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama.setText(list.get(position).getNama());
        holder.harga.setText("Rp. " + String.format("%.2f", list.get(position).getHarga()));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, harga;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            harga = itemView.findViewById(R.id.harga);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog != null) {
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });


        }
    }
}