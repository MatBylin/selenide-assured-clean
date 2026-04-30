package org.matbylin.gui.tests.filedownload;

import io.qameta.allure.Story;
import org.matbylin.core.test.validators.PdfValidator;
import org.matbylin.gui.groups.Tag;
import org.matbylin.gui.tests.BaseGuiTest;
import org.testng.annotations.Test;

@Story("File Download")
public class FileDownloadTests extends BaseGuiTest {
    @Test(groups = {Tag.SMOKE})
    void validateContentOfPdfFile() {
        var fileDownloadPage = dashboardPage
                .goToFileDownloadPage();

        var pdfFile = fileDownloadPage.downloadPdfFile();
        PdfValidator.validate(pdfFile)
                .pdfContainsText("This is test file.");
    }
}
