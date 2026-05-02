package org.matbylin.api.tests.pdf;

import org.matbylin.api.service.pdf.PdfApi;
import org.matbylin.api.tests.BaseApiTest;
import org.matbylin.core.groups.Tag;
import org.matbylin.core.test.validators.PdfValidator;
import org.testng.annotations.Test;

public class PdfApiTests extends BaseApiTest {

    @Test(groups = {Tag.SMOKE})
    void validatePdfContent() {
        var pdfFile = new PdfApi().getPdfFile("test.pdf");

        PdfValidator.validate(pdfFile.getBody())
                .pdfContainsText("This is test file.");
    }
}
