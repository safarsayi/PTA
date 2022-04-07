package com.coverteam.pta.model;

public class DataPanduan {

    String judul, gambar, keterangan;

    public DataPanduan(){}

    public DataPanduan(String judul, String gambar, String keterangan) {
        this.judul = judul;
        this.gambar = gambar;
        this.keterangan = keterangan;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
