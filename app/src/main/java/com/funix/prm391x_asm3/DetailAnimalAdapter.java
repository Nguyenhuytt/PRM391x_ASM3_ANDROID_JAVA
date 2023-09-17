package com.funix.prm391x_asm3;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

//class quản lý và cập nhật dữ liệu chi tiết cho animal
public class DetailAnimalAdapter extends PagerAdapter {
    SharedPreferences sharedPref;//khai báo biến sharedPref dùng để lưu trữ dữ liệu theo cặp khóa-giá trị trong SharePreferences của ứng dụng.
    SharedPreferences.Editor editor;//biến editor sử dụng để chỉnh sửa và lưu trữ các giá trị trong SharedPreferences.
    private Context mContext;//biến mcontext sử dụng để lưu trữ tham chiếu đến Context của ứng dụng
    private List<Animal> listAnimal;//khai báo danh sách kiểu Animal

    //contructor
    public DetailAnimalAdapter(List<Animal> listAnimal, Context mContext) {
        this.listAnimal = listAnimal;
        this.mContext = mContext;
        sharedPref = mContext.getSharedPreferences("FILE_SAVED", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //Lấy view từ layout item_detail_animal
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_animal, container, false);
        Animal item = listAnimal.get(position);
        //khai báo các biến giao diện và ánh xạ chúng
        ImageView iv_bg = view.findViewById(R.id.iv_animal_dtl_background);
        TextView tv_name = view.findViewById(R.id.tv_animal_dtl_name);
        TextView tv_description = view.findViewById(R.id.tv_animal_dtl_text);
        ImageView iv_fav = view.findViewById(R.id.iv_fav);
        TextView tv_phone = view.findViewById(R.id.tv_phone);
        //đặt văn bản cho tv_phone từ dữ liệu được lấy ra từ SharedPreferences
        tv_phone.setText(sharedPref.getString(item.getPath() + "_phone", ""));

        //kiểm tra trạng thái yêu thích của đối tượng Animal và
        // cập nhật hình ảnh yêu thích tương ứng trên ImageView iv_fav.
        if (!item.isFav()) {//nếu false thì không thưc hiện câu lệnh dưới
            iv_fav.setImageResource(R.drawable.ic_favorite2);
        } else if (item.isFav()) {//nếu đúng thì thực hiện câu lệnh dưới đây
            iv_fav.setImageResource(R.drawable.ic_favorite1);
        }
        iv_fav.setOnClickListener(view1 -> {
            //Khi người dùng nhấp vào,thì animation alpha và hình ảnh mới được set cho iv_fav,
            // cùng với cập nhật trạng thái yêu thích và lưu trữ các thay đổi trong SharedPreferences.
            iv_fav.startAnimation(AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.alpha));
            //kiểm tra đối tượng Animal có được yêu thích hay không
            if (!item.isFav()) {//nếu Animal không được yêu thích thì thiết lập lại yêu thích
                iv_fav.setImageResource(R.drawable.ic_favorite1);
                item.setFav(true);//đặt là true

                editor.putBoolean(item.getPath(), true);
                editor.apply();
            } else if (item.isFav()) {//nếu đã được yêu thích thì khi click vào sẽ thiết lập lại hình ảnh không được yêu thích
                iv_fav.setImageResource(R.drawable.ic_favorite2);
                item.setFav(false);
                // hai câu lệnh sau được sử dụng để xóa một thông tin đã lưu trữ trong SharedPreferences
                // và lưu trữ các thay đổi sau khi thông tin đã được xóa.
                editor.remove(item.getPath());
                editor.apply();
            }
        });

        iv_bg.setImageBitmap(item.getPhotoBg());//Đặt ảnh cho iv_bg
        tv_name.setText(item.getName());//Đặt tên cho tv_name
        tv_description.setText(item.getContent());//Đặt nội dung cho từng animal
        //xử lý click vào tv_phone
        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tạo đối tượng AlertDialog để xây dựng một dialog
                AlertDialog alert = new AlertDialog.Builder(mContext).create();
                //Sử dụng LayoutInflater để tạo đối tượng View dialog
                View dialog = LayoutInflater.from(mContext).inflate(R.layout.phone_diaglog, null);
                //khai báo và ánh xạ các biến giao diện
                ImageView iv_dialog = dialog.findViewById(R.id.iv_dialog_animal);
                Button btn_dialog_save = dialog.findViewById(R.id.btn_dialog_save);
                Button btn_dialog_delete = dialog.findViewById(R.id.btn_dialog_delete);
                EditText edt_dialog_animal = dialog.findViewById(R.id.edt_dialog_animal);
                //đặt văn bản cho edt_dialog_animal lấy ra từ SharedPreferences
                edt_dialog_animal.setText(sharedPref.getString(item.getPath() + "_phone", ""));
                //xử lý click btn_dialog_save
                btn_dialog_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tv_phone.setText(edt_dialog_animal.getText());//Thiết lập văn bản cho textView
                        //lưu trữ chuỗi giá trị vào SharedPreferences được nhập trong edt_dialog_animal
                        editor.putString(item.getPath() + "_phone", edt_dialog_animal.getText().toString());
                        editor.putString(edt_dialog_animal.getText().toString(), item.getPath());
                        editor.apply();//áp dụng các thay đổi
                        alert.dismiss();//đóng hộp thoại
                    }
                });
                //Xử lý click btn_dialog_delete
                btn_dialog_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_phone.setText("");//Thiết lập textView thành chuỗi rỗng
                        //xóa chuỗi được lấy ra từ SharedPreferences
                        editor.remove(item.getPath() + "_phone");
                        editor.remove(sharedPref.getString(item.getPath() + "_phone", ""));
                        editor.apply();//áp dụng các thay đổi
                        alert.dismiss();//đóng hộp thoại
                    }
                });
                //hiển thị một hộp thoại AlertDialog
                iv_dialog.setImageBitmap(item.getPhoto());
                alert.setView(dialog);
                alert.show();
            }
        });

        container.addView(view);//thêm view vào container
        return view;//trả về view đã thêm
    }

    @Override
    public int getCount() {
        return listAnimal.size();
    }//trả về số lượng phần tử trong listAnimal

    //sử dụng để kiểm tra xem một View có đượcliên kết với một
    // đối tượng cụ thể trong danh sách dữ liệu của adapter hay không
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    //sử dụng để xóa một View khỏi một ViewGroup (container)
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}