package com.raidencentral.resize_images;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/*
 * @author mkyong
 *
 */
public class ResizeImage166X98 {

	private static final int IMG_MAX_WIDTH = 134;
	private static final int IMG_MAX_HEIGHT = 103;
	private static final Dimension IMG_MAX_SIZE = new Dimension(IMG_MAX_WIDTH, IMG_MAX_HEIGHT);
	private static final String BIG_IMAGE_FOLDER_PATH = "D:/Dropbox/pieces/kkz_nutstore/share_with_polly/SKU图片/stock_preview/thumbnail/a/";

	public static void main(String [] args){

		File originalImageFolder = new File("D:/Dropbox/pieces/kkz_nutstore/share_with_polly/SKU图片/images原图");
		File[] files = originalImageFolder.listFiles();
		for(File file:files){
			if(!file.isDirectory()){

				String fileFullName = file.getName();
				int dotIndex = fileFullName.lastIndexOf(".");
				String sku = fileFullName.substring(0, dotIndex);
				String extension = fileFullName.substring(dotIndex+1);

				System.out.println(fileFullName);
				System.out.println(sku);
				System.out.println(extension);
				if( ("jpg").equals(extension) || ("png").equals(extension) ){
					try{
						BufferedImage originalImage = ImageIO.read(file);
						Dimension resultSize = getScaledDimension(new Dimension(originalImage.getWidth(), originalImage.getHeight()), IMG_MAX_SIZE);
						//int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
						int type = BufferedImage.TYPE_INT_ARGB;
						
						
//						BufferedImage resizeImageJpg = resizeImage(originalImage, type);
						BufferedImage resizeImageJpg = resize(originalImage, (int)resultSize.getWidth(), (int)resultSize.getHeight(), type);
						ImageIO.write(resizeImageJpg, "jpg", new File(BIG_IMAGE_FOLDER_PATH+sku+".jpg"));

						/*BufferedImage resizeImagePng = resizeImage(originalImage, type);
					ImageIO.write(resizeImagePng, "png", new File("D:/Dropbox/pieces/kkz_nutstore/share_with_polly/SKU图片/stock_preview/image/graphics2D/mkyong_png.jpg"));
						 */
						/*BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
					ImageIO.write(resizeImageHintJpg, "jpg", new File(BIG_IMAGE_FOLDER_PATH+sku+"_hints"+".jpg"));*/

						/*BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
					ImageIO.write(resizeImageHintPng, "png", new File("D:/Dropbox/pieces/kkz_nutstore/share_with_polly/SKU图片/stock_preview/image/graphics2D/mkyong_hint_png.jpg"));
						 */
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}



	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type){

		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();
		
		BufferedImage resizedImage = new BufferedImage(IMG_MAX_WIDTH, IMG_MAX_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		//g.drawImage(originalImage, 0, 0, IMG_MAX_WIDTH, IMG_MAX_HEIGHT, null);
		//g.drawImage(originalImage, 0, 0, IMG_MAX_WIDTH, IMG_MAX_HEIGHT, 0, 0, originalWidth, originalHeight, null);
		//g.drawImage(originalImage, IMG_MAX_WIDTH, IMG_MAX_HEIGHT, null); not working
		g.dispose();

		return resizedImage;
	}

	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){

		BufferedImage resizedImage = new BufferedImage(IMG_MAX_WIDTH, IMG_MAX_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_MAX_WIDTH, IMG_MAX_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width != bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }

	    return new Dimension(new_width, new_height);
	}
	private static BufferedImage resize(BufferedImage originalImage, int biggerWidth, int biggerHeight, int type) {
	   type = BufferedImage.TYPE_INT_RGB;

	    BufferedImage resizedImage = new BufferedImage(biggerWidth, biggerHeight, type);
	    
	    Graphics2D g = resizedImage.createGraphics();

	    g.setComposite(AlphaComposite.Src);
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    g.drawImage(originalImage, 0, 0, biggerWidth, biggerHeight, null);
	    
	    g.dispose();
	    
	    

	    return resizedImage;
	}
}