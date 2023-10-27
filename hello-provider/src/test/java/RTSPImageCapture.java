import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class RTSPImageCapture {
    public static void main(String[] args) {
        String rtsp = "rtsp://10.191.10.53:554/openUrl/QJiG6ly?beginTime=20230718T000000&endTime=20230718T000900&playBackMode=1"; // 替换为实际的RTSP URL
        String imgSrc = ""; // 图像保存路径
        String linuxImg = "/path/to/linux/img/"; // Linux系统下的保存路径
        String winImg = "E:\\agent\\"; // Windows系统下的保存路径

        try {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rtsp);
            // 使用tcp的方式，不然会丢包很严重
            grabber.setOption("rtsp_transport", "tcp");
            grabber.start();
            Frame frame = grabber.grabImage();
            if (frame != null) {
                if (imgSrc == null || imgSrc.isEmpty()) {
                    String path = "";
                    // if (SystemUtils.isLinux()) {
                    //     path = linuxImg;
                    // } else if (SystemUtils.isWindows()) {
                    path = winImg;
                    // }
                    imgSrc = path + "video.jpg";
                }
                File file = new File(imgSrc);
                file.createNewFile();
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.getBufferedImage(frame);
                ImageIO.write(bufferedImage, "jpg", file);
            }
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
