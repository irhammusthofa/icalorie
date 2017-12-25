package id.co.kamil.icalorie;

/**
 * Created by Irham on 12/17/2017.
 */

public class Exercise {
    private int id,langkah,icon;
    private String aktivitas;
    private float jarak,kalori;
    private long w_awal,w_akhir,w_length;


    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", langkah=" + langkah +
                ", aktivitas='" + aktivitas + '\'' +
                ", jarak=" + jarak +
                ", kalori=" + kalori +
                ", w_awal=" + w_awal +
                ", w_akhir=" + w_akhir +
                ", w_length=" + w_length +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLangkah() {
        return langkah;
    }

    public void setLangkah(int langkah) {
        this.langkah = langkah;
    }

    public String getAktivitas() {
        return aktivitas;
    }

    public void setAktivitas(String aktivitas) {
        this.aktivitas = aktivitas;
    }

    public float getJarak() {
        return jarak;
    }

    public void setJarak(float jarak) {
        this.jarak = jarak;
    }

    public float getKalori() {
        return kalori;
    }

    public void setKalori(float kalori) {
        this.kalori = kalori;
    }

    public long getW_awal() {
        return w_awal;
    }

    public void setW_awal(long w_awal) {
        this.w_awal = w_awal;
    }

    public long getW_akhir() {
        return w_akhir;
    }

    public void setW_akhir(long w_akhir) {
        this.w_akhir = w_akhir;
    }

    public long getW_length() {
        return w_length;
    }

    public void setW_length(long w_length) {
        this.w_length = w_length;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
