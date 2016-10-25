package com.rimi.angel.angeldoctor.http;

/**
 * Created by Android on 2016/6/6.
 */
public class HttpUrls {
    //西区医院端测试接口
    public static final String TEST_URL = "http://61.139.124.246:90/";
    //昆明angel医院接口
//    public static final String TEST_URL = "http://116.53.248.138:90/";
    //rimi 测试接口 内网
//    private static final String RIMI_TEST_URL = "http://192.168.6.36:1234/";
//    private static final String RIMI_TEST_URL = "http://do.rimiedu.com/angel/"; //外网
    private static final String RIMI_TEST_URL = "http://app.cdangel.com/"; //外网
    //登录 || 医生关联病人列表
    public static final String LOGIN = TEST_URL + "his/HisCustomer.ashx";
    //医生查看报表 | 电子报告详情
    public static final String REPORT = TEST_URL + "his/HisReport.ashx";
    //个人中心
    public static final String USERINFO = TEST_URL + "Customer/Doctor.aspx?action=GetDoctor";
    //今日预约
    public static final String TODAY_APPOINTMENT = TEST_URL + "Customer/AppList.aspx?action=GetAppList";
    //我的预约
    public static final String MY_APPOINTMENT = TEST_URL + "Customer/Doctor.aspx?action=GetDateList";
    //门诊病历
    public static final String BING_LI = TEST_URL + "his/HisOrder.ashx";
    //就诊信息
    public static final String JIUZHEN_DATA =TEST_URL + "his/HisOrder.ashx";
    //查询用户信息
    public static final String QUERY_DATA = TEST_URL + "his/HisAppRegister.ashx";
    //意见反馈
    public static final String FEEDBACK = RIMI_TEST_URL + "api/complaint/doctor/add";
    //是否有新消息状态查询接口
    public static final String HAVE_NEW_MSG = RIMI_TEST_URL + "api/doctor/message/status";
    //同步会话token
    public static final String JUDGE_TOKNE = RIMI_TEST_URL + "api/doctor/token/session";
    //消息列表
    public static final String MSG_LIST = RIMI_TEST_URL + "api/doctor/message";
    //阅读接口
    public static final String READ_MSG = RIMI_TEST_URL + "api/doctor/message/read";
    //同步推送token
    public static final String JUDGE_PUSH_TOKEN = RIMI_TEST_URL + "api/doctor/token/android";
    //删除消息
    public static final String DELETE_MSG = RIMI_TEST_URL + "api/doctor/message/delete";
    //容联云电话回拨
    public static final String CALL_BACK = "https://app.cloopen.com:8883/2013-12-26/SubAccounts/83418ceb37ae11e6bb9bac853d9f54f2/Calls/Callback?sig=";
    //检查更新
    public static final String CHECK_VERSION = "http://app.cdangel.com/checkversioncode.json";
    //获取客服电话
    public static final String GET_PHONE = RIMI_TEST_URL + "api/system/query";
}
