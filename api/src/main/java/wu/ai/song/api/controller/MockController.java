package wu.ai.song.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wu.ai.song.api.service.Demo;
import wu.ai.song.api.utils.Result;

/**
 * @author wuyaming
 */
@RestController
@Slf4j
public class MockController {
    @Autowired
    private Demo demo;

    /**
     * 抢红包 拆红包 抢到不一定能拆到
     */
    @GetMapping("/startTwoMock")
    public void startTwo() {
        Result result = demo.startTwoSeckil(1, 1);
        log.info("current result: {}", result);
    }
}
