package de.kaufland.ksilence.exception;

public class EmptyParameterException extends IllegalArgumentException{

    private String parameter;

    public String getParameter() {
        return parameter;
    }

    public EmptyParameterException(String parameter) {
        super(parameter + " must not be null or empty");
        this.parameter = parameter;
    }

}
