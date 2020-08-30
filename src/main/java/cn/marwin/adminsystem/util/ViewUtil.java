package cn.marwin.adminsystem.util;

import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

public class ViewUtil {
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

        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            // 记录浏览量
            // 如果key不存在，则创建zset并执行zadd。如果member不存在，则执行zadd
            jedis.zincrby(type + "viewcount", 1, "" + id);

            // 记录浏览历史
            jedis.rpush(type + "view:" + id, uid + " @ " + clientAddr + " @ " + TimeUtil.getCurrentTime());
        } finally {
            RedisUtil.returnResource(jedis);
        }
    }

    public static int getViewCount(String type, Integer id) {
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            if (jedis.exists(type + "viewcount")) {
                double count = jedis.zscore(type + "viewcount", "" + id);
                return (int) count;
            } else {
                return 0;
            }
        } finally {
            RedisUtil.returnResource(jedis);
        }
    }
}
