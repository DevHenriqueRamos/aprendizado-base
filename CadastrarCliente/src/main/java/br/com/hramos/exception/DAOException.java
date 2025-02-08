package main.java.br.com.hramos.exception;

public class DAOException extends Exception {
    public DAOException(String msg, Exception ex) {
        super(msg, ex);
    }
}
