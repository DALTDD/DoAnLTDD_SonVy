package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class KhachHang {

    @SerializedName("MaKH")
    @Expose
    private String maKH;
    @SerializedName("HoTen")
    @Expose
    private String hoTen;
    @SerializedName("GioiTinh")
    @Expose
    private String gioiTinh;
    @SerializedName("NgaySinh")
    @Expose
    private String ngaySinh;
    @SerializedName("SDT")
    @Expose
    private String sdt;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("DiaChi")
    @Expose
    private String diaChi;
    @SerializedName("TaiKhoan")
    @Expose
    private String taiKhoan;

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

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

}