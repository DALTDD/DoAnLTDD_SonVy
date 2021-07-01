package Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Wards {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<ResultWard> results = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultWard> getResults() {
        return results;
    }

    public void setResults(List<ResultWard> results) {
        this.results = results;
    }

}