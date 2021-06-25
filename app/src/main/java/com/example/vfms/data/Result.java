package com.example.vfms.data;

import org.jetbrains.annotations.NotNull;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
@SuppressWarnings("unused")
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private Result() {
    }

    @NotNull
    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            @SuppressWarnings("rawtypes") Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    @SuppressWarnings("rawtypes")
    public final static class Success<T> extends Result {
        @SuppressWarnings("FieldMayBeFinal")
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    @SuppressWarnings("rawtypes")
    public final static class Error extends Result {
        @SuppressWarnings("FieldMayBeFinal")
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }

    //Fail sub-class
    @SuppressWarnings("rawtypes")
    public final static class Fail extends Result {
        @SuppressWarnings("FieldMayBeFinal")
        private int code;

        public Fail(int code) {
            this.code = code;
        }

        public int getFail() {
            return this.code;
        }
    }
}