package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailOrder {
    @SerializedName("MaSach")
    @Expose
    private String maSach;
    @SerializedName("SoLuong")
    @Expose
    private int soLuong;
    @SerializedName("DonGia")
    @Expose
    private  double donGia;

    public DetailOrder() {
    }

    public DetailOrder(String maSach, int soLuong, double donGia) {
        this.maSach = maSach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}
