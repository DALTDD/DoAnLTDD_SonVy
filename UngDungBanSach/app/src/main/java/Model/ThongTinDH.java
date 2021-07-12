package Model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ThongTinDH {

    @SerializedName("MaDonHang")
    @Expose
    private String maDonHang;
    @SerializedName("ThanhTien")
    @Expose
    private String thanhTien;
    @SerializedName("TinhTrang")
    @Expose
    private String tinhTrang;
    @SerializedName("NgayDat")
    @Expose
    private String ngayDat;
    @SerializedName("TenPTTT")
    @Expose
    private String tenPTTT;
    @SerializedName("TenPTVC")
    @Expose
    private String tenPTVC;
    @SerializedName("TongTien")
    @Expose
    private String tongTien;
    @SerializedName("HoTen")
    @Expose
    private String hoTen;
    @SerializedName("SDT")
    @Expose
    private String sdt;
    @SerializedName("DiaChi")
    @Expose
    private String diaChi;

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(String thanhTien) {
        this.thanhTien = thanhTien;
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

    public String getTenPTTT() {
        return tenPTTT;
    }

    public void setTenPTTT(String tenPTTT) {
        this.tenPTTT = tenPTTT;
    }

    public String getTenPTVC() {
        return tenPTVC;
    }

    public void setTenPTVC(String tenPTVC) {
        this.tenPTVC = tenPTVC;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

}