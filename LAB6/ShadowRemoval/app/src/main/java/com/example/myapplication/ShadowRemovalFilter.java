package com.example.myapplication;



import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ShadowRemovalFilter {

    // Tạo cổng kết nối để gửi ảnh về màn hình sau khi xử lý xong
    public interface MyCallBack {
        void onComplete(Bitmap bitmap);
    }

    public static void getShadowFilteredImage(Bitmap bit_map, final MyCallBack callBack) {
        // Mở luồng chạy ngầm để ứng dụng không bị đứng khi tính toán ảnh nặng
        Executor executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // 1. Chuyển ảnh sang ma trận OpenCV
                Mat srcArry = new Mat(bit_map.getWidth(), bit_map.getHeight(), CvType.CV_8UC1);
                Utils.bitmapToMat(bit_map, srcArry);

                // 2. Chuyển hệ màu sang HSV để tách vùng sáng/tối
                Imgproc.cvtColor(srcArry, srcArry, Imgproc.COLOR_BGR2HSV);

                List<Mat> bgrPlanes = new ArrayList<>();
                List<Mat> list = new ArrayList<>();
                List<Mat> result = new ArrayList<>();

                Core.split(srcArry, bgrPlanes);
                list.add(bgrPlanes.get(2)); // Lấy kênh V (ánh sáng) để xử lý bóng
                result.add(0, bgrPlanes.get(0));
                result.add(1, bgrPlanes.get(1));

                // 3. Thuật toán lọc bóng
                for (Mat mat : list) {
                    Mat dilated_img = new Mat();
                    Mat kernel = Mat.ones(7, 7, CvType.CV_32F);
                    Imgproc.dilate(mat, dilated_img, kernel);
                    Imgproc.medianBlur(dilated_img, dilated_img, 21);

                    Mat diff = new Mat();
                    Core.absdiff(mat, dilated_img, diff);
                    Core.bitwise_not(diff, diff);

                    Mat norm = diff.clone();
                    Core.normalize(diff, norm, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);

                    result.add(norm); // Ghép kênh V đã xóa bóng vào lại
                }

                // 4. Gộp lại và chuyển về hệ màu RGB bình thường
                Mat result_norm = new Mat();
                Core.merge(result, result_norm);
                Imgproc.cvtColor(result_norm, result_norm, Imgproc.COLOR_HSV2BGR);

                // 5. Trả về ảnh Bitmap
                final Bitmap res_ult = Bitmap.createBitmap(srcArry.cols(), srcArry.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(result_norm, res_ult);

                // 6. Gửi kết quả lên giao diện chính
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onComplete(res_ult);
                    }
                });
            }
        });
    }
}