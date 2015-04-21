package mixDuplexPDFs;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;


public class MixDuplexPDFs {
	public static void main(String args[]) throws IOException, COSVisitorException {
		PDDocument evenPages = PDDocument.load(new File("F:\\evenpages.pdf"), new RandomAccessBuffer());
		PDDocument oddPages = PDDocument.load(new File("F:\\oddpages.pdf"), new RandomAccessBuffer());

		try (PDDocument result = new PDDocument();) {
			int index = 0;

			while (index < evenPages.getNumberOfPages() || index < oddPages.getNumberOfPages()) {
				PDPage oddPage = getEvenPageIfExists(oddPages, index);
				PDPage evenPage = getOddPageIfExists(evenPages, index);

				if (oddPage != null)
					result.addPage(oddPage);
				if (evenPage != null)
					result.addPage(evenPage);

				index++;
			}

			result.save("F:\\mergedDoc.pdf");

		}
	}

	private static PDPage getEvenPageIfExists(PDDocument doc, int pageIndex) {
		System.out.println("even pageIndex: " + pageIndex);
		if (doc.getDocumentCatalog().getAllPages().size() > pageIndex) {
			return (PDPage) doc.getDocumentCatalog().getAllPages().get(pageIndex);
		}

		return null;
	}

	private static PDPage getOddPageIfExists(PDDocument doc, int pageIndex) {
		int pageNum = doc.getNumberOfPages() - 1 - pageIndex;
		System.out.println("odd pageNum: " + pageNum);

		if (pageNum >= 0) {
			return (PDPage) doc.getDocumentCatalog().getAllPages().get(pageNum);
		}

		return null;
	}
}
