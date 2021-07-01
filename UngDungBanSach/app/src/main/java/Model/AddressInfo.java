package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressInfo {
    @SerializedName("HoTen")
    @Expose
    private String hoTen;
    @SerializedName("SDT")
    @Expose
    private String sdt;
    @SerializedName("DiaChi")
    @Expose
    private String diaChi;

    public AddressInfo() {
    }

    public AddressInfo(String hoTen, String sdt, String diaChi) {
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
