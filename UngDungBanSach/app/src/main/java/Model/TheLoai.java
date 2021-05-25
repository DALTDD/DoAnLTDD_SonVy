package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TheLoai {

    @SerializedName("MaLoai")
    @Expose
    private String maLoai;
    @SerializedName("MaDanhMuc")
    @Expose
    private String maDanhMuc;
    @SerializedName("TenTheLoai")
    @Expose
    private String tenTheLoai;
    @SerializedName("Anh")
    @Expose
    private String anh;

    public TheLoai(String maLoai, String maDanhMuc, String tenTheLoai) {
        this.maLoai = maLoai;
        this.maDanhMuc = maDanhMuc;
        this.tenTheLoai = tenTheLoai;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

}