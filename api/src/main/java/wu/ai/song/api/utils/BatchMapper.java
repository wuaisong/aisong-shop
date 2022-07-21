package wu.ai.song.api.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

/**
 * @author wuyaming
 */
@Mapper
public interface BatchMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入（mysql）
     *
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(Collection<T> entityList);

}
