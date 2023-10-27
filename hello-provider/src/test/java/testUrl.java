import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import java.io.File;
import java.io.IOException;
/**
 * @ClassName testUrl
 * @Author dell-pc
 * @create 2023/3/9 16:28
 */
public class testUrl  implements Runnable {

    public static void main(String[] args) {
        testUrl testUrl = new testUrl();
        testUrl.outFilePath = "C:\\test.flv";
        //最好设置结束时长 如直接停止程序会造成输出文件的损坏无法正常播放
        testUrl.timesSec = 15L;
        testUrl.hasAudio = true;
        new Thread(testUrl).start();
    }


    /**
     * 流地址 例如：rtmp://58.200.131.2:1935/livetv/hunantv 湖南卫视
     */
    private String streamUrl = "rtsp://10.191.10.53:554/openUrl/hcBR2kU?beginTime=20230718T000000&endTime=20230718T000900&playBackMode=1";
    /**
     * 停止录制时长 0为不限制时长
     */
    private long timesSec = 0L;
    /**
     * 视频文件的输出路径
     */
    private String outFilePath;
    /**
     * 录制的视频文件格式(文件后缀名)
     */
    private String filenameExtension = "mp4";
    /**
     * 是否录制音频
     */
    private boolean hasAudio = true;

    @Override
    public void run() {
        if(outFilePath == null || outFilePath.length() == 0){
            System.out.println("文件输出路径不能为空。");
            return;
        }
        //根据直播链接实例FFmpeg抓帧器
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(streamUrl);
        FFmpegFrameRecorder recorder = null;
        try {
            grabber.setOption("rtsp_transport", "tcp");
            grabber.start();
            Frame frame = grabber.grabFrame();
            if (frame != null) {
                //保存到本地的文件
                File outFile = new File(outFilePath);
                //文件不存在 || 文件不是一个普通文件
                if(!outFile.exists() || !outFile.isFile()){
                    if(!outFile.createNewFile()){
                        System.out.println("文件创建失败");
                        return;
                    }
                }
                // 视频输出地址,视频宽分辨率(宽,高),是否录制音频（0:不录制/1:录制）
                recorder = new FFmpegFrameRecorder(outFilePath, frame.imageWidth, frame.imageHeight, hasAudio ? 1 : 0);
                //直播流格式
                //录制的视频格式
                recorder.setFormat("flv");
                //视频帧数
                recorder.setFrameRate(60);
                //开始录制
                recorder.start();
                // 计算结束时间
                long endTime = System.currentTimeMillis() + timesSec * 1000;
                // 如果没有到录制结束时间并且获取到了下一帧则继续录制
                // while ((System.currentTimeMillis() < endTime) && (frame != null)) {
                while (frame != null) {
                        System.out.println("test");
                        //录制
                        recorder.record(frame);
                        //获取下一帧
                        frame = grabber.grabFrame();
                }
                recorder.record(frame);
            }
            System.out.println("录制完成。");
        } catch (IOException e) {
            System.out.println("录制出错。");
            e.printStackTrace();
        } finally {
            //停止录制
            try {
                grabber.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
            if (recorder != null) {
                try {
                    recorder.stop();
                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

