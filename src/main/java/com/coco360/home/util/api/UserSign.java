package com.coco360.home.util.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coco360.home.util.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserSign {
    /**
     * 检查需要签到的课堂
     *
     * @param openid
     * @return {}：表示暂未有需要签到的课堂  null 表示openid过期 抛出异常为连接超时 其他则为json需要签到的课堂
     * @throws IOException
     */
    public static String checkSign(String openid) throws Exception {
        String html = null;

        html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v1/class-attendance/active_sign", null, openid);

//        html="{\"courseId\":3176,\"signId\":10888,\"isGPS\":0,\"isQR\":0}";
        JSONObject jsonObject = JSONObject.parseObject(html);
        if (jsonObject.get("message") != null) {
            return null;
        }
        if ("{}".contains(html) || html == "") {
            return html;
        }


        // "{"message":"error"}"


        Object courseId = jsonObject.get("courseId");
        Object signId = jsonObject.get("signId");
        if (courseId == null && signId == null) {
            return null;
        }
        jsonObject.clear();
        jsonObject.put("courseId", courseId);
        jsonObject.put("signId", signId);
//        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }

    /**
     * 根据传入的Sign_Json 来进行统院签到
     *
     * @param Sign_Json 需要签到的json
     * @param openid    openid
     * @return null openid已过期   抛出异常服务器连接超时   其他为需要签到的课堂
     */
    public static String Sign_in(String Sign_Json, String openid) throws Exception {
        String html = null;

        html = HttpClientUtil.sendJson("http://www.hntyxxh.com/wechat-api/v1/class-attendance/student-sign-in", JSONObject.parseObject(Sign_Json), "utf-8", openid);

        //        System.out.println(html);
        //        html = "{\"courseId\":3550,\"signId\":11535}";
        JSONObject jsonObject = JSONObject.parseObject(html);
        if (jsonObject.get("message") != null) {
            return null;
        }
        if (jsonObject.get("errorCode") != null || jsonObject.get("msgClient") != null) {
            jsonObject.clear();
            jsonObject.put("error", html);
            return jsonObject.toString();
        }


//        {"signRank":33,"studentRank":34}
        Object courseId = jsonObject.get("signRank");
        Object signId = jsonObject.get("studentRank");
        if (courseId == null && signId == null) return null;
        jsonObject.clear();
        jsonObject.put("本课堂开启签到次数", courseId);
        jsonObject.put("您本次签到名次", signId);
//        return jsonObject.toString();
        return jsonObject.toString();
    }

    /**
     * 检查是否有课件需要查看
     *
     * @param openid
     * @return null为openid已经过期  list为空服务器连接超时   list有数据则为需要观看的课件 [{teacherName=秦航琪, name=单片机应用与技术-物联1901, id=3176}]
     * 返回示例
     * [{teacherName=秦航琪, name=单片机应用与技术-物联1901, unreadCount=15, id=3176, url=http://www.hntyxxh.com/wechat-api/v1/coursewares/3176/student}]
     */
    public static List<Map<String, Object>> checkCourseware(String openid) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String html = null;

        html = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v1/courses/openCoursewares", null, openid);

//        [{"id":3085,"name":"C#物联网应用程序开发-物联1901","cover":"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png","teacherName":"焦宁","avatar":"http://www.hntyxxh.com/nas/files/44c69bcb/kerok_msg1599454509","college":"河南信息统计职业学院","code":"D085","department":"未分类人员","count":8,"unreadCount":0},{"id":3176,"name":"单片机应用与技术-物联1901","cover":"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png","teacherName":"秦航琪","avatar":"http://www.hntyxxh.com/nas/files/9cb1d774/houfh_msg1576909743","college":"河南信息统计职业学院","code":"D176","department":"大数据教研室","count":72,"unreadCount":15},{"id":3419,"name":"python编程-物联1901","cover":"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png","teacherName":"魏瑶","avatar":"https://app.teachermate.com.cn/images/teacher-default-avatar.png","college":"河南信息统计职业学院","code":"D419","department":"统计教研室","count":60,"unreadCount":0},{"id":3550,"name":"路由器与交换技术-物联1901","cover":"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png","teacherName":"秦航琪","avatar":"http://www.hntyxxh.com/nas/files/9cb1d774/houfh_msg1576909743","college":"河南信息统计职业学院","code":"D550","department":"大数据教研室","count":44,"unreadCount":0},{"id":3633,"name":"周三2国学（合班）-财管物联信息造价视传（01)","cover":"https://app.teachermate.com.cn/covers/art3.png","teacherName":"李敏魁","avatar":"http://www.hntyxxh.com/nas/files/88142530/lagca_msg1575362337","college":"河南信息统计职业学院","code":"D633","department":"公共课教研室","count":48,"unreadCount":0}]
//        html="[{\"id\":3085,\"name\":\"C#物联网应用程序开发-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"焦宁\",\"avatar\":\"http://www.hntyxxh.com/nas/files/44c69bcb/kerok_msg1599454509\",\"college\":\"河南信息统计职业学院\",\"code\":\"D085\",\"department\":\"未分类人员\",\"count\":8,\"unreadCount\":0},{\"id\":3176,\"name\":\"单片机应用与技术-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"秦航琪\",\"avatar\":\"http://www.hntyxxh.com/nas/files/9cb1d774/houfh_msg1576909743\",\"college\":\"河南信息统计职业学院\",\"code\":\"D176\",\"department\":\"大数据教研室\",\"count\":72,\"unreadCount\":15},{\"id\":3419,\"name\":\"python编程-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"魏瑶\",\"avatar\":\"https://app.teachermate.com.cn/images/teacher-default-avatar.png\",\"college\":\"河南信息统计职业学院\",\"code\":\"D419\",\"department\":\"统计教研室\",\"count\":60,\"unreadCount\":0},{\"id\":3550,\"name\":\"路由器与交换技术-物联1901\",\"cover\":\"https://app.teachermate.com.cn/6063beadeea6b0c267d8f76147cca905.png\",\"teacherName\":\"秦航琪\",\"avatar\":\"http://www.hntyxxh.com/nas/files/9cb1d774/houfh_msg1576909743\",\"college\":\"河南信息统计职业学院\",\"code\":\"D550\",\"department\":\"大数据教研室\",\"count\":44,\"unreadCount\":0},{\"id\":3633,\"name\":\"周三2国学（合班）-财管物联信息造价视传（01)\",\"cover\":\"https://app.teachermate.com.cn/covers/art3.png\",\"teacherName\":\"李敏魁\",\"avatar\":\"http://www.hntyxxh.com/nas/files/88142530/lagca_msg1575362337\",\"college\":\"河南信息统计职业学院\",\"code\":\"D633\",\"department\":\"公共课教研室\",\"count\":48,\"unreadCount\":0}]";
//        System.out.println(html);

        if (!html.contains("cover") || html.contains("message")) {
            return null;
        }
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
        }


        return list;
    }

    /**
     * 根据checkCourseware的返回值来进行遍历查看所有课件
     *
     * @param openid openid
     * @param list   checkCourseware的返回值
     * @return null为openid过期 list为空服务器连接超时 list则为成功查看的课件
     */
    public static List<String> ViewCourseware(String openid, List<Map<String, Object>> list) throws Exception {
        Map<String, Object> map = null;
        String html = null;
        String str = null;
        List<String> view_List = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = null;
        JSONObject jsonObject2 = null;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            Object url = map.get("url");

            html = HttpClientUtil.doGet(url.toString(), null, openid);

            jsonObject = JSONObject.parseObject(html);
            if (html == null || jsonObject.get("message") != null) {
                return null;
            }
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("coursewares").toString());
            for (int j = 0; j < jsonArray.size(); j++) {

                String json = jsonArray.get(j).toString();
                jsonObject1 = JSONObject.parseObject(json);
                boolean readStatus = (boolean) jsonObject1.get("readStatus");
                if (readStatus == true) {
                    continue;
                }
                String name = jsonObject1.get("name").toString();
                String previewUrl = jsonObject1.get("previewUrl").toString();

                try {
                    str = HttpClientUtil.doGet(previewUrl, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
//                s={"url":"http://www.hntyxxh.com/ow/?&fname=5f9b805ae318642eb7676f54&furl=http://www.hntyxxh.com/nas/files/8e97ecef6633d7d19183023ef7647ce3_82e89eefbbeaaf5e9af232476064f1c3.pdf","accessId":"5fa0e6e5d2a7ed014a89b44f"}
                jsonObject2 = JSONObject.parseObject(str);
                String coursewareUrl = jsonObject2.get("url").toString();
                System.out.println(coursewareUrl);
                try {
                    HttpClientUtil.doGet(coursewareUrl, null, null);
                } catch (Exception e) {
                    continue;
                }
                view_List.add(name + ":" + coursewareUrl);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
        return view_List;
    }

    /**
     * 检查是否有课堂反馈需要提交
     *
     * @param openid
     * @return null openid已过期 list 为空为服务器连接超时  返回list则为需要进行的课堂反馈
     */
    public static List checkClass_feedback(String openid) throws Exception {
        //        http://www.hntyxxh.com/wechat-api/v3/students/orgFeedbacks

        String str = null;
        str = HttpClientUtil.doGet("http://www.hntyxxh.com/wechat-api/v3/students/orgFeedbacks", openid);
//        str="[{\"courseName\":\"毛泽东思想和中国特色社会主义理论体系概论（3）-物联\",\"code\":\"D581\",\"teacherName\":\"宋晗\",\"startTime\":\"2020-11-05T10:10:00.000Z\",\"id\":\"6786\",\"type\":2},{\"courseName\":\"路由器与交换技术-物联1901\",\"code\":\"D550\",\"teacherName\":\"秦航琪\",\"startTime\":\"2020-11-04T10:10:00.000Z\",\"id\":\"6374\",\"type\":2},{\"courseName\":\"路由器与交换技术-物联1901\",\"code\":\"D550\",\"teacherName\":\"秦航琪\",\"startTime\":\"2020-11-04T08:20:00.000Z\",\"id\":\"6314\",\"type\":2}]\n";
        System.out.println(str);
        if ("[]".equals(str)) {
            return new ArrayList();
        }
        if (str == null || str.contains("message")) {
            return null;
        }

        JSONObject jsonObject = null;
        JSONArray jsonArray = JSONObject.parseArray(str);
        System.out.println(jsonArray.size());
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < jsonArray.size(); i++) {
            map=new HashMap<String, Object>();
            jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());

            Object courseName = jsonObject.get("courseName");
            Object teacherName = jsonObject.get("teacherName");
            Object startTime = jsonObject.get("startTime");
            Object id = jsonObject.get("id");
            map.put("courseName", courseName);
            map.put("teacherName", teacherName);
            map.put("startTime", startTime);
            map.put("id", id);
            list.add(map);
        }
        System.out.println(list);
        return list;
    }

    /**
     * 根据检查课堂反馈得到的list集合  进行全部提交课堂反馈
     *
     * @param openid openid
     * @param list   检查 课堂反馈方法得到的list集合
     * @return 课堂反馈的结果  [] 为服务器连接超时
     */
    public static List class_feedback(String openid, List<Map<String, Object>> list) {
        List text = new ArrayList();
        Map reMap = new HashMap();

        String str = null;
//        POST  {"comment":"","dimensions":[5,5,5,5],"questions":[]}
        JSONObject jsonObject = JSONObject.parseObject("{\"comment\":\"\",\"dimensions\":[5,5,5,5],\"questions\":[]}");


        //        http://www.hntyxxh.com/wechat-api/v3/students/orgFeedbacks/6786



        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map =null;
            map = list.get(i);
            Object courseName = map.get("courseName");
            Object teacherName = map.get("teacherName");
            Object id = map.get("id");
            //http://www.hntyxxh.com/wechat2-ssr/student/organClassFeedback/6374
            try {
                str = HttpClientUtil.sendJson("http://www.hntyxxh.com/wechat-api/v3/students/orgFeedbacks/" + id.toString(), jsonObject, "utf-8", openid);
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList();
            }
            if ("{}".equals(str)) {
                reMap.put("courseName", courseName.toString());
                reMap.put("teacherName", teacherName.toString());
                reMap.put("id", id.toString());
                text.add(reMap);

            } else {
                reMap.put("courseName", courseName.toString());
                reMap.put("error", "反馈失败");
                reMap.put("id", id.toString());
                text.add(reMap);
            }
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return text;

    }


}
