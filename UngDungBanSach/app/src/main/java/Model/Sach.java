package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Sach {

    @SerializedName("MaSach")
    @Expose
    private String maSach;
    @SerializedName("TenSach")
    @Expose
    private String tenSach;
    @SerializedName("MaLoai")
    @Expose
    private String maLoai;
    @SerializedName("TacGia")
    @Expose
    private String tacGia;
    @SerializedName("MoTa")
    @Expose
    private String moTa;
    @SerializedName("Anh")
    @Expose
    private String anh;
    @SerializedName("GiaGoc")
    @Expose
    private String giaGoc;
    @SerializedName("GiaKhuyenMai")
    @Expose
    private String giaKhuyenMai;
    @SerializedName("TenTheLoai")
    @Expose
    private String tenTheLoai;
    @SerializedName("TenDanhMuc")
    @Expose
    private String tenDanhMuc;
    @SerializedName("TenNXB")
    @Expose
    private String tenNXB;

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

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
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

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getTenNXB() {
        return tenNXB;
    }

    public void setTenNXB(String tenNXB) {
        this.tenNXB = tenNXB;
    }

}