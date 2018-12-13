# mixDuplexPDFs
Small Java App to merge two scanned PDF files (one duplex document) back to the original page order

Even pages can be scanned in reverse order (by just flipping the stack of pages). They will be appended in reverse order.

usage: java -jar mixDuplexPDFs.jar -ep <arg> [-o <arg>] -op <arg>
 -ep,--even-pages <arg>   Path to the input file containing the even
                          pages.
 -o,--outfile <arg>       Path to the output file.
 -op,--odd-pages <arg>    Path to the input file containing the odd pages.

# Build

Run ./gradlew shadow

# Run
Run java -jar build/libs/mix-duplex-pdf-all.jar -op odd-pages.pdf -ep even-pages.pdf -o merged.pdf

If you do not specify an output file, a temp file will be created. The path to that file will be displayed when done.
