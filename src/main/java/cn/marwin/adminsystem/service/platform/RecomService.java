package cn.marwin.adminsystem.service.platform;

import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.recommend.ItemCF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecomService {
    @Autowired
    ItemService itemService;

    private ItemCF getItemCF() {
        ItemCF itemCF = new ItemCF();
        itemCF.initTable();
        itemCF.initMatrix();
        itemCF.calcuSimilarity();
        itemCF.normSimilarity();
        return itemCF;
    }

    public List<Item> getRecomItems(int uid, int size) {
        ItemCF itemCF = getItemCF();
        List<Item> items = new ArrayList<>();
        int[] iids = itemCF.getMostInterest(uid, size);
        for (int iid: iids) {
            items.add(itemService.findById(iid));
        }
        return items;
    }
}
