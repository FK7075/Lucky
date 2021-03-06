package com.lucky.utils.proxy;

import com.lucky.utils.reflect.ClassUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Proxy;

public abstract class CglibProxy {

    private static final String PROXY_NAME="$$ByLuckyCGLIB$$";

    public static <T> T getCglibProxyObject(Class<T> clazz, MethodInterceptor methodInterceptor){
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
//        enhancer.setInterfaces(clazz.getInterfaces());
        enhancer.setNamingPolicy(new LuckyNamingPolicy());
        enhancer.setCallback(methodInterceptor);
        return (T) enhancer.create();
    }

    public static Object getCglibProxyObject(LuckyMethodInterceptor luckyMethodInterceptor){
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(luckyMethodInterceptor.getSuperClass());
//        enhancer.setInterfaces(luckyMethodInterceptor.getInterfaces());
        enhancer.setNamingPolicy(new LuckyNamingPolicy());
        enhancer.setCallback(luckyMethodInterceptor);
        return enhancer.create();
    }

    public static boolean isAgent(Class<?> aClass){
        return aClass.getName().contains(PROXY_NAME);
    }

    public static Class<?> getOriginalType(Class<?> aClass){
        if(isAgent(aClass)){
            String name = aClass.getName();
            return ClassUtils.getClass(name.substring(0,name.indexOf(PROXY_NAME)));
        }
        return aClass;
    }
}



