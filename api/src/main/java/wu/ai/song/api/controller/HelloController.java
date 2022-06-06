package wu.ai.song.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wu.ai.song.api.entity.User;
import wu.ai.song.api.mapper.UserDao;
import wu.ai.song.util.ByteUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;

import static java.util.Optional.ofNullable;

/**
 * @author wuyaming
 */
@RestController
@Slf4j
public class HelloController {

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Value("${app.build.time}")
    private String appBuildTime;

    @Autowired
    private UserDao userDao;

    @GetMapping("/hello")
    public Object hello() {
        log.info("debug: hello~{}", springApplicationName);
        log.info("debug: hello~{}", appBuildTime);
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

    @GetMapping("/testTranslate")
    @Transactional(rollbackFor = Exception.class)
    public void testTranslate(HttpServletRequest request) {
        User user = new User();
        user.setName("墨白君");
        user.setAge(25);
        user.setEmail("mobaijun8@163.com");
        // mybatis-plus会自动帮助我们生成主键ID
        int insert = userDao.insert(user);
        // 被影响的行数
        System.out.println("insert = " + insert);
        // ID会自动回填
        System.out.println("user = " + user);
    }
}
