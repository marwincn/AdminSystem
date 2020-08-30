package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.util.FileUtil;
import cn.marwin.adminsystem.facade.Result;
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
    public Result upPicture(MultipartFile file) {
        String url;
        try {
            url = FileUtil.uploadPic(file);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(Result.ERROR, "上传文件失败！");
        }
        return new Result(Result.SUCCESS, "上传文件成功！", url);
    }

    @ApiOperation(value = "删除图片")
    @RequestMapping(value = "delPicture", method = RequestMethod.POST)
    public Result delPicture(String url) {

        // 验证是否是本人操作
        FileUtil.deleteFile(url);
        return new Result(Result.SUCCESS, "删除图片成功！");
    }
}
