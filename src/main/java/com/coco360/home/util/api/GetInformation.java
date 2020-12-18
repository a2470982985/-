package com.coco360.home.util.api;

import com.alibaba.fastjson.JSONObject;
import com.coco360.home.util.HttpClientUtil;
import com.coco360.home.web.servlet.BaseServlet;
import com.coco360.home.web.servlet.UserServlet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetInformation {
    static String html = null;


    /**
     * Robot打开微信 并点击微信事件
     * 更新Openid  防止openid过期
     *
     * @return
     */
    public static void updateOpenid() throws InterruptedException {
        Robot robot = null;
        String s = filterCommandLineSpecialChar("C:/Program Files (x86)/Tencent/WeChat/WeChat.exe");
        try {
            Runtime.getRuntime().exec("C:/Windows/System32/cmd.exe /k start " + s); // 通过cmd窗口执行命令
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                BaseServlet.sendEmail("2470982985@qq.com", "Server error", "Server error\n" + e.getStackTrace().toString());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        Thread.sleep(1000);
        int i = 10;
        while (i-- > 0) {
            robot.mouseMove(1250, 190);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(1726, 207);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    public static void updateOpenid(String str) throws InterruptedException {
        Robot robot = null;
        String s = filterCommandLineSpecialChar(str);
        try {
            Runtime.getRuntime().exec("C:/Windows/System32/cmd.exe /k start " + s); // 通过cmd窗口执行命令
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                BaseServlet.sendEmail("2470982985@qq.com", "Server error", "Server error\n" + e.getStackTrace().toString());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        Thread.sleep(1000);
        int i = 10;
        while (i-- > 0) {
            robot.mouseMove(1250, 190);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(1726, 207);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }


    /**
     * 传入url 获取openid
     *
     * @param url
     * @return
     */
    public static String getOpenid(String url) {
        String openid = getParam(url, "openid");
        return openid;

    }

    /**
     * 获取GET请求内的键值对
     *
     * @param url
     * @param name
     * @return
     */
    public static String getParam(String url, String name) {
        Map<String, Object> map = new HashMap<>();
        String params = url.substring(url.indexOf("?") + 1, url.length());
        //http://www.hntyxxh.com/wechat2-ssr/?openid=5ade261906b80d8715c8cb6585ac3327&a=b&from=z
        String[] split = params.split("&");
        for (int i = 0; i < split.length; i++) {
            String[] key_Value = split[i].split("=");
            if (key_Value.length == 2) {
                map.put(key_Value[0], key_Value[1]);
            }
        }

        return map.get(name).toString();
    }

    /**
     * 获取name
     *
     * @param openid
     * @return name
     * @throws IOException
     */
    public static String getName(String openid) throws IOException {
//        String html=null;
        try {
            html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v2/students", null, openid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (html == null || !html.contains("item_value")) {
            return null;
        }
//        JSONObject jsonObject = JSONObject.parseObject(html);
        Object obj = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(0).toString()).get(2)).toString()).get("item_value");
        return obj.toString();
    }

    /**
     * 获取Email地址
     *
     * @param openid
     * @return email
     * @throws IOException
     */
    public static String getEmail(String openid) throws IOException {
//        String html=null;
        try {
            html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v2/students", null, openid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (html == null || !html.contains("item_value")) {
            return null;
        }
        Object obj = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(1).toString()).get(0)).toString()).get("item_value");
        return obj.toString();
    }

    /**
     * 获取头像
     *
     * @param openid
     * @return 头像url
     * @throws IOException
     */
    public static String getAvatar(String openid) throws IOException {
//        String html=null;
        try {
            html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v2/students", null, openid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (html == null || !html.contains("item_value")) {
            return null;
        }
        Object obj = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(0).toString()).get(0)).toString()).get("item_value");
        return obj.toString();
    }

    /**
     * 获取name和Email
     *
     * @param openid
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getNameAndEmail(String openid) throws Exception {
//        String html=null;
        Map<String, Object> map = new HashMap<>();

        html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v2/students", null, openid);

        if (!html.contains("item_value")) {
            return null;
        }
        Object email = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(1).toString()).get(0)).toString()).get("item_value");
        Object name = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(0).toString()).get(2)).toString()).get("item_value");
        map.put("name", name);
        map.put("email", email);

        return map;
    }

    /**
     * 获取name和Email和Avatar
     *
     * @param openid
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getNameAndEmailAndAvatar(String openid) throws IOException {
//        String html=null;
        Map<String, Object> map = new HashMap<>();
        try {
            html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v2/students", null, openid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (html == null || !html.contains("item_value")) {
            return null;
        }
        Object email = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(1).toString()).get(0)).toString()).get("item_value");
        Object name = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(0).toString()).get(2)).toString()).get("item_value");
        Object avatar = JSONObject.parseObject((JSONObject.parseArray(JSONObject.parseArray(html).get(0).toString()).get(0)).toString()).get("item_value");

        map.put("name", name);
        map.put("email", email);
        map.put("avatar", avatar);

        return map;
    }


    /**
     * 置换传入的路径内的空格
     *
     * @param s
     * @return
     */
    public static String filterCommandLineSpecialChar(String s) {
        String regexp = "[\\pP\\pZ\\pS]+";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String p = matcher.group();
            matcher.appendReplacement(sb, "\"" + p + "\"");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
