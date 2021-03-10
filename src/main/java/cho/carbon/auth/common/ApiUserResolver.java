package cho.carbon.auth.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


import cho.carbon.bond.utils.TextUtils;
import cho.carbon.spring.security.NonAuthorityException;
import cho.carbon.spring.security.pojo.ApiUser;
import cho.carbon.spring.security.pojo.Token;
import cho.carbon.spring.security.pojo.UserWithToken;
import cho.carbon.spring.security.service1.AdminUserService;

@Component
public class ApiUserResolver implements HandlerMethodArgumentResolver{

	@Resource
	AdminUserService uService;
	
	static Logger logger = LoggerFactory.getLogger(ApiUserResolver.class);
	
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramClass = parameter.getParameterType();
		if(ApiUser.class.isAssignableFrom(paramClass)){
			return true;
		}
		return false;
	}

	@Override
	public ApiUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String tokenCode = webRequest.getParameter("@token");
		if(!TextUtils.hasText(tokenCode)) {
			tokenCode = webRequest.getHeader("hydrocarbon-token");
		}
		if(!TextUtils.hasText(tokenCode)) {
			tokenCode = webRequest.getHeader("datamobile-token");
		}
		String userName= webRequest.getHeader("userName");
		String tokenTime= webRequest.getHeader("tokenTime");
		
		if(!TextUtils.hasText(tokenCode)) {
			if(webRequest.getSessionMutex() instanceof HttpSession) {
				HttpSession session = (HttpSession) webRequest.getSessionMutex();
				tokenCode = (String) session.getAttribute(SessionKey.API_USER_TOKEN);
			}
		}
		if(TextUtils.hasText(tokenCode)) {
			try {
				Token token = uService.validateToken(tokenCode,userName,tokenTime);
				token.refreshDeadline();
				UserWithToken user = token.getUser();
				if(user != null) {
					UserUtils.setCurrentUserCode(user.getCode());
				}
				return user;
			} catch (Exception e) {
				logger.error("验证用户token时发生异常", e);
			}
		}
			throw new NonAuthorityException("没有权限");
		
	}

}
