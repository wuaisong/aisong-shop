package wu.ai.song.api.timeout;

/**
 * 延迟队列执行器
 * @author 75318070
 */
public interface RedisDelayQueueHandle<T> {
    /**
     * 延迟执行
     *
     * @param t
     */
    void execute(T t);
}
