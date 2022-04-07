package com.coverteam.pta.model;

public class DataCuti {
    String CutiID, CutiNama,CutiNIP, CutiJabatan, CutiAlasan, CutiAlamat, CutiNoHP, CutiTglMulai, CutiTglSelesai, CutiStatus, CutiTolakAlasan,CutiSurat,CutiUsername;
    Integer CutiJmlh;

    public DataCuti(){}

    public DataCuti(String cutiID, String cutiNama, String cutiNIP, String cutiJabatan, String cutiAlasan, String cutiAlamat, String cutiNoHP, String cutiTglMulai, String cutiTglSelesai, String cutiStatus, String cutiTolakAlasan, String cutiSurat, String cutiUsername, Integer cutiJmlh) {
        CutiID = cutiID;
        CutiNama = cutiNama;
        CutiNIP = cutiNIP;
        CutiJabatan = cutiJabatan;
        CutiAlasan = cutiAlasan;
        CutiAlamat = cutiAlamat;
        CutiNoHP = cutiNoHP;
        CutiTglMulai = cutiTglMulai;
        CutiTglSelesai = cutiTglSelesai;
        CutiStatus = cutiStatus;
        CutiTolakAlasan = cutiTolakAlasan;
        CutiSurat = cutiSurat;
        CutiUsername = cutiUsername;
        CutiJmlh = cutiJmlh;
    }

    public String getCutiID() {
        return CutiID;
    }

    public void setCutiID(String cutiID) {
        CutiID = cutiID;
    }

    public String getCutiNama() {
        return CutiNama;
    }

    public void setCutiNama(String cutiNama) {
        CutiNama = cutiNama;
    }

    public String getCutiNIP() {
        return CutiNIP;
    }

    public void setCutiNIP(String cutiNIP) {
        CutiNIP = cutiNIP;
    }

    public String getCutiJabatan() {
        return CutiJabatan;
    }

    public void setCutiJabatan(String cutiJabatan) {
        CutiJabatan = cutiJabatan;
    }

    public String getCutiAlasan() {
        return CutiAlasan;
    }

    public void setCutiAlasan(String cutiAlasan) {
        CutiAlasan = cutiAlasan;
    }

    public String getCutiAlamat() {
        return CutiAlamat;
    }

    public void setCutiAlamat(String cutiAlamat) {
        CutiAlamat = cutiAlamat;
    }

    public String getCutiNoHP() {
        return CutiNoHP;
    }

    public void setCutiNoHP(String cutiNoHP) {
        CutiNoHP = cutiNoHP;
    }

    public String getCutiTglMulai() {
        return CutiTglMulai;
    }

    public void setCutiTglMulai(String cutiTglMulai) {
        CutiTglMulai = cutiTglMulai;
    }

    public String getCutiTglSelesai() {
        return CutiTglSelesai;
    }

    public void setCutiTglSelesai(String cutiTglSelesai) {
        CutiTglSelesai = cutiTglSelesai;
    }

    public String getCutiStatus() {
        return CutiStatus;
    }

    public void setCutiStatus(String cutiStatus) {
        CutiStatus = cutiStatus;
    }

    public String getCutiTolakAlasan() {
        return CutiTolakAlasan;
    }

    public void setCutiTolakAlasan(String cutiTolakAlasan) {
        CutiTolakAlasan = cutiTolakAlasan;
    }

    public String getCutiSurat() {
        return CutiSurat;
    }

    public void setCutiSurat(String cutiSurat) {
        CutiSurat = cutiSurat;
    }

    public String getCutiUsername() {
        return CutiUsername;
    }

    public void setCutiUsername(String cutiUsername) {
        CutiUsername = cutiUsername;
    }

    public Integer getCutiJmlh() {
        return CutiJmlh;
    }

    public void setCutiJmlh(Integer cutiJmlh) {
        CutiJmlh = cutiJmlh;
    }
}
