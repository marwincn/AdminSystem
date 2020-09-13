package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.util.FileUtil;
import cn.marwin.adminsystem.facade.HttpResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("file")
public class FileController {
    @ApiOperation(value = "上传图片")
    @RequestMapping(value = "upPicture", method = RequestMethod.POST)
    public HttpResult upPicture(MultipartFile file) {
        String url;
        try {
            url = FileUtil.uploadPic(file);
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpResult(HttpResult.ERROR, "上传文件失败！");
        }
        return new HttpResult(HttpResult.SUCCESS, "上传文件成功！", url);
    }

    @ApiOperation(value = "删除图片")
    @RequestMapping(value = "delPicture", method = RequestMethod.POST)
    public HttpResult delPicture(String url) {

        // 验证是否是本人操作
        FileUtil.deleteFile(url);
        return new HttpResult(HttpResult.SUCCESS, "删除图片成功！");
    }
}
