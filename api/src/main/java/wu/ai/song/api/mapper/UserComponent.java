package wu.ai.song.api.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import wu.ai.song.api.entity.User;

/**
 * @author wuyaming 2022/6/11.
 */
@Component
public class UserComponent extends ServiceImpl<UserDao, User> {
}
