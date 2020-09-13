package cn.marwin.adminsystem.facade;

public class HttpResult {
    private int status;
    private String message;
    private Object data;
    public final static int SUCCESS = 1;
    public final static int ERROR = 0;

    public HttpResult() {}

    public HttpResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpResult(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public HttpResult(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
