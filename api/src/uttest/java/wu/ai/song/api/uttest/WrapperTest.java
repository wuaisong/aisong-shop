package wu.ai.song.api.uttest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.cursor.Cursor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import wu.ai.song.api.entity.User;
import wu.ai.song.api.mapper.UserDao;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class WrapperTest {

    @Autowired
    private UserDao userDao;


    /**
     * 条件查询
     */
    @Test
    public void wrapper1() {
        // 查询name不为空和email不为空并且年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 链式编程
        wrapper.isNotNull("name").isNotNull("email").ge("age", 20);
        userDao.selectList(wrapper).forEach(System.out::println);
    }


    /**
     * 根据名称查询
     */
    @Test
    public void wrapper2() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "MoBai");
        // selectOne(wrapper);查询一个数据
        User user = userDao.selectOne(wrapper);
        System.out.println("user = " + user);
    }

    /**
     * 查询区间
     */
    @Test
    public void wrapper3() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 查询年龄在15-25之间的数据
        wrapper.between("age", 15, 25);
        // selectOne(wrapper);查询一个数据
        Integer count = userDao.selectCount(wrapper);
        System.out.println(count);
    }

    /**
     * 模糊查询
     */
    @Test
    public void test4() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.
                // 查询name不包含e的字段
                        notLike("name", "e").
                // 左和右 t%
                        likeRight("email", "t");
        List<Map<String, Object>> maps = userDao.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    /**
     * 模糊查询
     */
    @Test
    public void test5() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // id在子查询中查出来
        wrapper.inSql("id", "select id from user where id < 3");
        List<Object> list = userDao.selectObjs(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 模糊查询
     */
    @Test
    public void test6() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 通过ID进行排序
        wrapper.orderByAsc("id");
        List<User> list = userDao.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * @link https://juejin.cn/post/6999145665787854856
     * 测试驼峰转换
     * 新增事务后方法会公用一个sql session
     * TransactionAspectSupport#invokeWithinTransaction
     * SqlSessionTemplate#selectList(String, Object)
     * TransactionSynchronization
     * AbstractPlatformTransactionManager
     * AbstractPlatformTransactionManager#processCommit
     */
    @Test
    @Transactional
    public void test7() {
        // 第一次查询
        List<User> list = userDao.getDepartmentAll();
        list.forEach(user -> user.setName("MoBai~~~~"));
        // 第二次查询
        List<User> list12 = userDao.getDepartmentAll();
        list12.forEach(System.out::println);
    }

    @Test
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void test8() {
        Cursor<User> users = userDao.cursorDepartment();
        users.forEach(user -> user.setName("MoBai~~~~"));
    }
}