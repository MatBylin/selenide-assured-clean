package org.matbylin.api.validators;

import org.matbylin.api.core.ApiResponse;

/**
 * Fluent validator for responses without a typed body (e.g. {@link ApiResponse<Void>})
 * or when checks should be limited to status, headers, and raw/empty body.
 */
public final class ResponseValidator extends Validator<ResponseValidator> {

    private ResponseValidator(ApiResponse<?> response) {
        super(response);
    }

    public static ResponseValidator validate(ApiResponse<?> response) {
        return new ResponseValidator(response);
    }
}
