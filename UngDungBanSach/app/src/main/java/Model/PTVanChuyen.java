package Model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PTVanChuyen {

    @SerializedName("MaPTVC")
    @Expose
    private String maPTVC;
    @SerializedName("TenPTVC")
    @Expose
    private String tenPTVC;
    @SerializedName("PhiVC")
    @Expose
    private String phiVC;

    public String getMaPTVC() {
        return maPTVC;
    }

    public void setMaPTVC(String maPTVC) {
        this.maPTVC = maPTVC;
    }

    public String getTenPTVC() {
        return tenPTVC;
    }

    public void setTenPTVC(String tenPTVC) {
        this.tenPTVC = tenPTVC;
    }

    public String getPhiVC() {
        return phiVC;
    }

    public void setPhiVC(String phiVC) {
        this.phiVC = phiVC;
    }

}