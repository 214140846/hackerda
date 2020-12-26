package com.hackerda.platform.controller;

import com.hackerda.platform.exception.BusinessException;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpEvaluationException;
import com.hackerda.spider.exception.UrpException;
import com.hackerda.spider.exception.UrpRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author junrong.chen
 * @date 2018/10/10
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {


	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public WebResponse<Void> handle401() {
		return WebResponse.failUnauthorized("用户未授权");
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(UnauthenticatedException.class)
	public WebResponse<Void> handle403(UnauthenticatedException e) {
		return WebResponse.failWithForbidden("身份认证失败");
	}

	@ExceptionHandler(BusinessException.class)
	public WebResponse<Void> handleCommon(BusinessException e) {
		return WebResponse.fail(e.getErrorCode().getErrorCode(), e.getMsg());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public WebResponse<Void> handleCommon(MissingServletRequestParameterException e) {
		return WebResponse.fail(ErrorCode.DATA_NOT_VALID.getErrorCode(), e.getMessage());
	}

	@ExceptionHandler(ClientAbortException.class)
	public WebResponse<Void> brokePipe(MissingServletRequestParameterException e) {
		return WebResponse.fail(ErrorCode.SYSTEM_ERROR.getErrorCode(), e.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	public WebResponse<Void> handleException(Exception e) {
		if (e instanceof PasswordUnCorrectException) {
			return WebResponse.fail(ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode(), e.getMessage());
		}else if(e instanceof UrpRequestException) {
			return WebResponse.fail(ErrorCode.LOGIN_ERROR.getErrorCode(), "can`t verify user");
		}else if(e instanceof UrpEvaluationException) {
			return WebResponse.fail(ErrorCode.Evaluation_ERROR.getErrorCode(), "未完成评估");
		}else if(e instanceof UrpException || e instanceof ResourceAccessException){
			return WebResponse.fail(ErrorCode.URP_EXCEPTION.getErrorCode(), "教务网异常，请重试");
		}
		log.error("request fail", e);

		return WebResponse.fail(ErrorCode.SYSTEM_ERROR.getErrorCode(), "服务器出了点小问题");
	}
}
