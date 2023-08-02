package wu.ai.song.api.service.imp;

import org.springframework.stereotype.Service;
import wu.ai.song.api.service.TestService;

@Service
public class PiceaServiceImpl implements TestService {

    @Override
    public void task() throws Exception {
        System.out.println("------------------------在看貂蝉，不要打扰--------------");
        Thread.sleep(1000);
    }

    @Override
    public String task2() throws Exception {
        int k = 1;
        System.out.println("------------------------在看鱼，不要打扰--------------");
        Thread.sleep(1000);
        return (String.valueOf(k));
    }
}
