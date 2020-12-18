package com.coco360.home.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class BaseServlet extends HttpServlet {
    @Override
    /**
     * 方法分发 通过重写 HTTPServlet中service的方法  完成方法分发
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        String uri = req.getRequestURI();   // 返回 /user/add
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        try {
//            this.getClass().getDeclaredMethods();  //忽略权限修饰符
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
//            method.setAccessible(true);// 暴力反射
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public static void writeValue(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), obj);
        
    }
    public static String getDate()  {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        return format;
    }
    public static boolean sendWechatMessage(String AppToken, String Uid, String Content, String Url) {
        Message message = new Message();
        message.setAppToken(AppToken);
        message.setContentType(Message.CONTENT_TYPE_TEXT);
        message.setContent(Content);
        message.setUid(Uid);
        if (Url==null){

        }
        message.setUrl(Url);//可选参数
        Result<List<MessageResult>> result = WxPusher.send(message);
        boolean success = result.isSuccess();
        return success;
    }
    public static void sendEmail(String send_to, String title, String content) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        //设置邮件地址
        props.put("mail.smtp.host", "smtp.163.com");
        //开启认证
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, null);
        Transport transport = session.getTransport();
        //用户名
        String user = "m13352988493@163.com";
        //授权码
        String password = "OHEIQLAPDNONBZPF";
        transport.connect(user, password);
        //创建邮件消息
        MimeMessage msg = new MimeMessage(session);
        msg.setSentDate(new Date());
        //邮件发送人
        InternetAddress fromAddress = new InternetAddress(user, "河南统院签到系统");
        msg.setFrom(fromAddress);
        //邮件接收人
        String to = send_to;
        InternetAddress[] toAddress = new InternetAddress[]{new InternetAddress(to)};
        msg.setRecipients(javax.mail.Message.RecipientType.TO, toAddress);
        //邮件主题
        msg.setSubject(title, "UTF-8");
        //邮件内容和格式
        msg.setContent(content, "text/html;charset=UTF-8");
        msg.saveChanges();
        //发送
        transport.sendMessage(msg, msg.getAllRecipients());
    }

}
