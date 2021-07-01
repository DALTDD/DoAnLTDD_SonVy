package Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Citys {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<ResultCity> results = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultCity> getResults() {
        return results;
    }

    public void setResults(List<ResultCity> results) {
        this.results = results;
    }

}