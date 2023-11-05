package com.codestates.auth.jwt.custom;

import com.codestates.auth.utils.ErrorResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class UserPermissionAspect {

        @Around("@annotation(CheckUserPermission) && args(memberId, requestBody,..)")
        public Object checkUserPermission(ProceedingJoinPoint joinPoint, Long memberId, Object requestBody) throws Throwable {
            // 인증 로직
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof Map)) {
                throw new ErrorResponse("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
            }

            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            long jwtMemberId = ((Number) principal.get("memberId")).longValue();

            Long memberId2 = 0L;
            if(requestBody instanceof IdentifiableMember)
                memberId2 = (((IdentifiableMember) requestBody).getMemberIdForAuth());

            // 인증 실패 시 예외 발생
            if(jwtMemberId != memberId || (memberId2 != 0L && jwtMemberId != memberId2))
                throw new ErrorResponse("권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN);

            // 다음 메소드 체인으로 진행
            return joinPoint.proceed();
        }


    @Around("@annotation(CheckUserPermission) && args(requestBody,..)")
    public Object checkUserPermission(ProceedingJoinPoint joinPoint, Object requestBody) throws Throwable {
        // 인증 로직
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Map)) {
            throw new ErrorResponse("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        Long memberId2 = 0L;
        if(requestBody instanceof IdentifiableMember)
            memberId2 = (((IdentifiableMember) requestBody).getMemberIdForAuth());

        // 인증 실패 시 예외 발생
        if((memberId2 != 0L && jwtMemberId != memberId2))
            throw new ErrorResponse("권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN);

        // 다음 메소드 체인으로 진행
        return joinPoint.proceed();
    }
}
