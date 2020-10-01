package cn.marwin.adminsystem.util;

import javax.servlet.http.HttpServletRequest;

public class ViewUtil {
    /**
     * 记录浏览信息
     * 为了简化系统需要替换成另一种方式
     *
     * @param type blog或item
     * @param id 对应的id
     * @param request 这次访问的request
     */
    public static void logView(String type, Integer id, HttpServletRequest request) {
        int uid = 0;
        if (UserUtil.getLoginUser() != null) {
            uid = UserUtil.getLoginUser().getId();
        }

        String clientAddr;
        if (request.getHeader("x-forwarded-for") != null) {
            clientAddr = request.getHeader("x-forwarded-for") + ":" + request.getRemotePort();
        } else {
            clientAddr = request.getRemoteAddr() + ":" + request.getRemotePort();
        }
        System.out.printf("%s viewed %s:%s @ %s, %s\n", uid, type, id, clientAddr, TimeUtil.getCurrentTime());
    }

    /**
     * 返回浏览量
     * 为了简化系统，需要替换成另一种记录方式
     *
     * @param type blog获取item
     * @param id 对应的id
     * @return 浏览量
     */
    public static int getViewCount(String type, Integer id) {
        return 0;
    }
}
