package com.whatsoeversky.test.aspect;

import com.whatsoeversky.test.annotation.ValidateResource;
import com.whatsoeversky.test.exception.ResourceAccessException;
import com.whatsoeversky.test.exception.ServiceException;
import com.whatsoeversky.test.repository.UserInfoRepository;
import com.whatsoeversky.test.support.UserInfoContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Aspect
@Component
public class ValidateResourceAspect {
    @Resource
    private UserInfoRepository userInfoRepository;

    @Pointcut("@annotation(com.whatsoeversky.test.annotation.ValidateResource)")
    public void pointcut() {

    }


    @Before(value = "pointcut() && @annotation(validateResource)")
    public void before(JoinPoint joinPoint,
                       ValidateResource validateResource) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        int index = -1;
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                if (Objects.equals(parameterName, validateResource.resourceKey())) {
                    index = i;
                    break;
                }
            }
        }
        if (index >= 0) {
            if (!userInfoRepository.isUserIdDataAccessible(UserInfoContext.getUserId(), joinPoint.getArgs()[index].toString())) {
                throw new ResourceAccessException("resource cannot be accessed");
            }
        } else {
            throw new ServiceException("resource key config error");
        }

    }

}
