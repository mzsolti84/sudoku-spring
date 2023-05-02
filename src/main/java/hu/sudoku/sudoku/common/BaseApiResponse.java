package hu.sudoku.sudoku.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class BaseApiResponse extends ResponseEntity<Object> {

    private final Map<String, Object> body;

    protected BaseApiResponse(Builder<?> builder) {
        super(builder.body,
                HttpStatus.valueOf((Integer) builder.body.get("statusCode")));
        this.body = builder.body;
    }

    public static Builder<?> builder() {
        return new Builder() {
            @Override
            public Builder getThis() {
                return this;
            }
        };
    }

    public abstract static class Builder<T extends Builder<T>> {

        protected Map<String, Object> body = new HashMap<>();

        public abstract T getThis();

        public T setMessage(String message) {
            this.body.put("message", message);
            return this.getThis();
        }

        public T setStatus(HttpStatus status) {
            this.body.put("statusCode", status.value());
            this.body.put("status", status.name());
            return this.getThis();
        }

        public T setResponseObject(Object responseObject) {
            this.body.put("data", responseObject);
            return this.getThis();
        }

        public BaseApiResponse build() {
            return new BaseApiResponse(this);
        }
    }

}

// https://ducmanhphan.github.io/2020-04-06-how-to-apply-builder-pattern-with-inhertitance/