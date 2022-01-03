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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.khoirul.aplikasikasir.adapter.DataAdapter;
import com.khoirul.aplikasikasir.model.Data;

import java.security.KeyRep;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    FloatingActionButton keranjang;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Data> list = new ArrayList<>();
    private DataAdapter dataAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.reclyerView);
        fab = findViewById(R.id.fab);
        keranjang = findViewById(R.id.keranjang);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil Data");
        dataAdapter = new DataAdapter(getApplicationContext(), list);
        dataAdapter.setDialog(new DataAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogitem = {"Edit", "Hapus", "Detail"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("nama", list.get(pos).getNama());
                                intent.putExtra("harga", list.get(pos).getHarga().toString());
                                startActivity(intent);
                                break;
                            case 1:
                                deleteData(list.get(pos).getId());
                                break;
                            case 2:
                                Intent det = new Intent(getApplicationContext(), DetailActivity.class);
                                det.putExtra("nama", list.get(pos).getNama());
                                det.putExtra("harga", list.get(pos).getHarga().toString());
                                startActivity(det);
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(dataAdapter);


        keranjang.setOnClickListener(view -> {
            Intent pindah = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(pindah);
        });
        fab.setOnClickListener(view -> {
            Intent pindah = new Intent(getApplicationContext(), CreateActivity.class);
            startActivity(pindah);
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        progressDialog.show();
        db.collection("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            list.clear();
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Data data = new Data(document.getString("nama"), document.getDouble("harga"));
                                data.setId(document.getId());
                                list.add(data);
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
        db.collection("data").document(id)
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