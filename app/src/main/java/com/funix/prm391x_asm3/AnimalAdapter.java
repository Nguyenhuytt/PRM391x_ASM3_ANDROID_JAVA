package com.funix.prm391x_asm3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//class quản lý và cập nhật dữ liệu vào View
public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalHolder> {

    private List<Animal> listAnimal;//khởi tạo danh sách animal
    private Context mContext;//khai báo biến mcontext
    private String animalType;//khởi tạo kiểu animal

    //Tạo contructor của lớp AnimalAdapter
    public AnimalAdapter(List<Animal> listAnimal, Context mContext, String animalType) {
        this.listAnimal = listAnimal;
        this.mContext = mContext;
        this.animalType = animalType;
    }

    //Phương thức tạo ra và trả về một ViewHolder mới cho mỗi item trong RecyclerView
    @NonNull
    @Override
    public AnimalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Sử dụng LayoutInflater để inflate (khởi tạo) layout của item_animal,
        // sử dụng mContext làm context và parent làm ViewGroup cha
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_animal, parent, false);
        return new AnimalHolder(view);//Trả về một đối tượng AnimalHolder mới
    }

    @Override
    //Phương thức gắn dữ liệu từ đối tượng Animal vào các thành phần giao diện của item trong RecyclerView
    public void onBindViewHolder(@NonNull AnimalHolder holder, int position) {
        Animal animal = listAnimal.get(position);//lấy ra đối tượng Animal từ vị trí possition
        holder.iv_item_animal.setImageBitmap(animal.getPhoto());//Đặt hình ảnh cho ImageView trong ViewHolder
        holder.tv_item_animal.setText(animal.getName());//Đặt text cho TextView trong ViewHolder
        holder.iv_item_animal.setTag(animal);//Lưu trữ thông tin liên quan đến Animal trên ViewHolder

        if (animal.isFav() == true) {//kiểm tra trạng thái của isFav()
            holder.iv_fav.setVisibility(View.VISIBLE);//iv_fav được hiển thị
        } else if (animal.isFav() == false) {
            holder.iv_fav.setVisibility(View.INVISIBLE);//iv_fav được ẩn
        }
    }

    //Phương thức số lượng item trong listAnimal
    @Override
    public int getItemCount() {
        return listAnimal.size();
    }

    //class chứa thông tin của một View
    public class AnimalHolder extends RecyclerView.ViewHolder {
        //khai báo các biến giao diện
        ImageView iv_item_animal;
        TextView tv_item_animal;
        ImageView iv_fav;

        public AnimalHolder(@NonNull View itemView) {
            super(itemView);
            //ánh xạ id
            iv_item_animal = itemView.findViewById(R.id.iv_item_animal);
            tv_item_animal = itemView.findViewById(R.id.tv_item_animal);
            iv_fav = itemView.findViewById(R.id.iv_fav);
            //xử lý click cho từng itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // sử dụng để bắt đầu chạy một hoạt hình (animation) trên một đối tượng View
                    view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha));
                    //sử dụng để chuyển từ màn hình hiện tại đến màn hình chi tiết (detail) trong ứng dụng
                    ((MainActivity) mContext).gotoDetailFragment(animalType, listAnimal, (Animal) itemView.findViewById(R.id.iv_item_animal).getTag());

                }
            });
        }
    }
}