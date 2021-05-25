package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class QuangCao {

    @SerializedName("MaQC")
    @Expose
    private String maQC;
    @SerializedName("TenQC")
    @Expose
    private String tenQC;
    @SerializedName("AnhQC")
    @Expose
    private String anhQC;

    public String getMaQC() {
        return maQC;
    }

    public void setMaQC(String maQC) {
        this.maQC = maQC;
    }

    public String getTenQC() {
        return tenQC;
    }

    public void setTenQC(String tenQC) {
        this.tenQC = tenQC;
    }

    public String getAnhQC() {
        return anhQC;
    }

    public void setAnhQC(String anhQC) {
        this.anhQC = anhQC;
    }

}