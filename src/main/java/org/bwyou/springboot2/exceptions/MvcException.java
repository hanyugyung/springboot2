package org.bwyou.springboot2.exceptions;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;

public enum MvcException {
	HttpRequestMethodNotSupportedException {
		@Override
		WebException getMvcException(Exception exraw) {
			return new WebException(HttpStatus.METHOD_NOT_ALLOWED, exraw);
		}
	},
	HttpMediaTypeNotSupportedException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new WebException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exraw);
		}
	},
	HttpMediaTypeNotAcceptableException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new WebException(HttpStatus.NOT_ACCEPTABLE, exraw);
		}
	},
	MissingPathVariableException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new WebException(HttpStatus.INTERNAL_SERVER_ERROR, exraw);
		}
	},
	MissingServletRequestParameterException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new BadRequestWebException(exraw);
		}
	},
	ServletRequestBindingException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new BadRequestWebException(exraw);
		}
	},
	ConversionNotSupportedException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new WebException(HttpStatus.INTERNAL_SERVER_ERROR, exraw);
		}
	},
	TypeMismatchException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new BadRequestWebException(exraw);
		}
	},
	HttpMessageNotReadableException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new BadRequestWebException(exraw);
		}
	},
	HttpMessageNotWritableException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new WebException(HttpStatus.INTERNAL_SERVER_ERROR, exraw);
		}
	},
	MethodArgumentNotValidException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new BadRequestWebException(exraw);
		}
	},
	MissingServletRequestPartException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new BadRequestWebException(exraw);
		}
	},
	NoHandlerFoundException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new NotFoundWebException();
		}
	},
	AsyncRequestTimeoutException{
		@Override
		WebException getMvcException(Exception exraw) {
			return new WebException(HttpStatus.SERVICE_UNAVAILABLE, exraw);
		}
	};
	
	public static Optional<MvcException> fromText(String text) {
        return Arrays.stream(values())
          .filter(mvcExeption -> mvcExeption.name().equalsIgnoreCase(text))
          .findFirst();
    }
	 abstract WebException getMvcException(Exception exraw); 
}
