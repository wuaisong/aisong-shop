package wu.ai.song.api.entity;

import lombok.Data;

import java.util.List;

/**
 * @author 75318070
 */
@Data
public class SysUser {
    private Long id;
    private String loginName;
    private String userName;
    private String email;
    private List<Long> roleIds;
    private List<SysRole> roles;
}
