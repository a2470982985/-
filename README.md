# 河南信息统计职业学院-精华平台学生自动化签到系统


## 河南信息统计学院Api-精华平台签到系统
* OpenID最长使用时间为45分钟
* 此方式可配合抓包工具fiddler来建成全自动系统
* 使用方面如有问题,可加QQ群咨询
* ![qun](https://pic.downk.cc/item/5fe37e8c3ffa7d37b30727c9.png)

注意：此Api接口内openid需从电脑版内置的浏览器复制
获取方法：
1. 登录电脑版本微信
2. 打开河南信息统计学院精华平台
3. 依次点击 学生-其他
4. 在弹出窗体内点击同意并等待加载完毕后
5. 点击复制连接-也就是字母A后边的图标即可复制
6. 将连接粘贴到url=后方 
7. 例如/hnty/user/classback?url=http://www.hntyxxh.com/wechat2-ssr/?openid=23bcdb9a1a795c76cc3fc20abc486332
5. ![logo](https://pic.downk.cc/item/5fe37be43ffa7d37b305b536.png)

#### 自动提交课堂反馈API
```
# */hnty/user/classback?url=
```
#### 查看是否需要观看课件API（需配合观看课件使用）
```
# */hnty/user/checkCourseware?url=
```
#### 查看自动签到API
```
# */hnty/user/tyqd?url=
```

## 获取方法(通过git命令)
```
git clone https://github.com/a2470982985/-.git
```
