package com.khoirul.aplikasikasir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.khoirul.aplikasikasir.adapter.DataAdapter;
import com.khoirul.aplikasikasir.model.Data;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerCart;
    EditText uang;
    TextView jumlah, kembalian;
    Button total, clear;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Data>  cart = new ArrayList<>();
    private DataAdapter dataAdapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerCart = findViewById(R.id.reclyerCart);
        uang = findViewById(R.id.uang);
        total = findViewById(R.id.total);
        jumlah = findViewById(R.id.jumlah);
        clear = findViewById(R.id.clear);
        uang = findViewById(R.id.uang);
        kembalian = findViewById(R.id.kembalian);
        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil Data");
        dataAdapter = new DataAdapter(getApplicationContext(), cart);
        dataAdapter.setDialog(new DataAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogitem = {"Hapus dari Cart"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                deleteData(cart.get(pos).getId());
                                break;

                        }
                    }
                });
                dialog.show();
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(decoration);
        recyclerCart.setAdapter(dataAdapter);


        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double sum = 0.0;
                for(int i=0; i<cart.size();i++) {
                    sum += cart.get(i).getHarga();
                }
                if(uang.getText().length() > 0) {
                    double uangmasuk = Double.parseDouble(uang.getText().toString());
                    double hasil = uangmasuk - sum;
                    jumlah.setText("Rp. "+ sum.toString());
                    kembalian.setText("Rp. "+String.format("%.2f", hasil));
                }else {
                    Toast.makeText(CartActivity.this, "Mohon masukan Uang", Toast.LENGTH_SHORT).show();
                }



            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cart.size()<1) {
                    Toast.makeText(CartActivity.this, "Cart masih kosong", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < cart.size(); i++) {
                        deleteData(cart.get(i).getId());
                    }
                    jumlah.setText("0");
                    kembalian.setText("0");
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        progressDialog.show();
        db.collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            cart.clear();
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Data data = new Data(document.getString("nama"), document.getDouble("harga"));
                                data.setId(document.getId());
                                cart.add(data);

                            }
                                dataAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(getApplicationContext(),"Data gagal ditampilkan", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
    private void deleteData(String id) {
        progressDialog.show();
        db.collection("cart").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Data gagal dihapus", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        getData();
                    }
                });
    }
}