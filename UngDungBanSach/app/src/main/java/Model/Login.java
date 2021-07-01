package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Login {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("KhachHang")
    @Expose
    private KhachHang khachHang;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

}