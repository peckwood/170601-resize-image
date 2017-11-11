package com.raidencentral.resize_images;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ConvertCMYKToRGB {
	public static void main(String[] args) throws Exception {
		
	    File f = new File("‪D:/Dropbox/pieces/kkz_nutstore/share_with_polly/SKU图片/stock_preview/image/morten-nobel/0010-03-15-0001_old.jpg");
	    System.out.println(f.getName());
	    //Find a suitable ImageReader
	    Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
	    ImageReader reader = null;
	    while(readers.hasNext()) {
	        reader = (ImageReader)readers.next();
	        if(reader.canReadRaster()) {
	        	System.out.println("found reader!");
	            break;
	        }
	    }

	    //Stream the image file (the original CMYK image)
	    ImageInputStream input =   ImageIO.createImageInputStream(f); 
	    reader.setInput(input); 

	    //Read the image raster
	    Raster raster = reader.readRaster(0, null); 

	    //Create a new RGB image
	    BufferedImage bi = new BufferedImage(raster.getWidth(), raster.getHeight(), 
	    BufferedImage.TYPE_4BYTE_ABGR); 

	    //Fill the new image with the old raster
	    bi.getRaster().setRect(raster);
	}
}
