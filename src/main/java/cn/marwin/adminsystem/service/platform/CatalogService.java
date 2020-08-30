package cn.marwin.adminsystem.service.platform;

import cn.marwin.adminsystem.entity.platform.Catalog;
import cn.marwin.adminsystem.repository.platform.CatalogDAO;
import cn.marwin.adminsystem.repository.platform.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {
    @Autowired
    CatalogDAO catalogDAO;
    @Autowired
    ItemDAO itemDAO;

    public Catalog add(Catalog catalog) {
        return catalogDAO.save(catalog);
    }

    public Catalog update(Catalog catalog) {
        return catalogDAO.save(catalog);
    }

    public Catalog findById(Integer id) {
        return catalogDAO.findById(id).orElse(null);
    }

    public List<Catalog> findAll() {
        return catalogDAO.findAll();
    }

    public void delete(Integer id) throws Exception {
        if (itemDAO.existsByCid(id)) {
            throw new Exception("请确认该分类下无项目，再进行尝试！");
        }
        catalogDAO.deleteById(id);
    }
}
