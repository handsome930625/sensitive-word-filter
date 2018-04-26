package com.chaser.utils;

/**
 * description:字符转换器
 *
 * @author wangyj on 2018/4/25
 */
public class Converter {
    /**
     * ASCII表中可见字符从!开始，偏移位值为33(Decimal)
     */
    private static final char DBC_CHAR_START = 33;

    /**
     * ASCII表中可见字符到~结束，偏移位值为126(Decimal)
     */
    private static final char DBC_CHAR_END = 126;

    /**
     * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
     */
    private static final char SBC_CHAR_START = 65281;

    /**
     * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
     */
    private static final char SBC_CHAR_END = 65374;

    /**
     * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
     */
    private static final int CONVERT_STEP = 65248;

    /**
     * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
     */
    private static final char SBC_SPACE = 12288;

    /**
     * 半角空格的值，在ASCII中为32(Decimal)
     */
    private static final char DBC_SPACE = ' ';

    /**
     * <p>功能描述: 半角字符->全角字符转换,只处理空格，!到˜之间的字符，忽略其他</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/25 15:59 </p>
     *
     * @param str 待转换的字符串
     * @return 转换后的字符串
     */
    public static String full2half(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str.length());
        char[] chars = str.toCharArray();
        for (char ch : chars) {
            if (ch == DBC_SPACE) {
                // 如果是半角空格，直接用全角空格替代
                sb.append(SBC_SPACE);
            } else if ((ch >= DBC_CHAR_START) && (ch <= DBC_CHAR_END)) {
                // 字符是!到~之间的可见字符
                sb.append((char) (ch + CONVERT_STEP));
            } else { // 不对空格以及ascii表中其他可见字符之外的字符做任何处理
                sb.append(ch);
            }
        }
        return sb.toString();
    }


    /**
     * <p>功能描述: 半角转换全角</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/25 16:05 </p>
     *
     * @param ch 待转换的字符
     * @return 转换后的字符
     */
    public static int half2full(char ch) {
        int r = ch;
        if (ch == DBC_SPACE) {
            // 如果是半角空格，直接用全角空格替代
            r = SBC_SPACE;
        } else if ((ch >= DBC_CHAR_START) && (ch <= DBC_CHAR_END)) {
            // 字符是!到~之间的可见字符
            r = ch + CONVERT_STEP;
        }
        return r;
    }

    /**
     * <p>功能描述: 全角字符->半角字符转换,只处理全角的空格，全角！到全角～之间的字符，忽略其他</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/25 15:59 </p>
     *
     * @param str 待转换的字符串
     * @return 转换后的字符串
     */
    public static String full2char(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        char[] chars = str.toCharArray();
        for (char ch : chars) {
            if (ch >= SBC_CHAR_START && ch <= SBC_CHAR_END) {
                // 如果位于全角！到全角～区间内
                sb.append((char) (ch - CONVERT_STEP));
            } else if (ch == SBC_SPACE) {
                // 如果是全角空格
                sb.append(DBC_SPACE);
            } else {
                // 不处理全角空格，全角！到全角～区间外的字符
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * <p>功能描述: 全角转换半角</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/25 16:05 </p>
     *
     * @param ch 待转换的字符
     * @return 转换后的字符
     */
    public static int full2half(char ch) {
        int r = ch;
        // 如果位于全角！到全角～区间内
        if (ch >= SBC_CHAR_START && ch <= SBC_CHAR_END) {
            r = ch - CONVERT_STEP;
        } else if (ch == SBC_SPACE) {
            // 如果是全角空格
            r = DBC_SPACE;
        }
        return r;
    }

    public static int up2lowAndFull2half(char ch) {
        int r = full2half(ch);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }
}
