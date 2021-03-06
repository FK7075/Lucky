package com.lucky.web.httpclient.callcontroller;

import com.lucky.utils.base.Assert;
import com.lucky.utils.config.ConfigUtils;
import com.lucky.utils.config.YamlConfAnalysis;
import com.lucky.utils.conversion.$Expression;
import com.lucky.utils.conversion.ExpressionParsingException;
import com.lucky.utils.jexl.JexlEngineUtil;
import com.lucky.web.conf.WebConfig;

import java.util.Map;


public class Api {

    private static final Map<String,String> callApi= WebConfig.getWebConfig().getCallApi();
    private static final YamlConfAnalysis yaml= ConfigUtils.getYamlConfAnalysis();
    /**
     * 将注解中配置的CallApi转化为实际的地址
     * @param annApiStr
     * @return
     */
    public static String getApi(String annApiStr){
        if(JexlEngineUtil.isExpression(annApiStr)){
            String api;
            try {
                api=$Expression.translation(annApiStr,callApi);
                return api;
            }catch (ExpressionParsingException e){
                Object apiObj = yaml.getObject(annApiStr);
                Assert.notNull(apiObj,annApiStr+" => null");
                return apiObj.toString();
            }
        }
        return annApiStr;
    }
}
