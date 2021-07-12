package Model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TTGiaoHang {

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

}