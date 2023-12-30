/**
 * A class that performs conversions between ASCII and Braille, as well as 
 * unicode according to txt files.
 * 
 * @author Jinny Eo
 */
import java.io.*;

public class BrailleASCIITables {
  // Instance variables to store conversion trees
  private static BitTree asciiToBrailleTree;
  private static BitTree brailleToAsciiTree;
  private static BitTree brailleToUnicodeTree;

  // Static block to initialize conversion trees from associated .txt files
  static {
    try {
      // Read in the conversion files
      brailleToAsciiTree = new BitTree(6);
      brailleToAsciiTree.load(new FileInputStream("BrailletoASCII.txt"));

      brailleToUnicodeTree = new BitTree(6);
      brailleToUnicodeTree.load(new FileInputStream("BrailletoUnicode.txt"));

      asciiToBrailleTree = new BitTree(8);
      asciiToBrailleTree.load(new FileInputStream("ASCIItoBraille.txt"));
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error initializing trees: " + e.getMessage());
    }
  }

  /**
   * Converts Braille representation to ASCII.
   * 
   * @param bits Braille representation.
   * @return ASCII representation.
   * @throws Exception If an error occurs during the conversion.
   */
  public static String toASCII(String bits) throws Exception {
    try {
      int expectedBitLength = 6; // Set the Braille representation length
      if (bits.length() != expectedBitLength || !isValidBitString(bits)) {
        throw new IllegalArgumentException("Invalid braille bits");
      }
      return brailleToAsciiTree.get(bits);
    } catch (IOException | IllegalArgumentException e) {
      e.printStackTrace();
      return "";
    }
  } // toASCII()

  /**
   * Converts Braille representation to Unicode.
   * 
   * @param bits Braille representation.
   * @return Unicode character.
   * @throws Exception If an error occurs during the conversion.
   */
  public static char toUnicode(String bits) throws Exception {
    try {
      String unicodeValue = brailleToUnicodeTree.get(bits);
      if (unicodeValue != null && !unicodeValue.isEmpty()) { // If unicode val is invalid
        return (char) Integer.parseInt(unicodeValue, 16); // Convert to unicode bits
      } else {
        throw new IllegalArgumentException("Invalid braille bits");
      }
    } catch (IOException | IllegalArgumentException e) {
      e.printStackTrace();
      return '\0';
    }
  } // toUnicode()

  /**
   * Converts ASCII character to Braille representation.
   * 
   * @param letter ASCII character.
   * @return Braille representation.
   * @throws Exception If an error occurs during the conversion.
   */
  public static String toBraille(char letter) throws Exception {
    try {
      int asciiValue = (int) letter;
      String binaryRepresentation = Integer.toBinaryString(asciiValue);

      // Match the expected bit length of 8
      while (binaryRepresentation.length() < 8) {
        binaryRepresentation = '0' + binaryRepresentation;
      }

      // Ensure binary representation is 8 characters long
      if (binaryRepresentation.length() != 8) {
        throw new IllegalArgumentException("Invalid binary representation length");
      }

      return asciiToBrailleTree.get(binaryRepresentation);
    } catch (IOException | IllegalArgumentException e) {
      e.printStackTrace();
      return "";
    }
  } // toBraille()

  /**
   * Checks if a given string consists of valid binary digits (0 or 1).
   * 
   * @param bits The binary string to be checked.
   * @return True if the string is a valid binary representation, false otherwise.
   */
  private static boolean isValidBitString(String bits) {
    return bits.matches("[01]+");
  } // isValidBitString()
}
