
/*

	RGBImage is a class that represents an RGB image as a 2d array of RGBPixel

	This class provides a way to create an empty image (call RGBImage(int height, int width))
         and then expects setPixel to be called height*width times for each pixel coordinate

	This class also provides a constructor that will read an image from disk into the 2d array
	 (call RGBImage(String jpgFileName) )

	This class also provides a method to write the 2d array to disk (call writeImage(String fName))

	Author: Michael Eckmann
	Skidmore College
	for Spring 2017
	Digital Image Processing Course

*/
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.stream.*;

public class RGBImage {

	private RGBPixel[][] pic;
	private YCbCrPixel[][] YCbCrpic;

	/**
	 * Constructor that creates a new RGBIMage object from an existing RGBImage.
	 * 
	 * @param img The RGBImage to copy
	 */
	public RGBImage(RGBImage img) {
		pic = new RGBPixel[img.getNumRows()][img.getNumCols()];
		for (int r = 0; r < img.getNumRows(); r++) {
			for (int c = 0; c < img.getNumCols(); c++) {
				pic[r][c] = new RGBPixel(img.getPixel(r, c).getRed(), img.getPixel(r, c).getGreen(),
						img.getPixel(r, c).getBlue());
			}
		}
	}

	/**
	 * Constructor that creates a completely black RGBImage of the appropriate
	 * size.
	 * 
	 * @param height The height of the image
	 * @param width The width of the image
	 */
	public RGBImage(int height, int width) {
		pic = new RGBPixel[height][width];
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				pic[r][c] = new RGBPixel(0,0,0);
			}
		}
	}

	/**
	 * Constructor that reads in a jpeg file name.
	 * 
	 * @param jpgFileName Name of the jpg file
	 */
	public RGBImage(String jpgFileName) {
		readImage(jpgFileName);
	}

	/**
	 * Constructor that creates an RGB image from either a jpg or a ppm file.
	 * 
	 * @param fileName The name of the file.
	 * @param jpg The presence of a jpg file.
	 */
	public RGBImage(String fileName, boolean jpg) {
		if (!jpg)
			readPPMImage(fileName);
		else
			readImage(fileName);
	}

	public int[] intensityHistogram() {
		int[] hist = new int[256];

		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				hist[pic[r][c].getIntensity()]++;
			}
		}

		return hist;
	}

	public int[] redHistogram() {
		int[] hist = new int[256];

		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				hist[pic[r][c].getRed()]++;
			}
		}

		return hist;
	}

	public int[] greenHistogram() {
		int[] hist = new int[256];

		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				hist[pic[r][c].getGreen()]++;
			}
		}

		return hist;
	}

	public int[] blueHistogram() {
		int[] hist = new int[256];

		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				hist[pic[r][c].getBlue()]++;
			}
		}

		return hist;
	}

	public int getNumRows() {
		return getHeight();
	}

	public int getNumCols() {
		return getWidth();
	}

	public int getHeight() {
		return pic.length;
	}

	public int getWidth() {
		return pic[0].length;
	}

	/**
	 * Returns the pixel at the givin Row and Column.
	 * 
	 * @param r The row
	 * @param c The column
	 * @return RGBPixel The RGBPixel
	 */
	public RGBPixel getPixel(int r, int c) {
		return pic[r][c];
	}

	/**
	 * Writes a pixel to the appropriate row and column.
	 * 
	 * @param r The row
	 * @param c The Column
	 * @param p The RGBPixel
	 */
	public void setPixel(int r, int c, RGBPixel p) {
		pic[r][c] = p;
	}

	public void setPixel(int r, int c, int red, int green, int blue) {
		pic[r][c] = new RGBPixel(red, green, blue);
	}

	public void makeBlack() {
		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				setPixel(r, c, 0, 0, 0);
			}
		}

	}

	public void convertToYCbCr() {
		YCbCrpic = new YCbCrPixel[getNumRows()][getNumCols()];

		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				YCbCrpic[r][c] = pic[r][c].convertToYCbCr();
			}
		}
	}

	public void convertToRGBFromYCbCr() {

		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				pic[r][c] = YCbCrpic[r][c].convertToRGB();
			}
		}
	}

	/**
	 * Reads a jpeg image from a file on disk into an RGBImage (which contains a
	 * 2d array of RGBPixel).
	 * 
	 * @param jpgFileName The name of the jpgFile
	 */
	private void readImage(String jpgFileName) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(jpgFileName));
		} catch (IOException e) {
			System.out.println("Could not read " + jpgFileName);
		}
		int w = img.getWidth();
		int h = img.getHeight();

		int allPixels[] = img.getRGB(0, 0, w, h, null, 0, w);

		int rows = h, cols = w;

		pic = new RGBPixel[h][w];

		for (int i = 0; i < allPixels.length; i++) {
			Color c = new Color(allPixels[i]);

			int row = i / w;
			int col = i % w;
			pic[row][col] = new RGBPixel(c.getRed(), c.getGreen(), c.getBlue());
		}

	}

	/**
	 * Write a jpeg image to disk from this RGBImage
	 * 
	 * @param fName The name of the file being written
	 * @throws IOException
	 */
	public void writeImage(String fName) throws IOException {
		int i = 0;
		int pixelArray[] = new int[getNumRows() * getNumCols()];
		BufferedImage img = new BufferedImage(getNumCols(), getNumRows(), BufferedImage.TYPE_INT_RGB);

		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				Color col = new Color(getPixel(r, c).getRed(), getPixel(r, c).getGreen(), getPixel(r, c).getBlue());
				img.setRGB(c, r, col.getRGB());
			}
		}
		// modified code from:
		// http://www.javased.com/?api=javax.imageio.IIOImage
		Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
		ImageWriter writer = (ImageWriter) iter.next();
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(1);
		File outputFile = new File(fName);
		FileImageOutputStream output = new FileImageOutputStream(outputFile);
		writer.setOutput(output);
		IIOImage image = new IIOImage(img, null, null);
		writer.write(null, image, iwp);
		writer.dispose();

	}

	/**
	 * Reads a PPM file and stores it as a RGBIMage.
	 * 
	 * @param fName The name of the PPM file
	 */
	public void readPPMImage(String fName) {
		File theFile;
		Scanner scan;
		try {
			theFile = new File(fName);
			scan = new Scanner(theFile);

			int i = 0, j = 0;
			int tokenCounter = 0;
			int cols = 0;
			int rows = 0;
			StringTokenizer tokens = null;

			outloop: while (scan.hasNextLine()) {
				String aLine = scan.nextLine();
				if (aLine.charAt(0) == '#')
					continue;

				// process the line:
				tokens = new StringTokenizer(aLine, " ");
				while (tokens.hasMoreTokens()) {
					tokenCounter++;
					String aToken = tokens.nextToken();
					if (tokenCounter == 1) {
						// expect that aToken is "P3"
						if (!aToken.equals("P3"))
							System.out.println("Expected P3 magic number, got " + aToken);
					}
					if (tokenCounter == 2) {
						// expect that aToken is the number of columns
						cols = Integer.parseInt(aToken);
					}
					if (tokenCounter == 3) {
						// expect that aToken is the number of rows
						rows = Integer.parseInt(aToken);
					}
					if (tokenCounter == 4) {
						// expect that aToken is the maximum value
						// ignore
						break outloop;
					}

				}

			}

			pic = new RGBPixel[rows][cols];

			// still worry if more tokens in previously read line

			int channelCount = 0;
			int redVal = 0, blueVal = 0, greenVal = 0;
			while (scan.hasNextLine()) {
				String aLine = scan.nextLine();
				if (aLine.charAt(0) == '#')
					continue;

				// process the line:
				tokens = new StringTokenizer(aLine, " ");
				while (tokens.hasMoreTokens()) {
					String aToken = tokens.nextToken();
					if (channelCount == 0) {
						redVal = Integer.parseInt(aToken);
						channelCount++;
					} else if (channelCount == 1) {
						// expect that aToken is the number of columns
						greenVal = Integer.parseInt(aToken);
						channelCount++;
					} else if (channelCount == 2) {
						// expect that aToken is the number of rows
						blueVal = Integer.parseInt(aToken);
						pic[i][j] = new RGBPixel(redVal, greenVal, blueVal);
						j++;
						if (j >= cols) {
							i++;
							j = 0;
						}
						channelCount = 0;

					}
				}

			}
		} catch (IOException e) {
			System.out.println("Could not read " + fName);
		}
	}

	/**
	 * Writes a PPM Image from this RGBIMage
	 * 
	 * @param fName The name of the file to be written.
	 * @throws IOException
	 */
	public void writePPMImage(String fName) throws IOException {
		File theFile = new File(fName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(theFile));
		bw.write("P3");
		bw.newLine();
		bw.write(pic[0].length + " " + pic.length + " 255");
		bw.newLine();
		int i = 0, j = 0;

		while (true) {
			int redVal = pic[i][j].getRed();
			int greenVal = pic[i][j].getGreen();
			int blueVal = pic[i][j].getBlue();
			bw.write("" + redVal + " " + greenVal + " " + blueVal);
			bw.newLine();
			j++;
			if (j >= pic[0].length) {
				i++;
				j = 0;
			}
			if (i >= pic.length)
				break;
		}
		bw.close();
	}

}
