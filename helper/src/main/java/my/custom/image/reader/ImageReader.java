package my.custom.image.reader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {

  public byte[] read(File file) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(file);
  }
  
}
