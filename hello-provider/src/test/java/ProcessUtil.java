import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessUtil {

    /**
     * just use java.lang.runtime (not use commons-io here)
     *
     * @param cmd
     * @return Aug 2, 2021
     */
    public static String runCmdByRuntime(String cmd) {
        long startTime = System.currentTimeMillis();
        logger.info("start to run cmd: {}\n", cmd);

        String stdOutput = null;
        Process proc = null;
        try {
            // exec
            String[] commands = {"bash", "-c", cmd};
            proc = Runtime.getRuntime().exec(commands);
            // UTF-8
            BufferedReader stdInput =
                new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8")); //ISO-8859-1
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "UTF-8"));

            // FIXME
            //2021-08-03 01:21:19,586 - task.detect - INFO - Detector reported 2369 findings in previous run on project 'closure' version 319. Skipping.
            //[[32mINFO   [0m] Detector reported 2369 findings in previous run on project 'closure' version 319. Skipping.
            // this might be caused by mubench logging formatter

            // output
            stdOutput = getOutputFromReader(stdInput);
            logger.info("standard output of the cmd: {}", stdOutput);
            String errorOutput = getOutputFromReader(stdError);
            logger.info("error output of the cmd: {}", errorOutput);
        } catch (Exception err) {
            err.printStackTrace();
        }

        // time cost
        long timeCost = (System.currentTimeMillis() - startTime) / 1000;
        logger.info("time cost of runCmd(): {}s", timeCost);

        return stdOutput;
    }

    /**
     * to serve runCmdByRuntime()
     *
     * @param br
     * @return
     * @throws IOException Aug 3, 2021
     */
    private static String getOutputFromReader(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String curStr = null;
        while ((curStr = br.readLine()) != null) {
            sb.append(curStr).append("\n");
        }
        br.close(); // close reader
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        List<String> commend = new ArrayList<>();
        commend.add("Notepad.exe");
        // commend.add("-c");
        //   /bin/bash -c '7zzs x /usr/data/tmp/测试.zip -o/usr/data/tmp/one/ && convmv -f GBK -t utf8 --notest -r /usr/data/tmp/one/'
        // commend.add("第1条命令 && 第2条命令 && 第3条命令 ...");
        //这里可以实现执行多条linux命令
        StringBuffer result = new StringBuffer();
        Integer status = ProcessUtil.exec(commend, result);
        logger.info("main result: {}", status);
    }

    private static final Logger logger = LoggerFactory.getLogger(ProcessUtil.class);
    private static final Integer WAIT_TIME = 4;

    /**
     * 执行脚本命令
     *
     * @param commands
     * @throws
     */
    public static Integer exec(List<String> commands, StringBuffer outPutResult) throws Exception {
        String[] arrCommands = list2Array(commands);
        ProcessBuilder processBuilder = new ProcessBuilder(arrCommands);
        processBuilder.redirectErrorStream(true);
        Process process = null;
        try {
            process = processBuilder.start();
            final BufferedReader br =
                new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            new Thread(() -> {
                try {
                    String str;
                    while ((str = br.readLine()) != null) {
                        if (outPutResult != null) {
                            outPutResult.append(str);
                        } else {
                            logger.info(str);
                        }
                    }
                } catch (Exception e) {
                    logger.error("获取子进程输出错误：", e);
                }
            });

            boolean alive = process.isAlive();
            logger.info("alive: {}", alive);
            boolean b = process.waitFor(WAIT_TIME, TimeUnit.SECONDS);
            logger.info("alive: {}", alive);
            logger.info("result: {}", b);
            return process.exitValue();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * List转String
     *
     * @param commands
     * @return
     */
    private static String[] list2Array(List<String> commands) {
        String[] commends = new String[commands.size()];
        commands.toArray(commends);
        return commends;
    }
}
