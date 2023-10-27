import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import java.io.IOException;

public class RtspTransform {

    public static void main(String[] args) throws IOException {
        String rtspUrl="rtsp://10.191.10.53:554/openUrl/LFxZpXa?beginTime=20230718T000000&endTime=20230718T000900&playBackMode=1";
        String hlsUrl="D:\\code\\temp\\hls\\baibing.m3u8";
        RtspTransform.convertMediaToM3u8ByHttp(rtspUrl,
               hlsUrl ,"5",20);
    }


    public static void convertMediaToM3u8ByHttp(String inputStream, String m3u8Url, String hls_time,int maxSize) throws IOException {

        avutil.av_log_set_level(avutil.AV_LOG_INFO);
        FFmpegLogCallback.set();

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(m3u8Url, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());

        recorder.setFormat("hls");
        recorder.setOption("hls_time", hls_time);
        recorder.setOption("hls_list_size", maxSize-1+"");
        recorder.setOption("hls_flags", "delete_segments");
        recorder.setOption("hls_delete_threshold", "1");
        recorder.setOption("hls_segment_type", "mpegts");


        // http属性
        recorder.setOption("method", "POST");

        recorder.setFrameRate(25);
        recorder.setGopSize(2 * 25);
        recorder.setVideoQuality(1.0);
        recorder.setVideoBitrate(10 * 1024);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.start();

        Frame frame;
        while ((frame = grabber.grabImage()) != null) {
            try {
                recorder.record(frame);
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
        recorder.setTimestamp(grabber.getTimestamp());
        recorder.close();
        grabber.close();
    }

}