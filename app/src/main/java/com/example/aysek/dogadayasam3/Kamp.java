package com.example.aysek.dogadayasam3;

public class Kamp {
    private int id;
    private String baslik;
    private String metin;
    private String ksayisi;
    private byte[] image;

    public Kamp(String baslik, String metin,String ksayisi, byte[] image, int id) {
        this.baslik = baslik;
        this.metin = metin;
        this.ksayisi=ksayisi;
        this.image = image;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getMetin() {
        return metin;
    }

    public void setMetin(String metin) {
        this.metin = metin;
    }

    public String getKsayisi() {
        return ksayisi;
    }

    public void setKsayisi(String ksayisi) {
        this.ksayisi = ksayisi;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}
