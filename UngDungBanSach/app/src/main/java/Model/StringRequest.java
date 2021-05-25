package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StringRequest {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result_code")
    @Expose
    private String resultCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

}