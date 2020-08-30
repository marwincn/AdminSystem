package cn.marwin.adminsystem.recommend;

import cn.marwin.adminsystem.entity.platform.Star;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.repository.platform.ItemDAO;
import cn.marwin.adminsystem.repository.platform.StarDAO;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ItemCF {
    @Autowired
    ItemDAO itemDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    StarDAO starDAO;

    private double[][] similarTable; // 新增物品，用户有新行为时刷新
    private int tableSize;

    public void initTable() {
        tableSize = (int)itemDAO.count();
        similarTable = new double[tableSize + 1][tableSize + 1];
        System.out.println("[" + TimeUtil.getCurrentTime() + "]InitTable Complete!");
    }

    // 获取相似矩阵的分子
    public void initMatrix() {
        List<User> users = userDAO.findAll();
        for (User user: users) {
            List<Star> stars = starDAO.findByUid(user.getId());
            for (int i = 0; i <= stars.size() - 2; i++) {
                for (int j = i + 1; j <= stars.size() - 1; j++) {
                    similarTable[stars.get(i).getIid()][stars.get(j).getIid()]++;
                    similarTable[stars.get(j).getIid()][stars.get(i).getIid()]++;
                }
            }
        }
        System.out.println("[" + TimeUtil.getCurrentTime() + "]InitMatrix Complete!");
    }

    // 计算物品间的相似性，得到相似矩阵
    public void calcuSimilarity() {
        for (int i = 1; i <= tableSize; i++) {
            // 只计算半边，另一半由对称性获得
            for (int j = i + 1; j <= tableSize; j++) {
                if (similarTable[i][j] != 0) {
                    // 注意long溢出
                    long ni = starDAO.countByIid(i);
                    long nj = starDAO.countByIid(j);
                    // 分子可以根据IUF参数优化，削弱热门物品的权重
                    similarTable[i][j] = similarTable[i][j] / Math.sqrt(ni + nj);
                    similarTable[j][i] = similarTable[i][j];
                }
            }
        }
        System.out.println("[" + TimeUtil.getCurrentTime() + "]Calculate Similarity Complete!");
    }

    // 归一化
    public void normSimilarity() {
        for (int i = 1; i <= tableSize; i++) {
            double max = 0;
            int j = 1;

            for (; j <= tableSize; j++) {
                max = similarTable[i][j] > max ? similarTable[i][j]: max;
            }

            for (j = i + 1; j <= tableSize; j++) {
                similarTable[i][j] = similarTable[i][j] / max;
                similarTable[j][i] = similarTable[i][j];
            }
        }
        System.out.println("[" + TimeUtil.getCurrentTime() + "]Normalize Similarity Complete!");
    }

    public int[] getMostInterest(int uid, int count) {
        int weight = 1; // 计算该行为产生的相似性的对兴趣影响的权重
        double interest;
        // 使用TreeSet插入时排序
        Map<Integer, Double> map = new HashMap<>();
        List<Star> stars = starDAO.findByUid(uid);

        // 收藏列表里所有物品最相似的count个物品的全集
        for (Star star: stars) {
            // 复用count
            int[] items = getMostSimilarItems(star.getIid(), count);
            // 累加兴趣值
            for (int item: items) {
                if (!map.containsKey(item)) {
                    interest = similarTable[star.getIid()][item] * weight;
                } else {
                    interest = map.get(item) + similarTable[star.getIid()][item] * weight;
                }
                map.put(item, interest);
            }
        }

        // 获取兴趣最大的count个item（iid）
        int[] arr = new int[count];
        int indexOfMin = 0;
        double minOfArr = 0;

        for (Map.Entry<Integer, Double> entry: map.entrySet()) {
            if (entry.getValue() > minOfArr) {
                arr[indexOfMin] = entry.getKey();

                // 重新计算min和indexOfMin
                minOfArr = Double.MAX_VALUE;
                for (int i = 0; i < count; i++) {
                    if (arr[i] < minOfArr) {
                        minOfArr = arr[i];
                        indexOfMin = i;
                    }
                }
            }
        }
        return arr;
    }

    // 获取和某物品最相似的count个物品
    private int[] getMostSimilarItems(int iid, int count) {
        MostSimilarGroup group = new MostSimilarGroup(count);
        for (int i = 1; i <= tableSize; i++) {
            if (similarTable[iid][i] > group.min) {
                // 插入后会重新计算最小值
                group.insert(iid, i);
            }
        }
        return group.arr;
    }

    private class MostSimilarGroup {
        int size;
        int[] arr;
        int indexOfMin;
        double min;

        MostSimilarGroup(int size) {
            this.size = size;
            this.arr = new int[size];
            this.indexOfMin = 0;
            this.min = 0;
        }

        private void calcuMin(int iid) {
            min = Double.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                double value = similarTable[iid][arr[i]];
                if (value < min) {
                    indexOfMin = i;
                    min = value;
                }
            }
        }

        void insert(int iid, int i) {
            arr[indexOfMin] = i;
            calcuMin(iid);
        }
    }
}
