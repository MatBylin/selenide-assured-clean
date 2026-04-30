package org.matbylin.gui.pages.qaplayground.filedownload;

import io.qameta.allure.Step;
import lombok.Getter;
import org.matbylin.gui.core.components.generic.Footer;
import org.matbylin.gui.core.components.generic.TopBar;
import org.matbylin.gui.core.elements.Button;
import org.matbylin.gui.core.elements.Text;
import org.matbylin.gui.pages.BasePage;

import java.io.File;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class FileDownloadPage extends BasePage<FileDownloadPage> {
    private static final String PAGE_TITLE = "File Upload & Download Automation Practice";

    private final Text pageTitle = new Text($("section h1"));
    private final TopBar topBar = new TopBar($("header"));
    private final Footer footer = new Footer($("footer"));
    private final Button pdfDownloadButton = new Button($("#btn-download-pdf").parent());

    @Override
    @Step("Validating 'File Download' page loaded")
    public void validateLoaded() {
        shouldHaveTitle(PAGE_TITLE);
        pdfDownloadButton.shouldBe(visible);
    }

    @Step("Clicking in 'Download PDF' button")
    public File downloadPdfFile() {
        return pdfDownloadButton.download();
    }
}
