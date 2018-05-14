package my.custom.image.reader;

import java.awt.Dimension;
import java.io.File;

public class ImageReader {

  private Dimension dimension;

  private byte[] bytes;

  private File file;

  public ImageReader() {
  }

  public boolean read() throws Exception {
    // TODO
    return false;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public File getFile() {
    return this.file;
  }

  public void setDimension(Dimension dimension) {
    this.dimension = dimension;
  }

  public Dimension getDimension() {
    return this.dimension;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return this.bytes;
  }
  
}
