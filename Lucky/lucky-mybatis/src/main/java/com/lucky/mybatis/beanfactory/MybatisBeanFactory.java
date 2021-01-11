package com.lucky.mybatis.beanfactory;

import com.lucky.datasource.sql.LuckyDataSource;
import com.lucky.datasource.sql.LuckyDataSourceManage;
import com.lucky.framework.container.Module;
import com.lucky.framework.container.factory.IOCBeanFactory;
import com.lucky.framework.container.factory.Namer;
import com.lucky.mybatis.annotation.Mapper;
import com.lucky.mybatis.conf.MybatisConfig;
import com.lucky.utils.base.Assert;
import com.lucky.utils.file.Resources;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fk
 * @version 1.0
 * @date 2021/1/7 0007 15:45
 */
public class MybatisBeanFactory extends IOCBeanFactory {

    private final static String TYPE="mybatis-mapper";
    private final static MybatisConfig mybatisConfig=MybatisConfig.getMybatisConfig();
    public MybatisBeanFactory(){
    }

    @Override
    public List<Module> createBean() {
        List<Module> mappers = super.createBean();
        //注册数据源
        getBeanByClass(LuckyDataSource.class)
                .stream()
                .map(m->(LuckyDataSource)m.getComponent())
                .forEach(LuckyDataSourceManage::addLuckyDataSource);


        List<LuckyDataSource> allDataSource = LuckyDataSourceManage.getAllDataSource();
        Map<String, List<Class<?>>> mapperClassesMap = dbnameGroup();
        for (LuckyDataSource luckyDataSource : allDataSource) {
            String dbname = luckyDataSource.getDbname();
            Configuration configuration=new Configuration();
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, luckyDataSource.createDataSource());
            configuration.setEnvironment(environment);
            if(mybatisConfig.getTypeAliasesPackage()!=null){
                configuration.getTypeAliasRegistry().registerAliases(mybatisConfig.getTypeAliasesPackage());
            }
            List<Class<?>> mapperClasses = mapperClassesMap.get(dbname);
            String mapperPackage = mybatisConfig.getMapperLocations();
            if(mapperClasses!=null){
                for (Class<?> mapperClass : mapperClasses) {
                    String xmlPath=mapperPackage+mapperClass.getSimpleName()+".xml";
                    InputStream inputStream = Resources.getInputStream(xmlPath);
                    if(inputStream!=null){
                        new XMLMapperBuilder(inputStream,configuration,mapperClass.getName(),configuration.getSqlFragments()).parse();
                    }else{
                        configuration.addMapper(mapperClass);
                    }
                    SqlSessionFactory sessionFactory
                            = new SqlSessionFactoryBuilder().build(configuration);
                    mappers.add(new Module(getBeanId(mapperClass),TYPE,sessionFactory.openSession().getMapper(mapperClass)));
                }
            }
        }
        return mappers;
    }

    private Map<String, List<Class<?>>> dbnameGroup(){
        return getPluginByAnnotation(Mapper.class).stream()
                .collect(Collectors.groupingBy(c->c.getAnnotation(Mapper.class).dbname()));
    }

    private String getBeanId(Class<?> mapperClass){
        String id = mapperClass.getAnnotation(Mapper.class).id();
        return Assert.isBlankString(id)? Namer.getBeanName(mapperClass):id;
    }
}
