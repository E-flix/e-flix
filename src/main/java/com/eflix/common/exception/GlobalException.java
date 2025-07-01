package com.eflix.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
		return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CommonException.class)
	public Object handleException(HttpServletRequest request, CommonException ex) {
		if (isAjax(request)) {
			// JSON 응답
			Map<String, Object> error = new HashMap<>();
			error.put("status", "error");
			error.put("message", ex.getMessage());

			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			// 뷰 반환 (일반 페이지 요청)
			ModelAndView mav = new ModelAndView("error");
			mav.addObject("message", ex.getMessage());
			return mav;
		}
	}

	private boolean isAjax(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(header);
	}
}
