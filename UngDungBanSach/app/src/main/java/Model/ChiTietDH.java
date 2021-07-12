package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChiTietDH {

    @SerializedName("MaSach")
    @Expose
    private String maSach;
    @SerializedName("Anh")
    @Expose
    private String anh;
    @SerializedName("TenSach")
    @Expose
    private String tenSach;
    @SerializedName("DonGia")
    @Expose
    private String donGia;
    @SerializedName("SoLuong")
    @Expose
    private String soLuong;

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

}