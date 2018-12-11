package dk.nykredit.infra.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomException extends RuntimeException {

    public CustomException(String msg) {
        super(msg);
    }
}
