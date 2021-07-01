package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DiaChi {

    @SerializedName("MaDiaChi")
    @Expose
    private String maDiaChi;
    @SerializedName("MaKH")
    @Expose
    private String maKH;
    @SerializedName("HoTen")
    @Expose
    private String hoTen;
    @SerializedName("SDT")
    @Expose
    private String sdt;
    @SerializedName("ThanhPho")
    @Expose
    private String thanhPho;
    @SerializedName("Quan")
    @Expose
    private String quan;
    @SerializedName("Phuong")
    @Expose
    private String phuong;
    @SerializedName("DiaChiNha")
    @Expose
    private String diaChiNha;
    @SerializedName("DiaChiMacDinh")
    @Expose
    private String diaChiMacDinh;

    public String getMaDiaChi() {
        return maDiaChi;
    }

    public void setMaDiaChi(String maDiaChi) {
        this.maDiaChi = maDiaChi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getThanhPho() {
        return thanhPho;
    }

    public void setThanhPho(String thanhPho) {
        this.thanhPho = thanhPho;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getPhuong() {
        return phuong;
    }

    public void setPhuong(String phuong) {
        this.phuong = phuong;
    }

    public String getDiaChiNha() {
        return diaChiNha;
    }

    public void setDiaChiNha(String diaChiNha) {
        this.diaChiNha = diaChiNha;
    }

    public String getDiaChiMacDinh() {
        return diaChiMacDinh;
    }

    public void setDiaChiMacDinh(String diaChiMacDinh) {
        this.diaChiMacDinh = diaChiMacDinh;
    }

}