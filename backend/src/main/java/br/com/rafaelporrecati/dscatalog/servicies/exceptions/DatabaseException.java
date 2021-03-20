package br.com.rafaelporrecati.dscatalog.servicies.exceptions;

public class DatabaseException extends RuntimeException{

    public DatabaseException(String msg) {
        super(msg);
    }
}
