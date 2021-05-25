package Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {
    @GET("getAllQuangCao.php")
    Call<List<QuangCao>> getAllQuangCao();

    @GET("getAllSach.php")
    Call<List<Sach>> getAllSach();

    @FormUrlEncoded
    @POST("getSachByMaSach.php")
    Call<Sach> getSachByMaSach(@Field("maSach") String maSach);

    @FormUrlEncoded
    @POST("getSachByTheLoaiRandom.php")
    Call<List<Sach>> getSachByTheLoaiRandom(@Field("maLoai") String maLoai, @Field("maSach") String maSach);
    //

    @FormUrlEncoded
    @POST("register_2.php")
    Call<StringRequest> register(@Field("HoTen") String HoTen, @Field("Email") String Email, @Field("TaiKhoan") String TaiKhoan, @Field("MatKhau") String MatKhau);
//    kết quả trả về
//    $result_code =-1; Trùng tên đăng nhập
//    $result_code =-2; Trùng Email
//    If(status = Ok) Thành công
    @FormUrlEncoded
    @POST("login.php")
    Call<StringRequest> login(@Field("TaiKhoan") String TaiKhoan, @Field("MatKhau") String MatKhau);

    @GET("getAllDanhMuc.php")
    Call<List<DanhMuc>> getAllDanhMuc();

    @FormUrlEncoded
    @POST("getTheLoaiByDanhMuc.php")
    Call<List<TheLoai>> getTheLoaiByDanhMuc(@Field("MaDanhMuc") String maDanhMuc);

    @FormUrlEncoded
    @POST("getSachByTheLoai.php")
    Call<List<Sach>> getSachByTheLoai(@Field("MaLoai") String maLoai);
}
