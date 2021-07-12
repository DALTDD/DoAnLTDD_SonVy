package Model;

import java.util.ArrayList;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChiTietDonHang {

    @SerializedName("ThongTinDH")
    @Expose
    private ThongTinDH thongTinDH;
    @SerializedName("ChiTietDH")
    @Expose
    private ArrayList<ChiTietDH> chiTietDH = null;

    public ThongTinDH getThongTinDH() {
        return thongTinDH;
    }

    public void setThongTinDH(ThongTinDH thongTinDH) {
        this.thongTinDH = thongTinDH;
    }

    public ArrayList<ChiTietDH> getChiTietDH() {
        return chiTietDH;
    }

    public void setChiTietDH(ArrayList<ChiTietDH> chiTietDH) {
        this.chiTietDH = chiTietDH;
    }

}