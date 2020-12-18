package com.coco360.home.main;


import com.coco360.home.util.HttpClientUtil;
import com.coco360.home.util.api.GetInformation;
import com.coco360.home.web.servlet.BaseServlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;


public class run {

    static String all_openid = null;
    static String Some_openid = null;
    static long read_openidTime = 1000 * 30;
    static long checkCoursewareTime = 3600000;
    static long classBackTime = 3600000;
    static long ty_qdTime = 1000 * 60;
    static String prefix_Url = "http://localhost:8080/hnty/user/";
    static String WeChart="D:/Program Files (x86)/Tencent/WeChat/WeChat.exe";

    static {

        Properties p = new Properties();
        InputStream is = null;
        try {
            // 配置文件位于当前目录中的config目录下
            is = new BufferedInputStream(new FileInputStream("config/" + "config.properties"));
//            is=run.class.getClassLoader().getResourceAsStream("config.properties");
            p.load(is);
            read_openidTime = Long.parseLong(p.get("read_openidTime").toString());
            checkCoursewareTime = Long.parseLong(p.get("checkCoursewareTime").toString());
            classBackTime = Long.parseLong(p.get("classBackTime").toString());
            ty_qdTime = Long.parseLong(p.get("ty_qdTime").toString());
            prefix_Url = p.get("prefix_Url").toString();
            WeChart=p.get("WeChart").toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    public static String getDate()  {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        return format;
    }
    public static void main(String[] args) {
        read_OpenID_Runnable ror = new read_OpenID_Runnable();
        Thread read_openID_Thread = new Thread(ror);
        read_openID_Thread.start();

        ty_qd_Runnable qd = new ty_qd_Runnable();
        Thread qd_Thread = new Thread(qd);
        qd_Thread.start();

        checkCourseware_Runnable ccr = new checkCourseware_Runnable();
        Thread checkCourseware_Thread = new Thread(ccr);
        checkCourseware_Thread.start();

        classBack_Runnable cbr = new classBack_Runnable();
        Thread classBack_Thread = new Thread(cbr);
        classBack_Thread.start();


        System.out.println("请输入对应数字开启相应功能");
        System.out.println("0.打开菜单");
        System.out.println("1.自动签到");
        System.out.println("2.自动观看课件");
        System.out.println("3.自动提交课堂反馈");
        System.out.println("9.查看任务状态");

        while (true) {
            Scanner sc = new Scanner(System.in);
            int i = sc.nextInt();
            if (i == 0) {
                System.out.println("请输入对应数字开启相应功能");
                System.out.println("0.打开菜单");
                System.out.println("1.开启签到");
                System.out.println("2.观看课件");
                System.out.println("3.自动提交课堂反馈");
                System.out.println("9.查看任务状态");
            }
            if (i == 1) {

                if (qd.qd_Bool == true) {
                    qd.qd_Bool = false;
                    System.out.println('['+getDate()+']'+"签到任务已关闭");
//                    关闭方法
                } else {
                    qd.qd_Bool = true;
                    System.out.println('['+getDate()+']'+"签到任务已开启");
                    qd_Thread.interrupt();
                }
            }
            if (i == 2) {
                if (ccr.checkCourseware_Bool == true) {
                    ccr.checkCourseware_Bool = false;
                    System.out.println('['+getDate()+']'+"观看课件任务已关闭");
//                    关闭方法
//                    checkCourseware_Thread.interrupt();
                } else {
                    ccr.checkCourseware_Bool = true;
                    System.out.println('['+getDate()+']'+"观看课件任务已开启");
//                    开启方法
                    checkCourseware_Thread.interrupt();

                }
            }
            if (i == 3) {
                if (cbr.classBack_Bool == true) {
                    cbr.classBack_Bool = false;
                    System.out.println('['+getDate()+']'+"自动提交课堂反馈已关闭");
//                    关闭方法
//                    checkCourseware_Thread.interrupt();
                } else {
                    cbr.classBack_Bool = true;
                    System.out.println('['+getDate()+']'+"自动提交课堂反馈已开启");
//                    开启方法
                    classBack_Thread.interrupt();

                }
            }
            if (i == 9) {
                System.out.println("-----当前任务状态-----");
                System.out.println("自动签到任务:" + qd.qd_Bool);
                System.out.println("查看课件任务:" + ccr.checkCourseware_Bool);
                System.out.println("自动课堂反馈:" + cbr.classBack_Bool);
                System.out.println("-----当前任务状态-----");
            }
        }

    }

    static class ty_qd_Runnable implements Runnable {

        boolean qd_Bool = false;

        @Override
        public void run() { //run方法，里面包含要执行的任务

            String str = null;
            while (true) {
                while (qd_Bool) {
                    try {
                        str = HttpClientUtil.doGet(prefix_Url + "tyqd?url=http://www.hntyxxh.com/wechat2-ssr/?openid=" + all_openid
                                , all_openid);

                        System.out.println('['+getDate()+']'+str);

                        Thread.sleep(ty_qdTime);

                    } catch (Exception e) {


                    }
                }
                try {

                    Thread.sleep(1000 * 60 * 60 * 24 * 365);
                } catch (InterruptedException e) {
//                    e.printStackTrace();

                }


            }
        }
    }

    static class classBack_Runnable implements Runnable {

        boolean classBack_Bool = false;

        @Override
        public void run() { //run方法，里面包含要执行的任务
            String str = null;
            while (true) {
                while (classBack_Bool) {
                    try {
                        str = HttpClientUtil.doGet(prefix_Url + "classback?url=http://www.hntyxxh.com/wechat2-ssr/?openid=" + all_openid
                                , all_openid);

                        System.out.println('['+getDate()+']'+str);

                        Thread.sleep(classBackTime);

                    } catch (Exception e) {


                    }
                }
                try {

                    Thread.sleep(1000 * 60 * 60 * 24 * 365);
                } catch (InterruptedException e) {
//                    e.printStackTrace();

                }


            }
        }
    }

    static class checkCourseware_Runnable implements Runnable {
        boolean checkCourseware_Bool = false;

        @Override
        public void run() {
            String str = null;
            while (true) {
                while (checkCourseware_Bool) {
                    try {
                        str = HttpClientUtil.doGet(prefix_Url + "checkCourseware?url=http://www.hntyxxh.com/wechat2-ssr/?openid=" + all_openid
                                , all_openid);

                        System.out.println('['+getDate()+']'+str);

                        Thread.sleep(checkCoursewareTime);

                    } catch (Exception e) {


                    }


                }
                try {
                    Thread.sleep(1000 * 60 * 60 * 24 * 365);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
            }
        }
    }

    static class read_OpenID_Runnable implements Runnable {

        @Override
        public void run() {
            File file = new File("C:\\hnty\\openid.properties");
            file.delete();

            while (true) {
                FileReader fis = null;
                Properties pro = new Properties();
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fis = new FileReader(file);
                    pro.load(fis);
//                    Some_openid = all_openid;
                    all_openid = pro.getProperty("openid");
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (all_openid != null) {
                    if (!all_openid.equalsIgnoreCase(Some_openid)) {
                        Some_openid = all_openid;
                        System.out.println('['+getDate()+']'+"成功获取OpenID:" + all_openid);
                        BaseServlet.sendWechatMessage("AT_4eG9dAg4tIY6eIcHyJpIWqRLKEEq5gJs", "UID_VNhOsjrNgKeINAxQFY8pf6WFy7JV",'['+getDate()+']'+"成功获取openid：" +all_openid, "http://www.hntyxxh.com/wechat2-ssr/?openid="+all_openid+"&from=wzj");
                    }
                    continue;
                }
                try {
                    GetInformation.updateOpenid(WeChart);
                    Thread.sleep(read_openidTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
