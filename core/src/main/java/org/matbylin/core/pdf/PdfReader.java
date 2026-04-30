package org.matbylin.core.pdf;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@UtilityClass
public class PdfReader {
    public String readPdf(File file) {
        Objects.requireNonNull(file, "[PdfReader] Provided file is null!");
        log.info("Reading content from pdf file: {}", file);
        return read(() -> Loader.loadPDF(file), "file: " + file);
    }

    public String readPdf(byte[] bytes) {
        Objects.requireNonNull(bytes, "[PdfReader] Provided bytes are null!");
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Provided bytes are empty!");
        }
        log.info("Reading content from pdf bytes");
        return read(() -> Loader.loadPDF(bytes), "bytes");
    }

    private String read(ThrowingSupplier<PDDocument> supplier, String sourceDescription) {
        try (PDDocument document = supplier.get()) {
            return new PDFTextStripper().getText(document);
        } catch (IOException e) {
            log.error("Failed to read pdf {}", sourceDescription, e);
            throw new IllegalStateException(
                    "Problem while reading pdf content from " + sourceDescription, e
            );
        }
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get() throws IOException;
    }
}
