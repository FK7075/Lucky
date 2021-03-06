package com.lucky.jacklamb.mapper.scan;

import com.lucky.jacklamb.mapper.xml.MapperXMLParsing;
import com.lucky.utils.config.ConfigUtils;
import com.lucky.utils.config.YamlConfAnalysis;
import com.lucky.utils.fileload.Resource;
import com.lucky.utils.fileload.resourceimpl.PathMatchingResourcePatternResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author fk
 * @version 1.0
 * @date 2020/12/18 0018 18:13
 */
public class MapperXmlScan {

    private final static Logger log= LoggerFactory.getLogger(MapperXmlScan.class);

    private static String mapperXmlRoot="classpath:mapper/*.xml";
    private static final YamlConfAnalysis yaml=ConfigUtils.getYamlConfAnalysis();
    private static final PathMatchingResourcePatternResolver patternResolver=new PathMatchingResourcePatternResolver();

    static {
        if(yaml!=null){
            Map<String, Object> map = yaml.getMap();
            Object jacklambObj = map.get("jacklamb");
            if(jacklambObj instanceof Map){
                Map<String, Object> jacklambMap = (Map<String, Object>) jacklambObj;
                Object mapperLocationObj = yaml.getObject(jacklambMap.get("mapper-locations"));
                if(mapperLocationObj instanceof String){
                    mapperXmlRoot=mapperLocationObj.toString();
                }
            }
        }
    }

    public static Map<String,Map<String,String>> getAllMapperSql(){
        Set<MapperXMLParsing> mapperXmlSet=getAllMapperLocations();
        Map<String,Map<String,String>> mapperSqls=new HashMap<>();
        for (MapperXMLParsing mapXmlp : mapperXmlSet) {
            Map<String, Map<String, String>> sqlMap = mapXmlp.getXmlMap();
            for(Map.Entry<String,Map<String,String>> en:sqlMap.entrySet()){
                String key = en.getKey();
                Map<String, String> map = en.getValue();
                if(mapperSqls.containsKey(key)){
                    Map<String, String> contextMap = mapperSqls.get(key);
                    for(Map.Entry<String,String> e:map.entrySet()){
                        if(contextMap.containsKey(e.getKey())){
                            throw new RuntimeException("同一个Mapper接口方法的SQL配置出现在了两个xml配置文件中！ "+key+"."+e.getKey()+"(XXX)");
                        }else{
                            contextMap.put(e.getKey(),e.getValue());
                        }
                    }
                }else{
                    mapperSqls.put(key,map);
                }
            }
        }
        return mapperSqls;
    }
    private static Set<MapperXMLParsing> getAllMapperLocations(){
        Set<MapperXMLParsing> mapperLocations=new HashSet<>();
        try {
            Resource[] resources = patternResolver.getResources(mapperXmlRoot);
            for (Resource resource : resources) {
                mapperLocations.add(new MapperXMLParsing(resource.getInputStream()));
            }
        } catch (IOException ignored) {
            log.warn("[{}] is not found ！",mapperXmlRoot);
        }
        return mapperLocations;
    }
}
