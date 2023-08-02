package wu.ai.song.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import wu.ai.song.api.service.TestService;

/**
 * @author 75318070
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @RequestMapping("/deferredResult")
    public DeferredResult<String> deferredResult() throws Exception {
        System.out.println("控制层执行线程:" + Thread.currentThread().getName());
        //超时
        DeferredResult<String> deferredResult = new DeferredResult<String>(10 * 1000L);
        deferredResult.onTimeout(() -> {
            System.out.println("异步线程执行超时");
            deferredResult.setResult("线程执行超时");
        });
        deferredResult.onCompletion(() -> System.out.println("异步执行完毕"));
        taskExecutor.execute(() -> {
            System.out.println("异步执行线程:" + Thread.currentThread().getName());
            try {
                String str = testService.task2();
                Thread.sleep(1000);
                deferredResult.setResult("这是【异步】的请求返回: " + str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return deferredResult;
    }
}
