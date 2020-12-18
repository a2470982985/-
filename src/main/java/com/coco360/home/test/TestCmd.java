package com.coco360.home.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coco360.home.util.HttpClientUtil;
import com.coco360.home.util.api.GetInformation;
import com.coco360.home.util.api.UserSign;
import com.coco360.home.web.servlet.BaseServlet;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCmd {
    @Test
    public void test() throws Exception {
        String openid = GetInformation.getOpenid("http://www.hntyxxh.com/wechat2-ssr/?openid=7888122c23f2ff43da5dddf24afadce6&from=wzj");
        List<Map<String, Object>> list = UserSign.checkCourseware(openid);
//        [{teacherName=魏瑶, name=python编程-物联1901, unreadCount=6, id=3419, url=http://www.hntyxxh.com/wechat-api/v1/coursewares/3419/student}, {teacherName=魏瑶, name=python编程-物联1901, unreadCount=6, id=3419, url=http://www.hntyxxh.com/wechat-api/v1/coursewares/3419/student}]
//        [{teacherName=秦航琪, name=单片机应用与技术-物联1901, unreadCount=8, id=3176, url=http://www.hntyxxh.com/wechat-api/v1/coursewares/3176/student}]
        System.out.println(list.toString());
        List<String> list1 = UserSign.ViewCourseware(openid, list);
        System.out.println(list1);


    }

    @Test
    public void test1() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String html = "[{\"id\":3085,\"name\":\"C#物联网应用程序开发-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"焦宁\",\"avatar\":\"http://www.hntyxxh.com/nas/files/44c69bcb/kerok_msg1599454509\",\"college\":\"河南信息统计职业学院\",\"code\":\"D085\",\"department\":\"未分类人员\",\"count\":8,\"unreadCount\":23},{\"id\":3176,\"name\":\"单片机应用与技术-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"秦航琪\",\"avatar\":\"http://www.hntyxxh.com/nas/files/9cb1d774/houfh_msg1576909743\",\"college\":\"河南信息统计职业学院\",\"code\":\"D176\",\"department\":\"大数据教研室\",\"count\":72,\"unreadCount\":15},{\"id\":3419,\"name\":\"python编程-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"魏瑶\",\"avatar\":\"https://app.teachermate.com.cn/images/teacher-default-avatar.png\",\"college\":\"河南信息统计职业学院\",\"code\":\"D419\",\"department\":\"统计教研室\",\"count\":60,\"unreadCount\":0},{\"id\":3550,\"name\":\"路由器与交换技术-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"秦航琪\",\"avatar\":\"http://www.hntyxxh.com/nas/files/9cb1d774/houfh_msg1576909743\",\"college\":\"河南信息统计职业学院\",\"code\":\"D550\",\"department\":\"大数据教研室\",\"count\":44,\"unreadCount\":0},{\"id\":3633,\"name\":\"周三2国学（合班）-财管物联信息造价视传（01)\",\"cover\":\"https://app.teachermate.com.cn/covers/art3.png\",\"teacherName\":\"李敏魁\",\"avatar\":\"http://www.hntyxxh.com/nas/files/88142530/lagca_msg1575362337\",\"college\":\"河南信息统计职业学院\",\"code\":\"D633\",\"department\":\"公共课教研室\",\"count\":48,\"unreadCount\":0}]";
        JSONObject jsonObject = null;
        JSONArray jsonArray = JSONArray.parseArray(html);

        for (int i = 0; i < jsonArray.size(); i++) {
//            System.out.println(jsonArray.get(i));

            jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            int unreadCount = Integer.parseInt(jsonObject.get("unreadCount").toString());
            if (unreadCount == 0) {
                continue;
            }
            //http://www.hntyxxh.com/wechat-api/v1/coursewares/3176/student
            Object id = jsonObject.get("id");
            Object name = jsonObject.get("name");
            Object teacherName = jsonObject.get("teacherName");
            map.put("id", id);
            map.put("name", name);
            map.put("unreadCount", unreadCount);
            map.put("teacherName", teacherName);
            map.put("url", "http://www.hntyxxh.com/wechat-api/v1/coursewares/" + id + "/student");

            list.add(map);
            System.out.println(list.toString());
        }

    }
}
