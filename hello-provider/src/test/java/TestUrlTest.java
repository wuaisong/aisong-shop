import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUrlTest {
    public static void main(String[] args) throws IOException, InterruptedException {

        // time=00:00:02.52 bitrate
        String text3 = "frame=  930 fps= 29 q=30.0 size=    6400kB time=00:00:32.72 bitrate=1602.0kbits/s speed=1.01x";
        String reg3 = "(?<=time=)[\\s\\S]*(?= bitrate)";
        Pattern p3 = Pattern.compile(reg3);
        Matcher m3 = p3.matcher(text3);
        String group = "";
        if (m3.find()) {
            group = m3.group();
        }
        System.out.println(group);
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm:ss.SS");
        LocalTime localTime = LocalTime.parse(group, parser);
        System.out.println(localTime);

        // int rs = 0;
        // String[] cmds = {"ffmpeg", "-rtsp_transport", "tcp", "-i",
        //     "rtsp://10.191.10.53:554/openUrl/d5r8gYE?beginTime=20230718T000800&endTime=20230718T000900&playBackMode=1",
        //     "a.mp4", "-y"};//command and arg
        // ProcessBuilder builder = new ProcessBuilder(cmds);
        // builder.redirectErrorStream(true);
        // Process process = builder.start();
        // BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        // String output;
        // while (null != (output = br.readLine())) {
        //     System.out.println(output);
        // }
        // rs = process.waitFor();
        // System.out.println(rs);
    }
}