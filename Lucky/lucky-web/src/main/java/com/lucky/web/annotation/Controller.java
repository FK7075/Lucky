package com.lucky.web.annotation;

import com.lucky.framework.annotation.Component;
import com.lucky.framework.annotation.Plugin;
import com.lucky.web.enums.Rest;

import java.lang.annotation.*;

/**
 * 在MVC中此用于标识一个Controller组件
 * 	value：单独使用此注解是用来定义一个IOC组件
 * 	prefix：MVC中的视图定位的前缀(eg: /WEB_INF/jsp/)
 * 	suffix:MVC中的视图定位的后缀(eg: .jsp)
 * -------------------------------------------
 * 	使用"return String"的方式设置转发或重定向的目的地(返回值为String的方法)
 * 	1.转发到页面：无前缀 return page
 * 	2.转发到Controller方法:return forward:method
 *	3.重定向到页面：return page:pageing
 *	4.重定向到Controller方法：return redirect:method
 * @author fk-7075
 *
 */

/**
 * 定义一个Controller组价
 * @author fk-7075
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Plugin
@Component(type = "controller")
public @interface Controller {
	
	/**
	 * 指定一个URL映射
	 * @return
	 */
	String value() default "/";

	/**
	 * 为该Controller组件指定一个唯一ID，默认会使用[首字母小写类名]作为组件的唯一ID
	 * @return
	 */
	String id() default "";

	/**
	 *
	 * @return
	 */
	String callapi() default "";
	
	/**
	 * 指定一些合法访问的ip地址，来自其他ip地址的请求将会被拒绝
	 * @return
	 */
	String[] ip() default {};
	
	/**
	 * 指定一些合法访问的ip段，来自其他ip地址的请求将会被拒绝
	 * @return
	 */
	String[] ipSection() default {};
	
	/**
	 * 视图定位的前缀(eg: /WEB_INF/jsp/),只有在rest=Rest.NO时发挥作用
	 * @return
	 */
	String prefix() default "";
	
	/**
	 * 视图定位的后缀(eg: .jsp),只有在rest=Rest.NO时发挥作用
	 * @return
	 */
	String suffix() default "";
}
