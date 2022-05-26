package wu.ai.song.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;
import org.springframework.stereotype.Repository;
import wu.ai.song.api.entity.User;

import java.util.List;

/**
 * Software：IntelliJ IDEA 2020.1 x64
 * Author: MoBai·杰
 * Date: 2020/6/8 14:05
 * ClassName:UserDao
 * 使用mybatis-plus,每个mapper接口都需要继承BaseMapper
 * BaseMapper类描述： Mapper继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 */
@Repository // 表示持久层
public interface UserDao extends BaseMapper<User> {
    // 第一种
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 3)
    @Select("SELECT * FROM user")
    List<User> queryDepartmentAll();

    // 第二种 在Mybatis-3.4.0版本中，不支持@select注解，在3.4.1版本中已经修复：
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 3)
    @Select("SELECT * FROM user")
    Cursor<User> cursorQueryDepartmentAll();

}