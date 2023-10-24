package dev.capstone.asu.Capstone.Project.Admin.System.ExceptionHandler;

public class AdminApiRequestError extends AdminApiSubError {
    private String object;
    private String field;
    private Object pathVariable;
    private String message;

    public AdminApiRequestError(String message)
    {
        this.message = message;
    }

    public AdminApiRequestError(String object, String field, Object pathVariable, String message)
    {
        this.object = object;
        this.field = field;
        this.pathVariable = pathVariable;
        this.message = message;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getPathVariable() {
        return pathVariable;
    }

    public void setPathVariable(Object pathVariable) {
        this.pathVariable = pathVariable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
