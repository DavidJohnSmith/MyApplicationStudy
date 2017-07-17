package com.myapp.study.net.exception;


/**
 * 进行数据解析异常  如 框架解析字符串 成为json 时候 或者 是 个人 在解析json时候抛出的异常 如果  在自己定义的解析异常的时候 要继承这个异常。
 *
 * @author lin
 */
public class ResolveException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public ResolveException() {
        super();
    }

    public ResolveException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ResolveException(String arg0) {
        super(arg0);
    }

    public ResolveException(Throwable arg0) {
        super(arg0);
    }

}
