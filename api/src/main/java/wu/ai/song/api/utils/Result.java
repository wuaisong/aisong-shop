package wu.ai.song.api.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 * 首选加入lombok的注解
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Long code;
    private String message;
    private Object obj;

    /**
     * 成功返回结果
     * 静态方法使用static
     */
    public static Result success(String message){
        return new Result(200L,message,null);
    }
    /**
     * 成功返回结果
     * 静态方法使用static
     */
    public static Result success(String message,Object obj){
        return new Result(200L,message,obj);
    }

    /**
     * 失败返回
     */
    public static Result error(String message){
        return new Result(500L,message,null);
    }

    /**
     * 失败返回
     */
    public static Result error(String message,Object obj){
        return new Result(500L,message,obj);
    }
}
