package wu.ai.song.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.util.Optional.ofNullable;

/**
 * @author wuyaming
 */
@RestController
@Slf4j
public class HelloController {
    @GetMapping("/hello")
    public Object hello() {
        log.debug("debug: hello~");
        log.info("info: hello~");
        log.warn("warn: hello~");
        log.error("error: hello~");
        return "Hello World~";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        session.setMaxInactiveInterval(3600);
        log.info(session.getAttribute("userInfo").toString());
        session.removeAttribute("userInfo");
        log.info(ofNullable(session.getAttribute("userInfo")).orElse("null").toString());
        return "ok";
    }
}
