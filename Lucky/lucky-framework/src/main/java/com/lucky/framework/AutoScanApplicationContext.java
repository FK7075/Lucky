package com.lucky.framework;

import com.lucky.framework.container.Module;
import com.lucky.framework.container.RegisterMachine;
import com.lucky.framework.container.SingletonContainer;
import com.lucky.framework.scan.Scan;
import com.lucky.framework.scan.ScanFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fk7075
 * @version 1.0
 * @date 2020/11/16 9:01
 */
public class AutoScanApplicationContext implements ApplicationContext{

    private static AutoScanApplicationContext autoScanApplicationContext;
    public Class<?> applicationBootClass;
    public SingletonContainer singletonPool;

    public static AutoScanApplicationContext create(){
        if(autoScanApplicationContext==null){
            autoScanApplicationContext=new AutoScanApplicationContext();
        }
        return autoScanApplicationContext;
    }

    public static AutoScanApplicationContext create(Class<?> applicationBootClass){
        if(autoScanApplicationContext==null){
            autoScanApplicationContext=new AutoScanApplicationContext(applicationBootClass);
        }
        return autoScanApplicationContext;
    }

    private AutoScanApplicationContext(){
        init();
    }

    private AutoScanApplicationContext(Class<?> applicationBootClass) {
        this.applicationBootClass = applicationBootClass;
        init();
    }

    private void init(){
        Scan scan= ScanFactory.createScan(applicationBootClass);
        RegisterMachine registerMachine=RegisterMachine.getRegisterMachine();
        registerMachine.setScan(scan);
        registerMachine.init();
        singletonPool=registerMachine.getSingletonPool();
    }

    @Override
    public Object getBean(String id) {
        return singletonPool.getBean(id).getComponent();
    }

    @Override
    public Module getModule(String id) {
        return singletonPool.getBean(id);
    }

    @Override
    public <T> List<T> getBean(Class<T>...aClasses) {
        List<Module> modules = singletonPool.getBeanByClass(aClasses);
        List<T> list=new ArrayList<>(modules.size());
        modules.stream().forEach(m->list.add((T)m.getComponent()));
        return list;
    }

    @Override
    public List<Module> getModule(Class<?>...aClasses) {
        return singletonPool.getBeanByClass(aClasses);
    }

    @Override
    public List<Object> getBeanByAnnotation(Class<? extends Annotation>...annotationClasses) {
        return singletonPool.getBeanByAnnotation(annotationClasses)
                .stream()
                .map(m->m.getComponent())
                .collect(Collectors.toList());
    }

    @Override
    public List<Module> getModuleByAnnotation(Class<? extends Annotation>...annotationClasses){
        return singletonPool.getBeanByAnnotation(annotationClasses);
    }

    @Override
    public List<Object> getBeans() {
        return singletonPool.values()
                .stream()
                .map(m->m.getComponent())
                .collect(Collectors.toList());
    }

    public SingletonContainer getNewSingletonPool(String...jarFilePath) throws IOException {
        return RegisterMachine.dynamicAssembly(Scan.scan(jarFilePath));
    }

    @Override
    public boolean isIOCType(String type) {
        return singletonPool.containsType(type);
    }

    @Override
    public boolean isIOCId(String id) {
        return singletonPool.containsKey(id);
    }

    @Override
    public boolean isIOCClass(Class<?> componentClass) {
        return singletonPool.containsClass(componentClass);
    }
}
