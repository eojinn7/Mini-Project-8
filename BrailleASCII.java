/**
 * Takies command line arguments to determine what to do with ASCII and Braille.
 * 
 * @author Jinny Eo
 */
public class BrailleASCII {

    /**
     * The main method that takes command line arguments and performs the conversion.
     * 
     * @param args Command line arguments: <targetCharset> <sourceText>
     */
    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length != 2) {
            System.out.println("Usage: java BrailleASCII <targetCharset> <sourceText>");
            System.exit(1);
        }

        // Extract targetCharset and sourceText from command line arguments
        String targetCharset = args[0].toLowerCase();
        String sourceText = args[1];

        try {
            String result;
            // Perform conversion based on the targetCharset
            switch (targetCharset) {
                case "braille":
                    result = toBraille(sourceText);
                    break;
                case "ascii":
                    result = toBrailleASCII(sourceText);
                    break;
                case "unicode":
                    result = toUnicode(sourceText);
                    break;
                default:
                    System.out.println("Incorrect instruction. Please input 'braille', 'ascii', or 'unicode'.");
                    return;
            }

            // Print the result
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the source text to Braille representation.
     * 
     * @param sourceText The input text to be converted.
     * @return The Braille representation of the input text.
     * @throws Exception If an error occurs during the conversion.
     */
    private static String toBraille(String sourceText) throws Exception {
        StringBuilder brailleBuilder = new StringBuilder();
        for (char c : sourceText.toCharArray()) {
            // Call toBraille() method to convert each character to Braille
            String braille = BrailleASCIITables.toBraille(c);
            brailleBuilder.append(braille);
        }
        return brailleBuilder.toString();
    }

    /**
     * Converts the source text to ASCII representation using Braille encoding.
     * 
     * @param sourceText The input text to be converted.
     * @return The ASCII representation of the input text.
     */
    private static String toBrailleASCII(String sourceText) {
        StringBuilder asciiBuilder = new StringBuilder();
        // Loop through every 6 characters in the source text
        for (int i = 0; i < sourceText.length(); i += 6) {
            String brailleChunk = sourceText.substring(i, Math.min(i + 6, sourceText.length()));
            try {
                // Call toASCII() method to convert Braille chunk to ASCII
                String asciiChar = BrailleASCIITables.toASCII(brailleChunk).toLowerCase();
                asciiBuilder.append(asciiChar);
            } catch (Exception e) {
                System.err.println("Error converting Braille chunk: " + brailleChunk + " - " + e.getMessage());
            }
        }
        return asciiBuilder.toString();
    }

    /**
     * Converts the source text to Unicode representation using Braille encoding.
     * 
     * @param sourceText The input text to be converted.
     * @return The Unicode representation of the input text.
     * @throws Exception If an error occurs during the conversion.
     */
    private static String toUnicode(String sourceText) throws Exception {
        StringBuilder unicodeBuilder = new StringBuilder();
        for (char c : sourceText.toCharArray()) {
            // Call toBraille() method to get the binary representation of each character
            String binaryRepresentation = BrailleASCIITables.toBraille(c);
            // Call toUnicode() method to convert binary representation to Unicode
            String unicodeChar = String.valueOf(BrailleASCIITables.toUnicode(binaryRepresentation));
            unicodeBuilder.append(unicodeChar);
        }
        return unicodeBuilder.toString();
    }
}
