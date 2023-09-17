package com.funix.prm391x_asm3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends Fragment {

    private static final String TAG = MenuFragment.class.getName();
    SharedPreferences sharedPref;
    private Context mContext;
    private RecyclerView rvAnimal;
    private List<Animal> listAnimal;
    private DrawerLayout mDrawer;
    private RecyclerView.LayoutManager mLayoutManager;
    private String animalType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        //khởi tạo một SharedPreferences có tên FILE_SAVED
        sharedPref = mContext.getSharedPreferences("FILE_SAVED", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    //hàm khởi tạo cấu hình,giao diện người dùng
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //tạo đối tượng view để hiển thị lên fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mDrawer = view.findViewById(R.id.drawer);//tham chiếu đến DrawerLayout
        rvAnimal = view.findViewById(R.id.rv_animal);//tham chiếu đến RecyclerView

        if (listAnimal == null) {//nếu listAnimal null thì gọi initView(view) để khởi tạo giao diện người dùng
            initView(view);
        } else {//nếu không null
            //khởi tạo animalAdapter
            AnimalAdapter animalAdapter = new AnimalAdapter(listAnimal, mContext, animalType);
            rvAnimal.setAdapter(animalAdapter);//gán animalAdapter cho RecyclerView
            initView(view);//hàm khởi tạo giao diện người dùng
        }
        return view;//trả về đối tượng View đã được khởi tạo và cấu hình.

    }

    //Hàm xử lý click
    private void initView(View view) {
        //khi click vào nút menu
        view.findViewById(R.id.iv_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            // Hàm mở drawer từ cạnh bên trái (START) của màn hình
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });
        //click vào iv_sea
        view.findViewById(R.id.iv_sea).setOnClickListener(new View.OnClickListener() {
            @Override
            //gọi hàm showAnimals("sea")
            public void onClick(View view) {
                showAnimals("sea");
            }
        });
        //click vào iv_mammal
        view.findViewById(R.id.iv_mammal).setOnClickListener(new View.OnClickListener() {
            @Override
            //gọi hàm showAnimals("mammal")
            public void onClick(View view) {
                showAnimals("mammal");
            }
        });
        //click vào iv_bird
        view.findViewById(R.id.iv_bird).setOnClickListener(new View.OnClickListener() {
            @Override
            //gọi hàm showAnimals("bird")
            public void onClick(View view) {
                showAnimals("bird");
            }
        });
    }

    //hàm hiển thị danh sách các động vật
    private void showAnimals(String animalType) {
        this.animalType = animalType;
        // đóng drawer từ cạnh bên trái (START) của màn hình
        mDrawer.closeDrawer(GravityCompat.START, true);
        //tạo đối tượng listAnimal để chứa danh sách các đối tượng Animal
        listAnimal = new ArrayList<Animal>();

        try {
            //liệt kê tất cả các tệp trong thư mục photo/ với loại động vật tương ứng.
            String[] listAnimals = mContext.getAssets().list("photo/" + animalType);

            for (String item : listAnimals) {
                String path = "photo/" + animalType + "/" + item;//tạo đường dẫn đến ảnh và các file
                //tạo đối tương Bitmap photo từ các đường dẫn đến ảnh
                Bitmap photo = BitmapFactory.decodeStream(mContext.getAssets().open(path));
                String photoName = item.substring(3, item.indexOf("."));//Trích xuất tên của ảnh từ item và chuẩn hóa tên động vật.
                String name = photoName.substring(0, 1).toUpperCase() + photoName.substring(1).toLowerCase().replace("_", " ");
                //Tạo đối tượng Bitmap từ tệp ảnh nền (photo_bg) tương ứng với động vật
                Bitmap photoBg = BitmapFactory.decodeStream(mContext.getAssets().open("photo_bg/" + animalType + "/bg_" + photoName + ".jpg"));
                Boolean isFav = sharedPref.getBoolean(path, false);//false;
                InputStream in = null;
                in = mContext.getAssets().open("text/" + animalType + "/" + photoName + ".txt");
                //đọc dữ liệu từ đầu vào in
                BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                String str = null;
                StringBuilder description = new StringBuilder();
                //xử dụng vòng lặp để đọc và thêm nội dung mỗi dòng vào description
                while ((str = br.readLine()) != null) {
                    description.append(str);
                }
                br.close();//đóng BufferedReader sau khi đọc xong dữ liệu
                //đổi đối tượng StringBuilder description thành chuỗi content
                // bằng cách sử dụng phương thức toString().
                String content = description.toString();

                Log.i(TAG, "path :" + path);//ghi thông điệp log
                //thêm đối tượng Animal này vào danh sách listAnimal`.
                listAnimal.add(new Animal(path, photo, photoBg, name, content, isFav));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnimalAdapter animalAdapter = new AnimalAdapter(listAnimal, mContext, animalType);
        rvAnimal.setAdapter(animalAdapter);//hiển thị các đối tượng Animal trong danh sách.
    }


}