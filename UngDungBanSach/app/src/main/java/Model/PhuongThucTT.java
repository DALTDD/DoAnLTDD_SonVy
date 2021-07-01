package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PhuongThucTT {

    @SerializedName("MaPTTT")
    @Expose
    private String maPTTT;
    @SerializedName("TenPTTT")
    @Expose
    private String tenPTTT;
    @SerializedName("Anh")
    @Expose
    private String anh;

    public String getMaPTTT() {
        return maPTTT;
    }

    public void setMaPTTT(String maPTTT) {
        this.maPTTT = maPTTT;
    }

    public String getTenPTTT() {
        return tenPTTT;
    }

    public void setTenPTTT(String tenPTTT) {
        this.tenPTTT = tenPTTT;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

}