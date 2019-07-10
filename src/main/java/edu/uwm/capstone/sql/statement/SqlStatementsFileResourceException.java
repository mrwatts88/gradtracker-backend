package edu.uwm.capstone.sql.statement;

import org.springframework.core.NestedRuntimeException;

public class SqlStatementsFileResourceException extends NestedRuntimeException {

    private static final long serialVersionUID = 1551532974211387214L;

    public SqlStatementsFileResourceException(String msg) {
        super(msg);
    }

    public SqlStatementsFileResourceException(String msg, Throwable e) {
        super(msg, e);
    }

}
