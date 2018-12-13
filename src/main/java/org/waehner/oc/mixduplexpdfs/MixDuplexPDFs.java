package org.waehner.oc.mixduplexpdfs;


import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class MixDuplexPDFs {

	private static final Options options;

	static {
		options = new Options();

		Option oddPages = new Option("op", "odd-pages", true, "Path to the input file containing the odd pages.");
		Option evenPages = new Option("ep", "even-pages", true, "Path to the input file containing the even pages.");
		Option outFile = new Option("o", "outfile", true, "Path to the output file.");

		oddPages.setRequired(true);
		evenPages.setRequired(true);
		outFile.setRequired(false);

		options.addOption(oddPages);
		options.addOption(evenPages);
		options.addOption(outFile);
	}

	private static void printOptsHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar mixDuplexPDFs.jar", options, true);
	}

	public static void main(String args[]) throws IOException, ParseException {
		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException pe) {
			printOptsHelp();
			throw pe;
		}

		String oddPagesPdfPath = commandLine.getOptionValue("op");
		String evenPagesPdfPath = commandLine.getOptionValue("ep");
		String outfilePath = commandLine.getOptionValue("o");

		if (oddPagesPdfPath == null || evenPagesPdfPath == null) {

		} else {

			File outfile;
			if (outfilePath != null) {
				outfile = new File(outfilePath);
			} else {
				outfile = File.createTempFile("mixDuplexPDFs", ".tmp.pdf");
			}
			PDDocument evenPages = PDDocument.load(new File(evenPagesPdfPath));
			PDDocument oddPages = PDDocument.load(new File(oddPagesPdfPath));

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

				result.save(outfile);
				System.out.println("Saved merged document to " + outfile.getAbsolutePath());
			}
		}
	}

	private static PDPage getEvenPageIfExists(PDDocument doc, int pageIndex) {
		if (doc.getPages().getCount() > pageIndex) {
			return (PDPage) doc.getPage(pageIndex);
		}

		return null;
	}

	private static PDPage getOddPageIfExists(PDDocument doc, int pageIndex) {
		int pageNum = doc.getNumberOfPages() - 1 - pageIndex;

		if (pageNum >= 0) {
			return (PDPage) doc.getPage(pageNum);
		}

		return null;
	}
}
