package wu.ai.song.api.utils;

import java.math.BigDecimal;

/**
 * 提供精确的浮点数运算工具类
 * @author clove
 *
 */
public class DoubleUtil {

    // 除法运算默认精度,下面自定义方法可修改
    private static final int DEF_DIV_SCALE = 10;

    private DoubleUtil() {

    }

    /**
     * 精确加法
     */
    public static double add(double firValue, double sedValue) {
        BigDecimal firBD = BigDecimal.valueOf(firValue);
        BigDecimal sedBD = BigDecimal.valueOf(sedValue);
        return firBD.add(sedBD).doubleValue();
    }

    /**
     * 精确减法
     */
    public static double sub(double firValue, double sedValue) {
        BigDecimal firBD = BigDecimal.valueOf(firValue);
        BigDecimal sedBD = BigDecimal.valueOf(sedValue);
        return firBD.subtract(sedBD).doubleValue();
    }

    /**
     * 精确乘法
     */
    public static double mul(double firValue, double sedValue) {
        BigDecimal firBD = BigDecimal.valueOf(firValue);
        BigDecimal sedBD = BigDecimal.valueOf(sedValue);
        return firBD.multiply(sedBD).doubleValue();
    }

    /**
     * 精确除法 使用默认精度
     */
    public static double div(double firValue, double sedValue) {
        double num = 0;
        try {
            num = div(firValue, sedValue, DEF_DIV_SCALE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 精确除法
     * 
     * @param scale 精度
     * @throws IllegalAccessException
     */
    public static double div(double firValue, double sedValue, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(firValue);
        BigDecimal b2 = BigDecimal.valueOf(sedValue);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     * 
     * @param scale 小数点后保留几位
     */
    public static double round(double value, int scale) {
        double num = 0;
        try {
            num = div(value, 1, scale);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 比较两数是否相等
     */
    public static boolean equalTo(BigDecimal firBD, BigDecimal sedBD) {
        if (firBD == null || sedBD == null) {
            return false;
        }
        return 0 == firBD.compareTo(sedBD);
    }
}