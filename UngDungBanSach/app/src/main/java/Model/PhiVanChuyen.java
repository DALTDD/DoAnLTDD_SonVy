package Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



public class PhiVanChuyen {

    @SerializedName("MienPhiVC")
    @Expose
    private String mienPhiVC;
    @SerializedName("PTVanChuyen")
    @Expose
    private ArrayList<PTVanChuyen> pTVanChuyen = null;

    public String getMienPhiVC() {
        return mienPhiVC;
    }

    public void setMienPhiVC(String mienPhiVC) {
        this.mienPhiVC = mienPhiVC;
    }

    public ArrayList<PTVanChuyen> getPTVanChuyen() {
        return pTVanChuyen;
    }

    public void setPTVanChuyen(ArrayList<PTVanChuyen> pTVanChuyen) {
        this.pTVanChuyen = pTVanChuyen;
    }

}