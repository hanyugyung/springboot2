package org.bwyou.springboot2.exceptions;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bwyou.springboot2.viewmodels.ErrorResultViewModel;
import org.hibernate.cfg.annotations.Nullability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

public class BWExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(BWExceptionHandler.class);

	public static class Config {
		final List<String> apiUriAntPatterns;
		final String webExceptionViewName;

		public Config(String webExceptionViewName, List<String> apiUriAntPatterns) {
			this.apiUriAntPatterns = apiUriAntPatterns;
			this.webExceptionViewName = webExceptionViewName;
		}

		public List<String> getApiUriAntPatterns() {
			return apiUriAntPatterns;
		}

		public String getWebExceptionViewName() {
			return webExceptionViewName;
		}
	}

	protected final Config config;
	AntPathMatcher antPathMatcher = new AntPathMatcher();

	public BWExceptionHandler(Config config) {
		this.config = config;
	}

	protected Object handleException(HttpServletRequest request, HttpServletResponse response, Locale locale,
			Exception exraw) {

		WebException ex = getWebException(exraw);

		String requestUri = getRequestUri(request);

		logger.warn(String.format("%s %s [locale : %s] Exception Cathed! [Exception(%s) : %s]", request.getMethod(), requestUri, locale, ex.getBody().getStatus(), ex.getBody().getMessage()));

		if (isApiUri(requestUri)) { // api 여부는 url로 확인 한다.
			return returnMV4ApiWebException(ex);
		} else {
			return returnMV4WebException(ex);
		}
	}

	protected ModelAndView returnMV4WebException(WebException ex) {

		// response.setStatus(ex.getBody().getStatus()); 쓰면 안 됨~

		ModelAndView mv = new ModelAndView();
		mv.addObject("exception", ex);
		mv.addObject("errorResultViewModel", new ErrorResultViewModel(ex));
		mv.setViewName(config.getWebExceptionViewName());

		return mv;
	}

	protected ResponseEntity<Object> returnMV4ApiWebException(WebException ex) {
		return new ResponseEntity<Object>(new ErrorResultViewModel(ex), HttpStatus.valueOf(ex.getBody().getStatus()));
	}


	private boolean isApiUri(String requestUri) {
		List<String> apiUriAntPatterns = config.getApiUriAntPatterns();

		if (apiUriAntPatterns == null) {
			return false;
		}
		for (String apiUriAntPattern : apiUriAntPatterns) {
			if (true == antPathMatcher.match(apiUriAntPattern, requestUri)) {
				return true;
			}
		}
		return false;
	}
	
	private String getRequestUri(HttpServletRequest request)
	{
		// error-page 등으로 포워딩 되는 경우 원래 uri 확인 하기 위하여 사용
		String requestUri = (String) request.getAttribute("javax.servlet.forward.request_uri");

		if (requestUri == null) {
			requestUri = request.getRequestURI();
		}
		return requestUri;
	}
	
	private WebException getWebException(Exception exraw) {
		if (exraw instanceof WebException) {
			return (WebException) exraw;
		}
		
		Optional<MvcException> mvcException = MvcException.fromText(exraw.getClass().getSimpleName());
		if(mvcException.isPresent()) {
			return mvcException.get().getMvcException(exraw);
		}else {
			return new WebException(exraw);
		}

		
	}
}
