package com.lucky.web.interceptor;

import com.lucky.web.core.Model;
import com.lucky.web.mapping.UrlMapping;

/**
 * 拦截器
 * @author fk7075
 * @version 1.0.0
 * @date 2021/1/3 上午3:33
 */
public interface HandlerInterceptor {

    /**
     * 该方法会在控制器方法前执行，其返回值表示是否中断后续操作。当其返回值为true时，表示继续向下执行；
     * 当其返回值为false时，会中断后续的所有操作（包括调用下一个拦截器和控制器类中的方法执行等）
     * @param model Model对象
     * @param handler 处理当前请求的UrlMapping对象
     * @return
     * @throws Exception
     */
    default boolean preHandle(Model model, final UrlMapping handler)
            throws Exception {
        return true;
    }

    /**
     * 该方法会在控制器方法调用之后，且解析视图之前执行。可以通过此方法对请求域中的模型和视图做出进一步的修改。
     * @param model Model对象
     * @param handler 处理当前请求的UrlMapping对象
     * @param result 原UrlMapping的执行结果
     * @return 这个方法的返回值会被直接响应给客户端，一般情况该应该将result直接返回，除非你想对预期的结果进行修改
     * @throws Exception
     */
    default Object postHandle(Model model, final UrlMapping handler,Object result) throws Exception {
        return result;
    }

    /**
     * 该方法会在整个请求完成，即视图渲染结束之后执行。可以通过此方法实现一些资源清理、记录日志信息等工作。
     * 注意：拦截器不能处理被异常处理器标记要处理的异常，如果你想只让本拦截器处理异常，并且让其他拦截器忽略此异常
     * 可以使用{@link Model#setExceptionIsHandling(boolean)}方法
     * @param model Model对象
     * @param handler 处理当前请求的UrlMapping对象
     * @param ex 如果没有出现异常，此参数为NULL
     * @throws Exception
     */
    default void afterCompletion(Model model, final UrlMapping handler, Throwable ex) throws Exception {
    }
}
