package Model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DonHang {

    @SerializedName("MaDonHang")
    @Expose
    private String maDonHang;
    @SerializedName("TinhTrang")
    @Expose
    private String tinhTrang;
    @SerializedName("NgayDat")
    @Expose
    private String ngayDat;
    @SerializedName("ThanhTien")
    @Expose
    private String thanhTien;

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(String thanhTien) {
        this.thanhTien = thanhTien;
    }

}