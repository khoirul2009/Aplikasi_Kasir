package com.khoirul.aplikasikasir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    TextView namaB, hargaB;
    Button add;
    Double pharga;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        namaB = findViewById(R.id.namaB);
        hargaB = findViewById(R.id.hargaB);
        namaB.setText(getIntent().getStringExtra("nama"));
        hargaB.setText("Rp. "+getIntent().getStringExtra("harga"));
        pharga = Double.parseDouble(getIntent().getStringExtra("harga"));
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> cart = new HashMap<>();
                cart.put("nama", namaB.getText().toString());
                cart.put("harga", pharga);
                db.collection("cart")
                        .add(cart)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(@NonNull DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Barang ditambahkan ke Cart", Toast.LENGTH_SHORT).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }
}