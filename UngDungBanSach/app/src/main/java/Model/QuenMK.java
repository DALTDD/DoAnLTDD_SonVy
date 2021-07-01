package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class QuenMK {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("HoTen")
    @Expose
    private String hoTen;
    @SerializedName("MatKhau")
    @Expose
    private String matKhau;
    @SerializedName("Email")
    @Expose
    private String email;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}