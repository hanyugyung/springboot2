package org.bwyou.springboot2.viewmodels;

import lombok.Data;

@Data
public class ValidationObjectError {

	private String objectName;
	private String errorMessage;
	
	public ValidationObjectError()
    {

    }
	
	public ValidationObjectError(String objectName, String errorMessage)
    {
        this.objectName = objectName;
        this.errorMessage = errorMessage;
    }
}
