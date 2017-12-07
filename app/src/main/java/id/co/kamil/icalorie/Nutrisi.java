package id.co.kamil.icalorie;

/**
 * Created by Irham on 12/7/2017.
 */

public class Nutrisi {
    private int id;
    private String nama;
    private double berat,kalori,karbohidrat,protein,vita,vitb,vitc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public double getKalori() {
        return kalori;
    }

    public void setKalori(double kalori) {
        this.kalori = kalori;
    }

    public double getKarbohidrat() {
        return karbohidrat;
    }

    public void setKarbohidrat(double karbohidrat) {
        this.karbohidrat = karbohidrat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getVita() {
        return vita;
    }

    public void setVita(double vita) {
        this.vita = vita;
    }

    public double getVitb() {
        return vitb;
    }

    public void setVitb(double vitb) {
        this.vitb = vitb;
    }

    public double getVitc() {
        return vitc;
    }

    public void setVitc(double vitc) {
        this.vitc = vitc;
    }

    @Override
    public String toString() {
        return "Nutrisi{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", berat=" + berat +
                ", kalori=" + kalori +
                ", karbohidrat=" + karbohidrat +
                ", protein=" + protein +
                ", vita=" + vita +
                ", vitb=" + vitb +
                ", vitc=" + vitc +
                '}';
    }
}
