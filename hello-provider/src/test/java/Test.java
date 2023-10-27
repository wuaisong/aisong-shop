import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacpp.Loader;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_objdetect;

public class Test {
    public static void main(String[] args) throws FrameGrabber.Exception, FrameRecorder.Exception, InterruptedException {
        // Preload the opencv_objdetect module to work around a known bug.
        String str = Loader.load(opencv_objdetect.class);
        System.out.println(str);

        FrameGrabber grabber = FrameGrabber.createDefault(0);
        grabber.start();
        Frame grabbedImage = grabber.grab();//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        int width = grabbedImage.imageWidth;
        int height = grabbedImage.imageHeight;

        String outputFile = "d:\\record.mp4";
        //String outputFile = "rtmp://127.0.0.1:1935/rtmplive/picamera";
        FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height); //org.bytedeco.javacv.FFmpegFrameRecorder
        System.out.println(recorder.getClass().getName());//org.bytedeco.javacv.FFmpegFrameRecorder
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(25);
        recorder.start();//开启录制器
        long startTime = 0;
        long videoTS;
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma()); //2.2/2.2=1
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        Frame rotatedFrame;
        while (frame.isVisible() && (rotatedFrame = grabber.grab()) != null) {
            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = (System.currentTimeMillis() - startTime) * 1000;//这里要注意，注意位
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40);
        }
        recorder.stop();
        recorder.release();
        frame.dispose();
        grabber.stop();
        grabber.close();
    }
}