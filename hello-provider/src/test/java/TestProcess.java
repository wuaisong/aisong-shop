import java.util.concurrent.TimeUnit;

public class TestProcess {
    public static void main(String[] args) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Launching of Notepad Application");
        Process process = runtime.exec("Notepad.exe");
        // 启动记事本应用程序
        System.out.println("等待5秒钟");
        process.waitFor(5, TimeUnit.SECONDS);
        System.out.println("退出记事本应用程序");
        process.destroy();
        //销毁应用程序
    }
}
