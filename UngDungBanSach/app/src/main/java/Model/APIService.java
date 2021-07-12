package Model;

public class APIService {
    private static String base_url ="http://sonvy.000webhostapp.com/Server/";
    private static  String url_address = "http://api.mysupership.vn/v1/partner/areas/";
    public  static DataService getService(){
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }
    public  static DataService getServiceAddress(){
        return APIRetrofitClientAddress.getClient(url_address).create(DataService.class);
    }
}
