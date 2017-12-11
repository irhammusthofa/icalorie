package id.co.kamil.icalorie;

/**
 * Created by Irham on 12/11/2017.
 */

public class Pengguna {
    private int id;
    private String nama,email,tgllahir,foto,berat,tinggi,kelamin,telepon;

    @Override
    public String toString() {
        return "Pengguna{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", tgllahir='" + tgllahir + '\'' +
                ", foto='" + foto + '\'' +
                ", berat='" + berat + '\'' +
                ", tinggi='" + tinggi + '\'' +
                ", kelamin='" + kelamin + '\'' +
                ", telepon='" + telepon + '\'' +
                '}';
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTgllahir() {
        return tgllahir;
    }

    public void setTgllahir(String tgllahir) {
        this.tgllahir = tgllahir;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getTinggi() {
        return tinggi;
    }

    public void setTinggi(String tinggi) {
        this.tinggi = tinggi;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
}
