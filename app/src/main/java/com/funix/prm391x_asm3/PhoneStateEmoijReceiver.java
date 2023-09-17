package com.funix.prm391x_asm3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

//class lắng nghe cuộc gọi đến,
public class PhoneStateEmoijReceiver extends BroadcastReceiver {
    private static String TAG = PhoneStateEmoijReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // tạo một đối tượng TelephonyManager từ mảng Context thông qua getSystemService.
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //gọi phương thức listen() của đối tượng TelephonyManager để lắng nghe sự thay đổi trạng thái cuộc gọi điện
        telephony.listen(new PhoneStateListener() {
            //Phương thức hiển thị một hình ảnh tương ứng với số điện thoại khi có một cuộc gọi đến.
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                //nếu trạng thái không phải là CALL_STATE_RINGING (cuộc gọi đang đổ chuông),
                // thì kết thúc phương thức
                if (state != TelephonyManager.CALL_STATE_RINGING) {
                    return;
                }
                //in ra màn hình thông tin về số điện thoại và trạng thái của cuộc gọi.
                Log.i(TAG, "This is phone number: " + phoneNumber);
                Log.i(TAG, "This is state: " + state);
                //lấy ra đường dẫn (path) dựa trên số điện thoại từ SharedPreferences.
                SharedPreferences sharedPref = context.getSharedPreferences("FILE_SAVED", Context.MODE_PRIVATE);
                String path = sharedPref.getString(phoneNumber, "");
                Log.i(TAG, "This is path :" + path);
                try {
                    //Nếu mở thành công, phương thức giải mã tệp tin ảnh thành một đối tượng Bitmap
                    Bitmap photo = BitmapFactory.decodeStream(context.getAssets().open(path));
                    //phương thức inflate một layout toast từ tệp tin layout_toast.xml
                    View iv_toast = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
                    //Đặt đối tượng Bitmap vào ImageView trong layout_toast
                    ((ImageView) iv_toast.findViewById(R.id.iv_toast)).setImageBitmap(photo);
                    Toast toast = new Toast(context);//Tạo một đối tượng Toast
                    toast.setView(iv_toast);//gán layout_toast vào Toast
                    toast.setDuration(Toast.LENGTH_LONG);//thời gian hiển thị toast
                    toast.show();//hiển thị toast lên màn hình
                    //Xử lý ngoại lệ IOException nếu có lỗi xảy ra trong quá trình đọc tệp tin ảnh.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);//lắng nghe các sự kiện thay đổi trạng thái cuộc gọi điện
    }
}