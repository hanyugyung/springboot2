package org.bwyou.springboot2.viewmodels;


import java.util.ArrayList;
import java.util.List;

import org.bwyou.springboot2.exceptions.WebException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import lombok.Getter;

@Getter
public class ErrorResultViewModel {
	private WebStatusMessageBody error;
	private List<ValidationObjectError> modelValidResult;
	
	public ErrorResultViewModel(HttpStatus httpStatusCode, String message)
    {
		error = new WebStatusMessageBody("E" + String.format("%03d", httpStatusCode.value()), message, "", "");
		error.setStatus(httpStatusCode.value());
    }

    public ErrorResultViewModel(WebException webException)
    {
		error = webException.getBody();
		if (webException.getBindingResult() != null) {
			modelValidResult = GetValidationObjectErrors(webException.getBindingResult());
		}
    }

    public ErrorResultViewModel(HttpStatus httpStatusCode, BindingResult bindingResult)
    {
		error = new WebStatusMessageBody("E" + String.format("%03d", httpStatusCode.value())
				, "Validation Fail", "", "");
		error.setStatus(httpStatusCode.value());
        modelValidResult = GetValidationObjectErrors(bindingResult);
    }

    public ErrorResultViewModel(BindingResult bindingResult)
    {
    	this(HttpStatus.BAD_REQUEST, bindingResult);
    }

    public static List<ValidationObjectError> GetValidationObjectErrors(BindingResult bindingResult)
    {
        List<ValidationObjectError> validationObjectErrors = new ArrayList<ValidationObjectError>();
        
		if (bindingResult.hasErrors()) {
			
			List<ObjectError> list = bindingResult.getAllErrors();
			
			for (ObjectError e : list) {
				if (e instanceof FieldError) {
					validationObjectErrors.add(new ValidationObjectError(e.getObjectName() + "." + ((FieldError)e).getField(), e.getDefaultMessage()));
				}
				else {
					validationObjectErrors.add(new ValidationObjectError(e.getObjectName(), e.getDefaultMessage()));
				}
			}
			
		}

        return validationObjectErrors;
    }
}

