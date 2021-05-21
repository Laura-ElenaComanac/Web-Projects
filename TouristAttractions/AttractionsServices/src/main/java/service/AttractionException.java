package service;

public class AttractionException extends RuntimeException{
    public AttractionException(){

    }
    public AttractionException(String message) {
        super(message);
    }
    public AttractionException(String message, Throwable cause){
        super(message,cause);
    }

}
