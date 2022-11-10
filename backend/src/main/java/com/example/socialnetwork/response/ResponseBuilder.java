package com.example.socialnetwork.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for creating {@link ResponseEntity} objects using the builder pattern.
 */
public class ResponseBuilder {
    private ResponseBuilder() {}

    /**
     * @see Builder#data(Object) Builder.data()
     */
    public static Builder data(Object data) {
        return new Builder().data(data);
    }

    /**
     * @see Builder#status(HttpStatus) Builder.status()
     */
    public static Builder status(HttpStatus status) {
        return new Builder().status(status);
    }

    /**
     * @see Builder#error(Error) Builder.error()
     */
    public static Builder error(Error error) {
        return new Builder().error(error);
    }

    /**
     * Builder class.
     */
    public static class Builder {
        private Object data;
        private String reason;
        private HttpStatus status = HttpStatus.OK;

        private Builder() {}

        /**
         * Updates the data field.
         * @param data the new data.
         * @return a reference to this instance.
         */
        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        /**
         * Updates the status field. Default is {@link HttpStatus#OK}.
         * @param status the new status.
         * @return a reference to this instance.
         */
        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Updates the reason and status field according to the {@link Error}.
         * @param error the {@link Error}.
         * @return a reference to this instance.
         */
        public Builder error(Error error) {
            this.reason = error.getReason();
            this.status = error.getStatus();
            return this;
        }

        /**
         * Builds a {@link ResponseEntity} by the preset fields in this instance.
         * @return a new {@link ResponseEntity}.
         */
        public ResponseEntity<Map<String, Object>> build() {
            Map<String, Object> map = new HashMap<>();
            if (data != null) {
                map.put("data", data);
            }
            if (reason != null) {
                map.put("reason", reason);
            }

            return new ResponseEntity<>(map, status);
        }
    }
}
