package Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataService {
    @GET("getAllQuangCao.php")
    Call<List<QuangCao>> getAllQuangCao();

    @GET("getAllSach.php")
    Call<List<SachLite>> getAllSach();

    @FormUrlEncoded
    @POST("getSachByMaSach.php")
    Call<Sach> getSachByMaSach(@Field("maSach") String maSach);

    @FormUrlEncoded
    @POST("getSachByTheLoaiRandom.php")
    Call<List<SachLite>> getSachByTheLoaiRandom(@Field("maLoai") String maLoai, @Field("maSach") String maSach);
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
    Call<List<SachLite>> getSachByTheLoai(@Field("maLoai") String maLoai);
    @FormUrlEncoded
    @POST("searchSach.php")
    Call<List<Sach>> getSachBySearch(@Field("search") String search);

    //

    @GET("province")
    Call<Citys> getDataCitys();

    @GET("district?")
    Call<Dictricts> getDataDictricts(@Query("province") String province);

    @GET("commune?")
    Call<Wards> getDataWards(@Query("district") String district);

    //

    @GET("getAllPTTT.php")
    Call<List<PhuongThucTT>> getDataPTTT();

    //
    @FormUrlEncoded
    @POST("loginAccount.php")
    Call<Login> loginAccount(@Field("TaiKhoan") String TaiKhoan, @Field("MatKhau") String MatKhau);

    //
    @POST("order.php")
    Call<StringRequest> datHang(@Body Order order);
    //
    @FormUrlEncoded
    @POST("forgotPassword.php")
    Call<QuenMK> forgotPassword(@Field("TaiKhoan") String TaiKhoan);


    //
    @FormUrlEncoded
    @POST("getSachPaging.php")
    Call<List<Sach>> getSachPaging(@Field("page") int page, @Field("limit") int limit);
    //
    @FormUrlEncoded
    @POST("getSachKMPaging.php")
    Call<List<SachLite>> getSachKMPaging(@Field("page") int page, @Field("limit") int limit);
    //
    @FormUrlEncoded
    @POST("getSachMoiPaging.php")
    Call<List<SachLite>> getSachMoiPaging(@Field("page") int page, @Field("limit") int limit);
    //
    @FormUrlEncoded
    @POST("getSachBanChayPaging.php")
    Call<List<SachLite>> getSachBanChayPaging(@Field("page") int page, @Field("limit") int limit);
    //
    @FormUrlEncoded
    @POST("getSachAllPaging.php")
    Call<List<SachLite>> getSachAllPaging(@Field("page") int page, @Field("limit") int limit);
    //
    @GET("getSachMoi.php")
    Call<List<SachLite>> getSachMoi();
    //
    @GET("getSachKM.php")
    Call<List<SachLite>> getSachKM();
    //
    @GET("getSachBanChay.php")
    Call<List<SachLite>> getSachBanChay();
    //
    @GET("getSachAll.php")
    Call<List<SachLite>> getSachAll();
    //
    @FormUrlEncoded
    @POST("checkDiaChiGH.php")
    Call<StringRequest> checkDiaChiGH(@Field("MaKH") String MaKH);
    //
    @FormUrlEncoded
    @POST("insertDeliveryAddress.php")
    Call<StringRequest> insertDeliveryAddress(@Field("MaKH") String MaKH, @Field("HoTen") String HoTen,@Field("SDT") String SDT,@Field("ThanhPho") String ThanhPho,@Field("Quan") String Quan,@Field("Phuong") String Phuong, @Field("DiaChiNha") String DiaChiNha);
    //
    @FormUrlEncoded
    @POST("getAddressByMaKH.php")
    Call<List<DiaChi>> getAddressByMaKH(@Field("MaKH") String MaKH);
    //
    @FormUrlEncoded
    @POST("getDiaChiByMaDiaChi.php")
    Call<DiaChi> getDiaChiByMaDiaChi(@Field("MaDiaChi") String MaDiaChi);
    //
    @FormUrlEncoded
    @POST("updateDiaChi.php")
    Call<StringRequest> updateDiaChi(@Field("MaDiaChi") String MaDiaChi, @Field("HoTen") String HoTen,@Field("SDT") String SDT,@Field("ThanhPho") String ThanhPho,@Field("Quan") String Quan,@Field("Phuong") String Phuong, @Field("DiaChiNha") String DiaChiNha);
    //
    @FormUrlEncoded
    @POST("deleteDiaChi.php")
    Call<StringRequest> deleteDiaChi(@Field("MaDiaChi") String MaDiaChi);
    //
    @FormUrlEncoded
    @POST("datDiaChiMacDinh.php")
    Call<StringRequest> datDiaChiMacDinh(@Field("MaDiaChi") String MaDiaChi, @Field("MaKH") String MaKH);
    //GetTTKHByMaKH
    @FormUrlEncoded
    @POST("getTTKHByMaKH.php")
    Call<KhachHang> getTTKHByMaKH(@Field("MaKH") String MaKH);
    //Update KH
    @FormUrlEncoded
    @POST("updateAccountInfo.php")
    Call<StringRequest> updateAccountInfo(@Field("MaKH") String MaKH, @Field("HoTen") String HoTen,@Field("SDT") String SDT, @Field("NgaySinh") String NgaySinh, @Field("GioiTinh") String GioiTinh);
    //Update MK
    @FormUrlEncoded
    @POST("updateMatKhauKH.php")
    Call<StringRequest> updateMatKhauKH(@Field("MaKH") String MaKH, @Field("MatKhau") String MatKhau);
    //
    @GET("getAllPTVC.php")
    Call<PhiVanChuyen> getPTVC();
    //getAllPTVC.php
    //
    @FormUrlEncoded
    @POST("getTTGHByMaDiaChi.php")
    Call<TTGiaoHang> getTTGHByMaDiaChi(@Field("MaDiaChi") String MaDiaChi);
    //
    @FormUrlEncoded
    @POST("getAllOrderByMaKH.php")
    Call<List<DonHang>> getAllOrderByMaKH(@Field("MaKH") String MaKH);
    //
    @FormUrlEncoded
    @POST("getDetailOrderByMaDH.php")
    Call<ChiTietDonHang> getDetailOrderByMaDH(@Field("MaDH") String MaDH);
    //
    @FormUrlEncoded
    @POST("cancelOrder.php")
    Call<ResultRequest> cancelOrder(@Field("MaDH") String MaDH);
    //

}
