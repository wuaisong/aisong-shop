package aisong.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HJ212MsgUtils {

    /**
     * 传入HJ212数据，返回简单json数据，没有对污染源进行分类汇总。
     *
     * @param msg hj212
     * @return json
     */
    public static JSONObject dealMsg1(String msg) {
        JSONObject data = new JSONObject();
        try {
            // 拆分消息
            String[] subMsg = msg.split("&&");
            // 清洗消息头基本数据
            String headStr = subMsg[0].substring(2).replace(";CP=", "").replace("=", "\":\"").replace(",", "\",\"").replace(";", "\",\"");
            data.put("SIZE", headStr.substring(0, 4));
            data.putAll(JSONObject.parseObject("{\"" + headStr.substring(4) + "\"}"));
            // 清洗数据体基本数据
            String paramStr = subMsg[1].replace("=", "\":\"").replace(",", "\",\"").replace(";", "\",\"");
            data.put("CP", JSONObject.parseObject("{\"" + paramStr + "\"}"));
            // 保存消息尾数据，主要是CRC校验和包结束符
            data.put("End", subMsg[2]);
        } catch (Exception e) {
            log.error("HJ212数据转JSON错误。报错信息：{}，消息内容：{}", e.getMessage(), msg);
        }
        return data;
    }


    /**
     * 传入HJ212数据，返回复杂json数据，对污染源进行分类汇总，不同污染源用不同的json key。
     *
     * @param msg hj212
     * @return json
     */
    public static JSONObject dealMsg2(String msg) {
        JSONObject data = new JSONObject();
        try {
            // 拆分消息
            String[] subMsg = msg.split("&&");
            // 清洗消息头基本数据
            String headStr = subMsg[0].substring(2).replace(";CP=", "").replace("=", "\":\"").replace(",", "\",\"").replace(";", "\",\"");
            data.put("SIZE", headStr.substring(0, 4));
            data.putAll(JSONObject.parseObject("{\"" + headStr.substring(4) + "\"}"));
            // 清洗数据体基本数据
            String[] monitors = subMsg[1].split(";");
            JSONObject cp = new JSONObject();
            for (String obj : monitors) {
                String paramStr = obj.replace("=", "\":\"").replace(",", "\",\"").replace(";", "\",\"");
                // 如果是时间信息，则直接放到外层
                if (paramStr.contains("DataTime")) {
                    data.putAll(JSONObject.parseObject("{\"" + paramStr + "\"}"));
                } else {
                    String[] ele = getPollutionSource(paramStr);
                    cp.put(ele[0], JSONObject.parseObject("{\"" + ele[1] + "\"}"));
                }
            }
            data.put("CP", cp);
            // 保存消息尾数据，主要是CRC校验和包结束符
            data.put("End", subMsg[2]);
        } catch (Exception e) {
            log.error("HJ212数据转JSON错误。报错信息：{}，消息内容：{}", e.getMessage(), msg);
        }
        return data;
    }


    /**
     * 解析污染源数据，获取污染源编号
     */
    private static String[] getPollutionSource(String data) {
        String key = data.substring(0, data.indexOf("-"));
        data = data.replaceAll(key + "-", "");
        String[] result = new String[2];
        result[0] = key;
        result[1] = data;
        return result;
    }


    public static void main(String[] args) {
        System.out.println(dealMsg1("##0746ST=31;CN=2061;PW=123456;MN=7568770259402;Flag=0;CP=&&DataTime=20221008100000;B02-Min=1.6960,B02-Avg=3.0586,B02-Max=3.7704,B02-Cou=11010.8437;S01-Min=17.7469,S01-Avg=19.4636,S01-Max=19.6944;S02-Min=3.2459,S02-Avg=5.6705,S02-Max=6.9578;S03-Min=30.0434,S03-Avg=30.2675,S03-Max=30.4503;S08-Min=-0.4643,S08-Avg=-0.3541,S08-Max=0.0000;S05-Min=6.1814,S05-Avg=6.8655,S05-Max=7.0097;a24088-Min=2.4957,a24088-Avg=3.2176,a24088-Max=4.7744,a24088-Cou=0.0354;25-Min=6.4292,25-Avg=12.1457,25-Max=20.0606,25-Cou=0.1336;a05002-Min=2.7715,a05002-Avg=7.8561,a05002-Max=14.7934,a05002-Cou=0.0863;17-Min=0.0000,17-Avg=0.0000,17-Max=0.0000,17-Cou=0.0000;18-Min=0.0000,18-Avg=0.0000,18-Max=0.0000,18-Cou=0.0000;16-Min=0.0000,16-Avg=0.0000,16-Max=0.0000,16-Cou=0.0000&&7BC0"));
        System.out.println(dealMsg2("##0746ST=31;CN=2061;PW=123456;MN=7568770259402;Flag=0;CP=&&DataTime=20221008100000;B02-Min=1.6960,B02-Avg=3.0586,B02-Max=3.7704,B02-Cou=11010.8437;S01-Min=17.7469,S01-Avg=19.4636,S01-Max=19.6944;S02-Min=3.2459,S02-Avg=5.6705,S02-Max=6.9578;S03-Min=30.0434,S03-Avg=30.2675,S03-Max=30.4503;S08-Min=-0.4643,S08-Avg=-0.3541,S08-Max=0.0000;S05-Min=6.1814,S05-Avg=6.8655,S05-Max=7.0097;a24088-Min=2.4957,a24088-Avg=3.2176,a24088-Max=4.7744,a24088-Cou=0.0354;25-Min=6.4292,25-Avg=12.1457,25-Max=20.0606,25-Cou=0.1336;a05002-Min=2.7715,a05002-Avg=7.8561,a05002-Max=14.7934,a05002-Cou=0.0863;17-Min=0.0000,17-Avg=0.0000,17-Max=0.0000,17-Cou=0.0000;18-Min=0.0000,18-Avg=0.0000,18-Max=0.0000,18-Cou=0.0000;16-Min=0.0000,16-Avg=0.0000,16-Max=0.0000,16-Cou=0.0000&&7BC0"));
    }

}

