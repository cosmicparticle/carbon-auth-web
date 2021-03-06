package cho.carbon.auth.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cho.carbon.spring.security.NonAuthorityException;


@ControllerAdvice
public class HydrocarbonControllerExceptionHandler {
	
	
	Logger logger = LoggerFactory.getLogger(HydrocarbonControllerExceptionHandler.class);
	
	@ResponseBody
    @ExceptionHandler(NonAuthorityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAuthorityException(NonAuthorityException ex) {
		logger.error("访问权限不足", ex);
		return "Forbidden";
    }
	
	@ResponseBody
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String handleAccessDeniedException(AccessDeniedException ex) {
		logger.error("访问权限不足", ex);
		return "AccessDenied";
	}
		
}
