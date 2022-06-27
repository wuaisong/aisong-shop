package wu.ai.song.api.utils;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 自定义BeanPostProcessor实现类
 * BeanPostProcessor接口的作用是：
 * 	 我们可以通过该接口中的方法在bean实例化、配置以及其他初始化方法前后添加一些我们自己的逻辑
 * @author dengp
 *
 */
@Component
@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor{

	/**
	 * 实例化、依赖注入完毕，在调用显示的初始化之前完成一些定制的初始化任务
	 * 注意：方法返回值不能为null
	 * 如果返回null那么在后续初始化方法将报空指针异常或者通过getBean()方法获取不到bena实例对象
	 * 因为后置处理器从Spring IoC容器中取出bean实例对象没有再次放回IoC容器中
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// System.out.println("before--实例化的bean对象:"+bean+"\t"+beanName);
		// 可以根据beanName不同执行不同的处理操作
		return bean;
	}

	/**
	 * 实例化、依赖注入、初始化完毕时执行 
	 * 注意：方法返回值不能为null
	 * 如果返回null那么在后续初始化方法将报空指针异常或者通过getBean()方法获取不到bena实例对象
	 * 因为后置处理器从Spring IoC容器中取出bean实例对象没有再次放回IoC容器中
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// 可以根据beanName不同执行不同的处理操作
        if ("sqlSessionFactory".equals(beanName)) {
            System.out.println("after...实例化的bean对象:"+bean+"\t"+beanName);
            DefaultSqlSessionFactory defaultSqlSessionFactory = (DefaultSqlSessionFactory) bean;
            MybatisConfiguration configuration = (MybatisConfiguration) defaultSqlSessionFactory.getConfiguration();
            GlobalConfig globalConfig = configuration.getGlobalConfig();
            globalConfig.setIdentifierGenerator(new CustomIdGenerator());
        }
		return bean;
	}

}