package pergudangan;

public class Produk {
    private String nama;
    private int jumlahStok;
    private String gudang;

    public Produk(String nama, int jumlahStok) {
        this.nama = nama;
        this.jumlahStok = jumlahStok;
    }

    public String getNama() {
        return nama;
    }

    // Add the following methods
    public void tambahStok(int jumlah) {
        this.jumlahStok += jumlah;
    }

    public void kurangiStok(int jumlah) {
        if (jumlah <= this.jumlahStok) {
            this.jumlahStok -= jumlah;
        } else {
            System.out.println("Error: Stok tidak mencukupi.");
        }

    }

    public int getJumlahStok() {
        return jumlahStok;
    }

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
    }
}