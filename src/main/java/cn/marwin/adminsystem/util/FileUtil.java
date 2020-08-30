package cn.marwin.adminsystem.util;

import cn.marwin.adminsystem.facade.FileFacade;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtil {
    // 服务器上图片存储位置
    public static String location;
    // 服务器url前缀
    public static String context;

    @Value("${file.context}")
    public void setContext(String context) {
        FileUtil.context = context;
    }

    @Value("${file.location}")
    public void setLocation(String location) {
        FileUtil.location = location;
    }

    public static boolean checkPicType(MultipartFile file) {
        if (file != null) {
            String name = file.getOriginalFilename();
            int split = name.lastIndexOf(".");
            String suffix = name.substring(split + 1).toLowerCase();
            // 白名单
            if (suffix.equals("jpg") || suffix.equals("jpeg") || suffix.equals("png") || suffix.equals("gif")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String uploadPic(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        int split = name.lastIndexOf(".");
        String suffix = name.substring(split);
        String filename = UUID.randomUUID() + suffix;

        File path = new File(location);
        if (!path.exists()) {
            path.mkdirs();
        }
        file.transferTo(new File(path, filename));
        // 返回相对路径
        return context + filename;
    }

    // 对上传的头像进行压缩
    public static String uploadAvatar(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        int split = name.lastIndexOf(".");
        String suffix = name.substring(split);
        String filename = UUID.randomUUID() + suffix;

        File path = new File(location);
        if (!path.exists()) {
            path.mkdirs();
        }
        Thumbnails.of(file.getInputStream())
                .size(500, 500)
                .toFile(new File(path, filename));
        // 返回相对路径
        return context + filename;
    }

    // 单独压缩图片
    public static void compPic(String url, double scale) throws IOException {
        File file = new File(location, url.substring(context.length()));
        Thumbnails.of(file)
                .scale(scale)
                .toFile(file);
    }

    public static int getFileCount() {
        File file = new File(location);
        if (!file.exists()) {
            return 0;
        }
        if (!file.isDirectory()) {
            return 0;
        }
        String[] files = file.list();
        return files.length;
    }

    public static List<FileFacade> getFileList(Integer currPage, Integer pageSize) {
        File file = new File(location);
        if (!file.exists()) {
            return null;
        }
        if (!file.isDirectory()) {
            return null;
        }
        if (currPage <= 0 || pageSize <= 0) {
            return null;
        }

        String[] files = file.list();
        int start = (currPage - 1) * pageSize;
        int end = start + pageSize;
        if (start > (files.length - 1)) {
            return null;
        }
        if (end > files.length) {
            end = files.length;
        }

        List<FileFacade> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            File newFile = new File(location + "/" + files[i]);
            if (newFile.isFile()) {
                result.add(new FileFacade(newFile));
            }
        }
        return result;
    }

    public static void deleteFile(String url) {
        File file = new File(location, url.substring(context.length()));
        if (file.exists()) {
            file.delete();
        }
    }

}
