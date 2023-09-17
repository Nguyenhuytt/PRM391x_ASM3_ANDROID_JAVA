package com.funix.prm391x_asm3;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int CALL_PHONE_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        MenuFragment menuFragment = new MenuFragment();
        //Lấy thể hiện của FragmentManager từ Activity hiện tại
        getSupportFragmentManager()
                .beginTransaction()
                //Thay thế fragment hiện tại trong container có id ln_main bằng fragment menuFragment.
                .replace(R.id.ln_main, menuFragment, null)
                .addToBackStack(null)//Thêm fragment hiện tại vào back stack của FragmentManager
                .commit();
        //Kiểm tra ứng dụng đã được cấp quyền truy cập trạng thái điện thoại và nhật ký cuộc gọi hay chưa
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{//nếu chưa thì yêu cầu quyền truy cập
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG,
            }, CALL_PHONE_CODE);
        }
    }

    //phương thức chuyển đổi Fragment
    public void gotoDetailFragment(String animalType, List<Animal> listAnimal, Animal animal) {
        DetailFragment detailFragment = new DetailFragment();//Khởi tạo đối tượng detailFragment
        detailFragment.setData(listAnimal, animalType, animal);//Thiết lập dữ liệu cho fragment
        Log.i(TAG, "gotoDetailFragment: pass");
        //Lấy thể hiện của FragmentManager từ Activity hiện tại
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ln_main, detailFragment, null)//Thay thế fragment hiện tại trong container có id ln_main bằng fragment detailFragment.
                .addToBackStack(null)//Thêm fragment hiện tại vào back stack của FragmentManager
                .commit();


    }

    public void backtoMenuFragment(String animalType, List<Animal> listAnimal) {
        //gọi popBackStackImmediate() để xóa fragment hiện tại và quay lại fragment trước
        getSupportFragmentManager().popBackStackImmediate();

    }

}