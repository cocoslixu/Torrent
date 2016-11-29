package com.xlj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.pepper.sdk.base.utils.MD5Utils;

public class Tools
{   
    public static final String PAYKEY="";
    /**
     * <pre>
     * 将Map中参数组装成排序的参数串(格式：key1=value1&key2=value2&key3=value3...)
     * </pre>
     * @param paramMap
     * @return
     */
    public static String toSortParamStr(Map<String, String> paramMap,String paykey)
    {
        StringBuffer sb = new StringBuffer();
        // 按照key做排序
        List<String> keyList = new ArrayList<String>(paramMap.keySet());
        Collections.sort(keyList);
        for (int i = 0; i < keyList.size(); i++)
        {
            String key = keyList.get(i);
            String value = paramMap.get(key);
            if (null != value)
            {
                sb.append(key + "=" + value);   
            }
            else
            {
                sb.append(key + "=");
            }
            
            if (i < keyList.size() - 1)
            {
                sb.append("&");
            }
        }
        return MD5Utils.encodeString(sb.append("&").append(paykey).toString(), "UTF-8");
    }
}
