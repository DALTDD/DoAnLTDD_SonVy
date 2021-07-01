package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SachLite {

    @SerializedName("MaSach")
    @Expose
    private String maSach;
    @SerializedName("TenSach")
    @Expose
    private String tenSach;
    @SerializedName("Anh")
    @Expose
    private String anh;
    @SerializedName("GiaGoc")
    @Expose
    private String giaGoc;
    @SerializedName("GiaKhuyenMai")
    @Expose
    private String giaKhuyenMai;

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(String giaGoc) {
        this.giaGoc = giaGoc;
    }

    public String getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(String giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }

}