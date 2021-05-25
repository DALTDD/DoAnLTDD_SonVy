package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DanhMuc {

    @SerializedName("MaDanhMuc")
    @Expose
    private String maDanhMuc;
    @SerializedName("TenDanhMuc")
    @Expose
    private String tenDanhMuc;
    @SerializedName("Anh")
    @Expose
    private String anh;

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

}