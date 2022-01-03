package com.khoirul.aplikasikasir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {
    private EditText Enama, Eharga;
    private Button simpan;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private String id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        progressDialog = new ProgressDialog(CreateActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan...");

        Enama = findViewById(R.id.Enama);
        Eharga = findViewById(R.id.Eharga);
        simpan = findViewById(R.id.simpan);

        simpan.setOnClickListener(view -> {
            if(Enama.getText().length()>0 && Eharga.getText().length()>0){
                saveData(Enama.getText().toString(), Double.parseDouble(Eharga.getText().toString()));
                Intent kembali = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(kembali);
            }
            else {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = getIntent();
        if(intent!=null){
            id=intent.getStringExtra("id");
            Enama.setText(intent.getStringExtra("nama"));
            Eharga.setText(intent.getStringExtra("harga"));

        }
    }
    private void saveData(String nama, Double harga) {
        Map<String, Object> data = new HashMap<>();
        data.put("nama", nama);
        data.put("harga", harga);
        progressDialog.show();
        if (id!=null) {
            db.collection("data").document(id)
                    .set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                                finish();

                            }else {
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            db.collection("data")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(@NonNull DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });

        }
    }
}