package com.zhao.zhaopicturebacked.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zhao.zhaopicturebacked.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String userAccount;
    private String userName;
    private String userIntroduction;
    private String userAvatar;
    private Integer userType;
    private Date createTime;
    private Date updateTime;
    private Date EditTime;

    /**
     * 封装类转对象
     *
     * @param
     * @return
     */
    public static User voToObj(UserVO userVO) {
        if (userVO == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        return user;
    }

    /**
     * 对象转封装类
     *
     * @param
     * @return
     */
    public static UserVO objToVo(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
