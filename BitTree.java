/**
 * 
 * Implementation of a BitTree.
 * 
 * @author Jinny Eo
 * 
 */

import java.io.*;

// Node classes

class BitTreeNode {
  String val;
  BitTreeNode left;
  BitTreeNode right;
  int bitLength;

  BitTreeNode(int bitLength) {
    this.bitLength = bitLength;
    this.val = "";
    this.left = null;
    this.right = null;
  }
}

class BitTreeLeaf extends BitTreeNode {
  BitTreeLeaf(String val, int bitLength) {
    super(bitLength);
    this.val = val;
    this.left = null;
    this.right = null;
  }
}

// BitTree class

public class BitTree {
  private BitTreeNode root;
  private int bitLength;

  public BitTree(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("Bit length must be >= 1");
    }
    this.bitLength = n;
    this.root = new BitTreeNode(n); // Initialize an empty root node with the specified bit length.
  }

  // Set a value for a given bit string
  public void set(String bits, String value) throws Exception {
    if (bits.length() != bitLength || !isValidBitString(bits)) {
      throw new Exception("Invalid bits length or format");
    }

    BitTreeNode curr = root;
    for (int i = 0; i < bits.length() - 1; i++) {
      if (bits.charAt(i) == '0') {
        if (curr.left == null) {
          curr.left = new BitTreeNode(bitLength);
        }
        curr = curr.left;
      } else if (bits.charAt(i) == '1') {
        if (curr.right == null) {
          curr.right = new BitTreeNode(bitLength);
        }
        curr = curr.right;
      }
    }
    // Set the leaf node with the given value
    if (bits.charAt(bits.length() - 1) == '0') {
      curr.left = new BitTreeLeaf(value, bitLength);
    } else {
      curr.right = new BitTreeLeaf(value, bitLength);
    }
  }

  // Get a value for a given bit string
  public String get(String bits) throws Exception {
    BitTreeNode curr = root;
    for (int i = 0; i < bits.length(); i++) {
      if (bits.charAt(i) == '0') {
        if (curr.left == null) {
          System.out.println("Invalid path: No left child");
          throw new Exception("Invalid path");
        }
        curr = curr.left;
      } else if (bits.charAt(i) == '1') {
        if (curr.right == null) {
          System.out.println("Invalid path: No right child");
          throw new Exception("Invalid path");
        }
        curr = curr.right;
      }
    }
    // Check if the current is a leaf, returning the value if it is and null if not
    return (curr instanceof BitTreeLeaf) ? ((BitTreeLeaf) curr).val : null;
  }

  // Dump the contents of the tree to a PrintWriter
  public void dump(PrintWriter pen) {
    dumpHelper(root, "", pen);
  }

  // Helper method for recursive tree traversal during dumping
  private void dumpHelper(BitTreeNode node, String path, PrintWriter pen) {
    if (node != null) {
      if (node instanceof BitTreeLeaf) {
        pen.println(path + "," + ((BitTreeLeaf) node).val);
      }
      dumpHelper(node.left, path + "0", pen); // Recurse through the left
      dumpHelper(node.right, path + "1", pen); // Recurse through the right
    }
  }

  // Load data into the tree from an InputStream
  public void load(InputStream source) {
    if (source == null) {
      System.err.println("InputStream is null. Cannot load data.");
      return;
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(source))) {
      String line;
      int lineNumber = 0;
      while ((line = reader.readLine()) != null) {
        lineNumber++;
        String[] parts = line.split(",", 2); // Limit the split to 2 parts
        if (parts.length == 2) {
          String bits = parts[0];
          String value = parts[1];
          try {
            set(bits, value);
          } catch (Exception e) {
            System.err.println(
                "Error setting value for bits " + bits + " at line " + lineNumber + ": " + e.getMessage());
          }
        } else {
          System.err.println("Invalid line format at line " + lineNumber + ": " + line);
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading from InputStream: " + e.getMessage());
    }
  }

  // Check if a given bit string is valid
  private boolean isValidBitString(String bits) {
    return bits.matches("[01]+"); // Regex to repeatedly check for '0' or '1'
  }
}
