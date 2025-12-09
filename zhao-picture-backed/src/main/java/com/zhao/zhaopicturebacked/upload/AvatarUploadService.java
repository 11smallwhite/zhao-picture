package com.zhao.zhaopicturebacked.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.zhao.zhaopicturebacked.constant.UserConstant;
import com.zhao.zhaopicturebacked.cos.CosService;
import com.zhao.zhaopicturebacked.domain.User;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.model.UserVO;
import com.zhao.zhaopicturebacked.service.UserService;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 头像上传服务
 */
@Slf4j
@Service
public class AvatarUploadService {

    @Resource
    private CosService cosService;
    
    @Resource
    private UserService userService;

    /**
     * 上传用户头像
     * @param multipartFile 头像文件
     * @param  request
     * @return 头像URL
     */
    public String uploadAvatar(MultipartFile multipartFile, HttpServletRequest request) {
        // 1. 校验文件
        vailAvatarFile(multipartFile);
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STORE);
        UserVO loginUserVO = (UserVO) attribute;
        // 2. 生成文件key
        String key = generateAvatarKey(multipartFile, loginUserVO.getId());
        
        // 3. 上传到COS
        String avatarUrl = uploadToCos(multipartFile, key);
        
        // 4. 更新用户信息
        User user = new User();
        user.setId(loginUserVO.getId());
        user.setUserAvatar(avatarUrl);
        user.setEditTime(new Date());
        boolean b = userService.updateById(user);
        if (!b) {
            log.info("更新用户信息失败");
            ThrowUtil.throwBusinessException(CodeEnum.SYSTEM_ERROR, "更新用户信息失败");
        }
        return avatarUrl;
    }

    /**
     * 校验头像文件
     * @param multipartFile
     */
    private void vailAvatarFile(MultipartFile multipartFile) {
        // 1. 文件不能为空
        if (multipartFile.isEmpty()) {
            log.info("头像文件为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR, "不允许上传空文件");
        }
        
        // 2. 文件大小不能超过1M
        long size = multipartFile.getSize();
        if (size > 1024 * 1024) {
            log.info("头像大小不能超过1M");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR, "头像大小不能超过1M");
        }
        
        // 3. 允许的图片格式
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        // 白名单，允许的图片后缀
        if (!"png".equalsIgnoreCase(suffix) && !"jpg".equalsIgnoreCase(suffix) 
                && !"jpeg".equalsIgnoreCase(suffix)) {
            log.info("头像格式错误");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR, "头像格式错误，仅支持png、jpg、jpeg格式");
        }
    }

    /**
     * 生成头像存储key
     * @param multipartFile
     * @param userId
     * @return
     */
    private String generateAvatarKey(MultipartFile multipartFile, Long userId) {
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        // 存储在avatar目录下，文件名为 用户ID + 时间戳 + 后缀
        return String.format("/avatar/%s_%d.%s", userId, System.currentTimeMillis(), suffix);
    }

    /**
     * 上传到COS
     * @param multipartFile
     * @param key
     * @return
     */
    private String uploadToCos(MultipartFile multipartFile, String key) {
        File file = null;
        try {
            // 将MultipartFile转换为File
            file = FileUtil.createTempFile();
            multipartFile.transferTo(file);
            
            // 直接上传，不进行图片处理
            PutObjectResult result = cosService.putPicture(key, file);
            
            // 返回访问URL
            return cosService.getHost() + "/" + key;
        } catch (IOException e) {
            log.error("头像上传失败", e);
            ThrowUtil.throwBusinessException(CodeEnum.SYSTEM_ERROR, "头像上传失败");
        } finally {
            // 删除临时文件
            if (file != null && file.exists()) {
                FileUtil.del(file);
            }
        }
        return null;
    }


}