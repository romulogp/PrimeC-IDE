package br.com.primec.core.exception;

import br.com.primec.core.exception.AnalysisError;

public class SyntaticError extends AnalysisError {

    public SyntaticError(String msg, int position) {
        super(msg, position);
    }

    public SyntaticError(String msg) {
        super(msg);
    }
}
