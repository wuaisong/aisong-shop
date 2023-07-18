package wu.ai.song.api.mapper;

import org.springframework.stereotype.Repository;
import wu.ai.song.api.entity.SysUser;

import java.util.List;

@Repository
public interface UserMapper {
    List<SysUser> findUsers(int status);
}
