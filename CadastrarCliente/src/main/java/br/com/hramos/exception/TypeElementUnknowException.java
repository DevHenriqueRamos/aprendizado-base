package main.java.br.com.hramos.exception;

public class TypeElementUnknowException extends Exception {
    public TypeElementUnknowException(String msg) {
        this(msg, null);
    }

    public TypeElementUnknowException(String msg, Throwable e) {
        super(msg, e);
    }
}
