package org.matbylin.api.service.pdf;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.api.config.TargetApi;
import org.matbylin.api.config.auth.TokenProvider;
import org.matbylin.api.core.ApiRequest;
import org.matbylin.api.core.ApiResponse;
import org.matbylin.api.service.BaseApi;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;

@Slf4j
public class PdfApi extends BaseApi {

    private static final String APPLICATION_PDF = "application/pdf";

    public PdfApi() {
        super(new TokenProvider());
    }

    @Step("GET pdf /docs/{filename}")
    public ApiResponse<byte[]> getPdfFile(String filename) {
        log.info("GET request to /docs/{}", filename);
        var request = ApiRequest.builder()
                .targetApi(TargetApi.QA_PLAYGROUND)
                .header(CONTENT_TYPE, APPLICATION_PDF)
                .pathParam("filename", filename)
                .path("/docs/{filename}")
                .build();

        return restExecutor.get(request, byte[].class);
    }
}
