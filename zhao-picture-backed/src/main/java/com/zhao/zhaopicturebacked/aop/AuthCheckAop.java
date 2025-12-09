package com.zhao.zhaopicturebacked.aop;

import cn.hutool.core.util.ObjUtil;
import com.zhao.zhaopicturebacked.annotation.AuthType;
import com.zhao.zhaopicturebacked.constant.UserConstant;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.model.UserVO;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect//声明这是一个切面类
@Component//将该类交给spring管理
@Slf4j
public class AuthCheckAop {

    @Around("@annotation(authType)")//@Around：环绕通知 表示在方法执行之前进行拦截,
    // @annotation表示只拦截方法，并且它的参数ddddddd与方法里的参数进行匹配，决定了要拦截的注解是什么，permissionCheck方法里的参数ddddddd的类型是AuthType，
    // 这告诉了@annotation要拦截的是拥有AuthType注解的方法
    public Object permissionCheck(ProceedingJoinPoint joinPoint, AuthType authType) throws Throwable {
        //1.拿到HttpServeletRequest，然后通过它拿到用户Type信息
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();// 通过Spring的RequestContextHolder获取当前线程的HTTP请求
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();



        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STORE);
        if (attribute == null){
            log.warn("用户未登录");
            ThrowUtil.throwBusinessException(CodeEnum.NOT_AUTH,"用户未登录");
        }
        UserVO loginUserVO = (UserVO) attribute;
        
        //3.拿到注解上的userType,低于userType的权限不能通过
        int userType = authType.userType();

        if (loginUserVO.getUserType()<userType){
            log.info("注解+AOP权限校验未通过，用户权限不足");
            ThrowUtil.throwBusinessException(CodeEnum.NOT_AUTH,"权限不足");
        }

        return joinPoint.proceed();
    }
}
