package com.funix.prm391x_asm3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class DetailFragment extends Fragment {

    private static String TAG = DetailFragment.class.getName();
    private Context mContext;//biến mContext sử dụng để lưu trữ tham chiếu đến Context của ứng dụng
    private List<Animal> listAnimal;//khai báo danh sách kiếu Animal
    private Animal currentItem;//khai báo biến currentItem kiếu Animal
    private String animalType;//khai báo biến animalType dùng để xác định kiếu animal

    //phương thức thiết lập dữ liệu mới cho Adapter
    public void setData(List<Animal> listAnimal, String animalType, Animal currentItem) {
        this.listAnimal = listAnimal;
        this.animalType = animalType;
        this.currentItem = currentItem;
        Log.i(TAG, "listAnimal: " + listAnimal.get(0).getName());//hiển thị tên Aniaml đầu tiên
        Log.i(TAG, "animalType: " + animalType);//hiển thị giá trị chuỗi animalType
        Log.i(TAG, "currentItem: " + currentItem.getName());//hiển thị tên animal currentItem
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, null);
        initViews(view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initViews(View view) {
        view.findViewById(R.id.iv_back).setVisibility(View.VISIBLE);//hiển thị nút back
        view.findViewById(R.id.iv_menu).setVisibility(View.GONE);//ẩn nút menu
        //xử lý click
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //quay lại fragment trước
                ((MainActivity) getActivity()).backtoMenuFragment(animalType, listAnimal);
            }
        });

        ViewPager vp = view.findViewById(R.id.vp_animal_dtl);//tìm và tham chiếu đến viewpager trong giao diện
        DetailAnimalAdapter adapter = new DetailAnimalAdapter(listAnimal, mContext);//khởi tạo một đối tượng DetailAnimalAdapter
        vp.setAdapter(adapter);//gán adapter cho viewPager
        vp.setCurrentItem(listAnimal.indexOf(currentItem));//đặt trạng thái hiển thị ban đầu của ViewPager bằng vị trí CurrentItem
    }
}