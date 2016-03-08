package com.smapley.powerwork.Utils;


import android.text.format.Time;

/**
 * Created by smapley on 2015/4/30.
 */
public class MyData {

    public static final String SUCC = "succ";
    public static final String SP = "user";
    public static final String SP_MAINLIST = "main_list";

    public static final String URL_FILE = "http://team.zz.mu/";
    public static final String URL_REGISTER = URL_FILE + "index.php?m=User&c=User&a=Register";
    public static final String URL_LOGIN = URL_FILE + "index.php?m=User&c=User&a=Login";
    public static final String URL_FORGET = URL_FILE + "index.php?m=User&c=User&a=ForgetPassword";
    public static final String URL_GETTASKLIST = URL_FILE + "index.php?m=Task&c=Task&a=GetTaskList";
    public static final String URL_GETTASK = URL_FILE + "index.php?m=Task&c=Task&a=GetTaskDetails";
    public static final String URL_GETTEAMLIST = URL_FILE + "index.php?m=Team&c=Team&a=GetTeamList";
    public static final String URL_GETHISTASK = URL_FILE + "index.php?m=Task&c=Task&a=GetOldTaskList";
    public static final String URL_GETTEAMTASK = URL_FILE + "index.php?m=Task&c=Task&a=GetTaskList";

    public static final String CACHE_PIC = "/PowerWork/ImageCache/";

    public static final int ORDER_CREATETIME = 0;
    public static final int ORDER_STARTTIME = 1;
    public static final int ORDER_ENDTIME = 2;
    public static final int ORDER_FREETIME = 3;

    public static String YEAR = "";
    public static String MONTH = "";
    public static String DAY = "";
    public static String TaskId;
    public static int TEAMID;



    /**
     * 获取服务器加密码
     * key
     *
     * @return
     */
    public static int getKey() {
        int key = 0;
        key = 1 + (int) (Math.random() * 999);
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int date = t.monthDay;
        return key * 789 * date;
    }
}
