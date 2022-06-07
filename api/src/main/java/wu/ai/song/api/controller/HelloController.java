package wu.ai.song.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wu.ai.song.api.entity.User;
import wu.ai.song.api.mapper.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    /**
     * 测试事务
     *
     * @param request
     * @see org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration 自动配置事务
     * @see org.springframework.transaction.annotation.EnableTransactionManagement 开启事务
     * @see org.springframework.context.annotation.AutoProxyRegistrar
     * @see org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration
     * @see org.springframework.aop.config.AopConfigUtils
     * @see org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator
     * @see org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator
     * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization(java.lang.Object, java.lang.String)
     * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     * @see com.baomidou.mybatisplus.core.override.MybatisMapperProxy#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     * @see org.mybatis.spring.SqlSessionTemplate
     * @see SqlSessionTemplate#SqlSessionTemplate(org.apache.ibatis.session.SqlSessionFactory, org.apache.ibatis.session.ExecutorType, org.springframework.dao.support.PersistenceExceptionTranslator)
     * @see SqlSessionUtils#getSqlSession(org.apache.ibatis.session.SqlSessionFactory, org.apache.ibatis.session.ExecutorType, org.springframework.dao.support.PersistenceExceptionTranslator)
     */

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
