package Model;

public class Utils {
    public static final  String email = "noreplykrt@gmail.com";
    public static final String password = "VyMai1102";
    public static final String tittle = "[BookShop] Nhận lại mật khẩu";
    public static String createMessage(String name, String password){
        String message = "Xin chào: " + name + "!";
        message += "\n";
        message += "\nMật khẩu đăng nhập vào ứng dụng của bạn là : " + password;
        message += "\nHãy thay đổi mật khẩu để bảo vệ tài khoản của bạn";
        message += "\n";
        message += "\nTrân trọng cảm ơn!";
        message += "\nSon&Vy's BookShop";
        return  message;
    }
}