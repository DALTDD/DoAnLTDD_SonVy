package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Order {

    @SerializedName("MaKH")
    @Expose
    private String maKH;
    @SerializedName("NgayDat")
    @Expose
    private String ngayDat;
    @SerializedName("ThanhTien")
    @Expose
    private double thanhTien;
    @SerializedName("MaPTTT")
    @Expose
    private String maPTTT;
    @SerializedName("MaPTVC")
    @Expose
    private String maPTVC;
    @SerializedName("HoTen")
    @Expose
    private String hoTen;
    @SerializedName("SDT")
    @Expose
    private String sdt;
    @SerializedName("DiaChi")
    @Expose
    private String diaChi;
    @SerializedName("DetailOrder")
    @Expose
    private ArrayList<DetailOrder> detailOrder;

    public Order() {
    }

    public Order(String maKH, String ngayDat, double thanhTien, String maPTTT, String maPTVC, String hoTen, String sdt, String diaChi, ArrayList<DetailOrder> detailOrder) {
        this.maKH = maKH;
        this.ngayDat = ngayDat;
        this.thanhTien = thanhTien;
        this.maPTTT = maPTTT;
        this.maPTVC = maPTVC;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.detailOrder = detailOrder;
    }

    public String getMaPTVC() {
        return maPTVC;
    }

    public void setMaPTVC(String maPTVC) {
        this.maPTVC = maPTVC;
    }
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getMaPTTT() {
        return maPTTT;
    }

    public void setMaPTTT(String maPTTT) {
        this.maPTTT = maPTTT;
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

    public ArrayList<DetailOrder> getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(ArrayList<DetailOrder> detailOrder) {
        this.detailOrder = detailOrder;
    }
}
