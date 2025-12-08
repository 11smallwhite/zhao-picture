package com.zhao.zhaopicturebacked.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.zhaopicturebacked.domain.SpaceUser;
import com.zhao.zhaopicturebacked.model.SpaceUserVO;
import com.zhao.zhaopicturebacked.request.DeleteRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserAddRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserDeleteRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserEditRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserQueryRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Vip
* @description 针对表【space_user(空间用户关联)】的数据库操作Service
* @createDate 2025-12-06 16:33:48
*/
public interface SpaceUserService extends IService<SpaceUser> {

    //添加成员到空间
    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);
    //从空间移除成员（踢人）
    boolean deleteSpaceUser(SpaceUserDeleteRequest spaceUserDeleteRequest);
    //删除团队空间
    boolean deleteSpaceTeam(DeleteRequest deleteRequest);
    //查询空间成员列表
    List<SpaceUserVO> selectSpaceUserVO(SpaceUserQueryRequest spaceUserQueryRequest);
    //查询某个成员在空间的信息
    SpaceUser getSpaceUser(SpaceUserQueryRequest spaceUserQueryRequest);
    SpaceUserVO getSpaceUserVO(SpaceUserQueryRequest spaceUserQueryRequest);
    //编辑成员信息
    boolean editSpaceUser(SpaceUserEditRequest spaceUserEditRequest);
    //查询我加入的团队空间
    List<SpaceUserVO> selectMySpaceUser(HttpServletRequest request);
    List<SpaceUserVO> listToVOList( List<SpaceUser> spaceUserList);
    QueryWrapper<SpaceUser> getSpaceUserQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);
    void validSpaceUserAdmin(Long userId,Long spaceId);
}
