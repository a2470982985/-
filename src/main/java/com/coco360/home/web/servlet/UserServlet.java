package com.coco360.home.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.coco360.home.domain.RespMsg;
import com.coco360.home.util.HttpClientUtil;
import com.coco360.home.util.api.GetInformation;
import com.coco360.home.util.api.UserSign;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    static String html = null;

    public static void tyqd(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException, MessagingException {
        String url = request.getParameter("url");
        if (url == null || url == "") {
            RespMsg reg = new RespMsg(0, "error", "请输入openid");
            writeValue(reg, response);
            return;
        }
        String openid = GetInformation.getOpenid(url);
        String str = null;
        try {
            str = UserSign.checkSign(openid);
        } catch (Exception e) {
//            e.printStackTrace();
            RespMsg reg = new RespMsg(0, "error", "河南统院服务器拥挤，暂未连接成功");
            writeValue(reg, response);
            return;
        }

        if (str == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误");
            writeValue(reg, response);
            return;
        } else if ("{}".contains(str)) {
            RespMsg reg = new RespMsg(1, "success", "暂无开启的签到");
            writeValue(reg, response);
            return;
        }
        String s = null;
        try {
            s = UserSign.Sign_in(str, openid);
        } catch (Exception e) {
//            e.printStackTrace();
            RespMsg reg = new RespMsg(0, "error", "河南统院服务器拥挤，暂未连接成功");
            writeValue(reg, response);
            return;
        }
        System.out.println(s);

        if (s == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误");
            writeValue(reg, response);
            return;
        } else if (s.contains("errorCode")) {
            RespMsg reg = new RespMsg(0, "error", s);
            writeValue(reg, response);
            return;
        }
        RespMsg reg = new RespMsg(1, "success", s);
        writeValue(reg, response);
//        sendEmail(email.toString(), "河南统院签到系统", "尊敬的用户" + name + "，您好:\n\n服务器在 " + getDate() + " 已成功签到\n\n返回信息为:" + s + "\n\n\n\t\t河南信息统计学院签到系统\n\t\tdesigned by：Flik");
        sendEmail("2470982985@qq.com", "河南统院签到系统", "尊敬的用户" + "李菲" + "，您好:\n\n服务器在 " + getDate() + " 已成功签到\n\n返回信息为:" + s + "\n\n\n\t\t河南信息统计学院签到系统\n\t\tdesigned by：Flik");
        sendWechatMessage("AT_4eG9dAg4tIY6eIcHyJpIWqRLKEEq5gJs", "UID_VNhOsjrNgKeINAxQFY8pf6WFy7JV", s, "http://www.hntyxxh.com/wechat2-ssr/?openid=" + openid + "&from=wzj");
        System.out.println("[" + getDate() + "]" + "\t用户:" /*+ name*/ + "发起签到,返回值为：" + s);
    }

    public static void checkCourseware(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException, MessagingException {
        String url = request.getParameter("url");
        if (url == null || url == "") {
            RespMsg reg = new RespMsg(0, "error", "请输入openid");
            writeValue(reg, response);
            return;
        }
        String openid = GetInformation.getOpenid(url);
//        System.out.println(openid);
        Map<String, Object> map = null;
        try {
            map = GetInformation.getNameAndEmail(openid);
        } catch (Exception e) {
            RespMsg reg = new RespMsg(0, "error", "河南统院服务器拥挤，暂未连接成功");
            writeValue(reg, response);
            return;
        }
        if (map == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误 或 连接服务器超时");
            writeValue(reg, response);
            return;
        }

        Object name = map.get("name");
        Object email = map.get("email");
        List<Map<String, Object>> list = null;
        try {
            list = UserSign.checkCourseware(openid);
        } catch (Exception e) {
            RespMsg reg = new RespMsg(0, "error", "河南统院服务器拥挤，暂未连接成功");
            writeValue(reg, response);
            return;
        }

        if (list == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误");
            writeValue(reg, response);
            return;
        } else if (list.size() == 0) {
            RespMsg reg = new RespMsg(1, "success", "暂无需要观看的课件");
            writeValue(reg, response);
            return;
        }


        List<String> str = null;
        try {
            str = UserSign.ViewCourseware(openid, list);
        } catch (Exception e) {
            RespMsg reg = new RespMsg(0, "error", "河南统院服务器拥挤，暂未连接成功");
            writeValue(reg, response);
            return;
        }
        if (str == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误");
            writeValue(reg, response);
            return;
        } else if (list.size() == 0) {
            RespMsg reg = new RespMsg(1, "success", "暂无需要观看的课件");
            writeValue(reg, response);
            return;
        }
//        System.out.println(str);

        RespMsg reg = new RespMsg(1, "success", str.toString());
        writeValue(reg, response);

        sendEmail(email.toString(), "河南统院签到系统", "尊敬的用户" + name + "，您好:\n\n服务器在 " + getDate() + " 已成功观看完毕所有课件\n\n返回信息为:" + str.toString() + "\n\n\n\t\t河南信息统计学院签到系统\n\t\tdesigned by：Flik");
        sendWechatMessage("AT_4eG9dAg4tIY6eIcHyJpIWqRLKEEq5gJs", "UID_VNhOsjrNgKeINAxQFY8pf6WFy7JV", str.toString(), "http://www.hntyxxh.com/wechat2-ssr/?openid=" + openid + "&from=wzj");

        System.out.println("[" + getDate() + "]" + "\t用户:" + name + "观看完毕所有课件,返回值为：" + str.toString());
    }

    public static void classback(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException, MessagingException {
        String url = request.getParameter("url");
        if (url == null || url == "") {
            RespMsg reg = new RespMsg(0, "error", "请输入openid");
            writeValue(reg, response);
            return;
        }
        String openid = GetInformation.getOpenid(url);
        Map<String, Object> map = null;
        try {
            map = GetInformation.getNameAndEmail(openid);
        } catch (Exception e) {
            RespMsg reg = new RespMsg(0, "error", "河南统院服务器拥挤，暂未连接成功");
            writeValue(reg, response);
            return;
        }
        if (map == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误 或 连接服务器超时");
            writeValue(reg, response);
            return;
        }

        Object name = map.get("name");
        Object email = map.get("email");
        List<Map<String, Object>> list = null;
        try {
            list = UserSign.checkClass_feedback(openid);
        } catch (Exception e) {
//            e.printStackTrace();
            RespMsg reg = new RespMsg(0, "error", "河南统院服务器拥挤，暂未连接成功");
            writeValue(reg, response);
            return;
        }

        if (list == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误");
            writeValue(reg, response);
            return;
        } else if (list.size() == 0) {
            RespMsg reg = new RespMsg(1, "success", "暂无需要提交的课堂反馈");
            writeValue(reg, response);
            return;
        }


        List<String> str = UserSign.class_feedback(openid, list);
        if (str == null) {
            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid 已过期 或 openid输入错误");
            writeValue(reg, response);
            return;
        }
        System.out.println(str);
        if (list.size() == 0) {
            RespMsg reg = new RespMsg(1, "success", "暂无开启的课堂反馈");
            writeValue(reg, response);
            return;
        }
        RespMsg reg = new RespMsg(1, "success", str.toString());
        writeValue(reg, response);
        sendEmail(email.toString(), "河南统院签到系统", "尊敬的用户" + name + "，您好:\n\n服务器在 " + getDate() + " 已成功提交所有课堂反馈\n\n返回信息为:" + str.toString() + "\n\n\n\t\t河南信息统计学院签到系统\n\t\tdesigned by：Flik");
        sendWechatMessage("AT_4eG9dAg4tIY6eIcHyJpIWqRLKEEq5gJs", "UID_VNhOsjrNgKeINAxQFY8pf6WFy7JV", str.toString(), "http://www.hntyxxh.com/wechat2-ssr/?openid=" + openid + "&from=wzj");
        System.out.println("[" + getDate() + "]" + "\t用户:" + name + "提交完成所有课堂反馈,返回值为：" + str.toString());
    }

    public static void getOpenid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = request.getParameter("url");

        String openid = GetInformation.getOpenid(url);

        if (openid == null) {
//            GetInformation.updateOpenid();
            RespMsg reg = new RespMsg(0, "error", "openid未获取到");
            writeValue(reg, response);
        }

        RespMsg reg = new RespMsg(1, "success", "openid获取成功");

//        System.out.println(openid);
//
//        writeValue(reg, response);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //1.加载配置文件
            Properties pro = new Properties();
            File file = new File("C:\\hnty\\openid.properties");
            fos = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }

            fis = new FileInputStream(file);

            pro.load(fis);

            //2.获取DataSource
            pro.put("openid", openid);
            pro.store(fos, "text");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null || fos != null) {
                fis.close();
                fos.close();

            }
        }
        writeValue(reg, response);
    }


    /**
     * 生成随机验证码验
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void newCode(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        int width = 100;
        int height = 35;
        //创建对象，在内存中生成1个验证码的底图
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        StringBuffer sb = new StringBuffer();
        Graphics g1 = image.getGraphics();
        Graphics2D g = (Graphics2D) g1;
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.gray);
        g.drawRect(0, 0, width - 1, height - 1);
        Random r = new Random();
        g.setColor(getRandomColor());
        String example = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        g.setFont(new Font("楷体", Font.PLAIN, 30));
        g.setColor(Color.red);

        for (int i = 1; i <= 4; i++) {
            char c = example.charAt(r.nextInt(example.length()));
            sb.append(c);

            double x = r.nextInt() % 30;
//            ((Graphics2D) g).rotate(Math.toRadians(x));
//            g.rotate(x * Math.PI / 180, 40*i, height/2);

            g.drawString(c + "", (float) width / 5 * i, (float) ((float) height / 1.5));
//            g.rotate(-x * Math.PI / 180, 40*i, height/2);

        }
        ;
//        request.getSession().setAttribute("CheckCode_Session",sb.toString());
        request.getSession().setAttribute("CHECK_CODE", sb.toString());
        for (int i = 0; i < 5; i++) {
            g.setColor(getRandomColor());
            int x1 = r.nextInt(width);
            int x2 = r.nextInt(width);
            int y1 = r.nextInt(height);
            int y2 = r.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
        for (int i = 0; i < 30; i++) {
            int x1 = r.nextInt(width);
            int y1 = r.nextInt(height);
            g.setColor(getRandomColor());
            g.fillRect(x1, y1, 2, 2);
        }

        //将图片输出到页面上
        ImageIO.write(image, "jpg", response.getOutputStream());
    }

    /**
     * 生成随机颜色
     *
     * @return 返回随机颜色
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
        return color;

    }
    /*public void tyqd2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getParameter("url");
        String str = null;
        RespMsg reg = new RespMsg(0, "error", "未知错误");

        if (url == "" || url == null || !url.contains("www.hntyxxh.com") || !url.contains("openid=")) {
            reg.setCode(0);
            reg.setData("");
            reg.setMsg("url输入错误 正确格式:{ http://www.hntyxxh.com/wechat2-ssr/student/sign?openid=89dc1e37b979a8abc21262212fc4bc4 }");
            writeValue(reg, response);
            return;
        }

        *//***
     * 签到
     * http://www.hntyxxh.com/wechat2-ssr/student/sign?openid=89dc1e37b979a8abc21262212fc4bc4
     * 其他
     * http://www.hntyxxh.com/wechat2-ssr/?openid=fc396e6f24b555c10b75e800f01f9557&from=wzj
     * 邮箱json
     * http://www.hntyxxh.com/wechat-api/v2/students
     *//*
        String[] split = url.split("\\?");//["http://www.hntyxxh.com/wechat2-ssr/student/sign","openid=89dc1e37b979a8abc21262212fc4bc4&from","wzj"]
        String[] openId_map = split[1].split("=");
        if (openId_map.length < 2) {
            reg.setCode(0);

            reg.setData("url错误 未获取到openid");
            reg.setMsg("URL error");
            writeValue(reg, response);
            return;
        }
        String name = openId_map[0];
        String openid = openId_map[1];

        *//*String html = null;
        try {
            html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v1/class-attendance/student/courses", null, name, value);
        } catch (URISyntaxException e) {
            reg.setMsg("error");
            reg.setCode(0);
            reg.setData("连接超时 河南统院服务器响应失败");
            writeValue(reg, response);
            return;
        }

        if (html == null || !html.contains("id")) {
            reg.setCode(0);
            reg.setData(html);
            reg.setMsg("error");
            writeValue(reg, response);
            return;
        }*//*

        try {
            str = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v1/class-attendance/active_sign", null,  openid);
        } catch (Exception e) {
            reg.setMsg("error");
            reg.setCode(0);
            reg.setData("连接超时 河南统院服务器响应失败");
            writeValue(reg, response);
            return;
        }
//        str="{\"courseId\":3176,\"signId\":10888,\"isGPS\":0,\"isQR\":0}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        if (jsonObject.get("message") != null) {
            reg.setMsg("error");
            reg.setCode(0);
            reg.setData((String) jsonObject.get("message"));
            writeValue(reg, response);
            return;
        }
//        System.out.println(str);
        *//**
     * 判断是否从网页获取到信息
     *//*
        if (str == null) {
            reg.setMsg("error");
            reg.setCode(0);
            reg.setData("连接超时 河南统院服务器响应失败");
            writeValue(reg, response);
            return;
        }


        Object courseId = jsonObject.get("courseId");
        Object signId = jsonObject.get("signId");
        if (str == "{}" || courseId == null) {
            reg.setMsg("success");
            reg.setCode(1);
            reg.setData("暂无开启的签到");
            writeValue(reg, response);
            return;
        }
        jsonObject.clear();
        jsonObject.put("courseId", courseId);
        jsonObject.put("signId", signId);


        String post = HttpClientUtil.sendJson("http://www.hntyxxh.com/wechat-api/v1/class-attendance/student-sign-in", jsonObject, "utf-8", openid);


        reg.setMsg("签到成功");
        reg.setCode(1);
        reg.setData(post);
        writeValue(reg, response);

    }*/

}
