package org.matbylin.core.test.validators;

import io.qameta.allure.Step;
import org.matbylin.core.pdf.PdfReader;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;


public class PdfValidator {
    private final String content;

    private PdfValidator(String content) {
        this.content = content;
    }

    public static PdfValidator validate(File pdfFile) {
        var pdfContent = PdfReader.readPdf(pdfFile);
        return new PdfValidator(pdfContent);
    }

    public static PdfValidator validate(byte[] pdfFile) {
        var pdfContent = PdfReader.readPdf(pdfFile);
        return new PdfValidator(pdfContent);
    }

    @Step("Validating that PDF file contains strings: {texts}")
    public PdfValidator pdfContainsText(String... texts) {
        for (var text : texts) {
            assertThat(content, containsString(text));
        }
        return this;
    }
}
