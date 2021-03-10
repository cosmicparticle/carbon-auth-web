package cho.carbon.auth.common;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import cho.carbon.spring.security.pojo.ABCUser;

public class UserUtils {
	private static ThreadLocal<String> threadUserCode = new ThreadLocal();
	/**
	 * 获得当前登录的用户对象
	 * @param userClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentUser(Class<T> userClass){
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		
		if(authen instanceof AnonymousAuthenticationToken) {
			// 匿名用户返回空
			return null;
		}
		
		if(authen != null){
			Object user = authen.getPrincipal();
			return (T) user;
		}
		return null;
	}
	public static void setCurrentUserCode(String userCode){
		threadUserCode.set(userCode);
	}

	public static String getCurrentUserCode(){

		return threadUserCode.get();

	}
	
	/**
	 * 获得当前登录的用户对象（）
	 * @return
	 */
	public static ABCUser getCurrentUser(){
		return getCurrentUser(ABCUser.class);
	}
}
