package com.khoirul.aplikasikasir.model;

public class Data {
    private String id, nama;
    private Double harga;


    public Data(String nama, Double harga){
        this.nama = nama;
        this.harga = harga;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }
}

