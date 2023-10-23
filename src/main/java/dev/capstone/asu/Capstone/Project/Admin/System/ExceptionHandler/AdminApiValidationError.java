package dev.capstone.asu.Capstone.Project.Admin.System.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class AdminApiValidationError extends AdminApiSubError{
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public AdminApiValidationError(String object, String message)
    {
        this.object = object;
        this.message = message;
    }
}
