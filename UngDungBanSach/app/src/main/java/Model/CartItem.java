package Model;

public class CartItem {
    private String maSach;
    private String tenSach;
    private String anh;
    private double giaGoc;
    private double giaKhuyenMai;
    private int soLuong;
    private double thanhTien;

    public CartItem() {
    }

    public CartItem(String maSach, int soLuong) {
        this.maSach = maSach;
        this.soLuong = soLuong;
    }

    public CartItem(String maSach, String tenSach, String anh, double giaGoc, double giaKhuyenMai) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.anh = anh;
        this.giaGoc = giaGoc;
        this.giaKhuyenMai = giaKhuyenMai;
    }

    public CartItem(String maSach, String tenSach, String anh, double giaGoc, double giaKhuyenMai, int soLuong) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.anh = anh;
        this.giaGoc = giaGoc;
        this.giaKhuyenMai = giaKhuyenMai;
        this.soLuong = soLuong;
    }

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

    public double getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(double giaGoc) {
        this.giaGoc = giaGoc;
    }

    public double getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(double giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getThanhTien() {
        return this.soLuong * this.giaKhuyenMai;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "maSach=" + maSach +
                ", tenSach='" + tenSach + '\'' +
                ", anh='" + anh + '\'' +
                ", giaGoc=" + giaGoc +
                ", giaKhuyenMai=" + giaKhuyenMai +
                ", soLuong=" + soLuong +
                ", thanhTien=" + getThanhTien() +
                '}';
    }
}
