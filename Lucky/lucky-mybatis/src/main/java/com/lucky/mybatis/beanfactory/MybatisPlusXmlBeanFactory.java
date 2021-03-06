package com.lucky.mybatis.beanfactory;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.lucky.framework.annotation.Component;
import com.lucky.framework.container.Module;
import com.lucky.framework.container.factory.IOCBeanFactory;
import com.lucky.framework.container.factory.Namer;
import com.lucky.mybatis.annotation.Mapper;
import com.lucky.mybatis.proxy.SqlSessionTemplate;
import com.lucky.utils.base.Assert;
import com.lucky.utils.file.Resources;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.BufferedReader;
import java.util.List;

/**
 * @author fk
 * @version 1.0
 * @date 2021/3/2 0002 15:44
 */
@Component
public class MybatisPlusXmlBeanFactory extends BaseMybatisFactory {

    @Override
    public List<Module> createBean() {
        SqlSessionFactory sqlSessionFactory
                =new MybatisSqlSessionFactoryBuilder().build(Resources.getReader(DEFAULT_XML_CONF));
        return getMappers(sqlSessionFactory);
    }

}
