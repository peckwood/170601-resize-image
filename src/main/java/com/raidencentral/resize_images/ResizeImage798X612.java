package com.raidencentral.resize_images;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.Scanner;

import javax.imageio.ImageIO;

/*
 * @author mkyong
 *
 */
public class ResizeImage798X612 {
	private static File originalImageFolder = new File("D:/kkz_nutstore/SKU图片/original");
	private static  int IMG_RESULT_WIDTH;
	private static  int IMG_RESULT_HEIGHT; 
	private static  Dimension IMG_MAX_SIZE = new Dimension(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT);
	private static  String targetFolderPath;

	public static void main(String [] args){
		System.out.println("please choose one: 1:处理大图 2:处理小图 3:都处理 ");
		Scanner input = new Scanner(System.in);
		int chosen = 0;
		if(input.hasNext()){
			chosen = input.nextInt();
		}
		input.close();
		Calendar calendar = Calendar.getInstance();
		long before = calendar.getTimeInMillis();
		if(chosen==1){
			IMG_RESULT_WIDTH = 798;
			IMG_RESULT_HEIGHT = 612;
			targetFolderPath = "D:/kkz_nutstore/SKU图片/stock_preview/image/";
			IMG_MAX_SIZE = new Dimension(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT);
			process();
		}else if(chosen==2){
			IMG_RESULT_WIDTH = 134;
			IMG_RESULT_HEIGHT = 103;
			targetFolderPath = "D:/kkz_nutstore/SKU图片/stock_preview/thumbnail/";
			IMG_MAX_SIZE = new Dimension(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT);
			process();
		}else if(chosen==3){
			IMG_RESULT_WIDTH = 798;
			IMG_RESULT_HEIGHT = 612;
			IMG_MAX_SIZE = new Dimension(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT);
			targetFolderPath = "D:/kkz_nutstore/SKU图片/stock_preview/image/";
			process();
			IMG_RESULT_WIDTH = 134;
			IMG_RESULT_HEIGHT = 103;
			targetFolderPath = "D:/kkz_nutstore/SKU图片/stock_preview/thumbnail/";
			IMG_MAX_SIZE = new Dimension(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT);
			process();
		}
		calendar = Calendar.getInstance();
		long after = calendar.getTimeInMillis();
		long seconds = ( after-before)/1000;
		int fileCount = originalImageFolder.listFiles().length;
		System.out.println();
		System.out.println(fileCount+" files processed in "+seconds+" seconds");
		System.out.println("speed: "+(float)fileCount/seconds+" files per second");
	}
	private static void process(){
		File targetFolder = new File(targetFolderPath);
		if(!targetFolder.exists()){
			targetFolder.mkdir();
		}
		File[] files = originalImageFolder.listFiles();
		for(File file:files){
			System.out.println("============================================================");
			if(!file.isDirectory()){
				String fileFullName = file.getName();
				int dotIndex = fileFullName.lastIndexOf(".");
				String extension = fileFullName.substring(dotIndex+1);
				String fileName = fileFullName.substring(0, dotIndex);
				System.out.println(fileFullName);
				if( ("jpg").equals(extension) || ("png").equals(extension) || ("rgb").equals(extension) ){
					try{
						BufferedImage image = null;
						if(("rgb").equals(extension)){
							String sku = fileName.split("_")[0];
							String rgbColor = fileName.split("_")[1];
							System.out.println("fileName "+fileName);
							for(String part:fileName.split("_")){
								System.out.println("fileName split: "+part );
							}
							image = createPureColorImage(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT, rgbColor);
							ImageIO.write(image, "jpg", new File(targetFolderPath+sku+".jpg"));
							
						}else{
							String sku = fileName;
							image = ImageIO.read(file);
							System.out.println("width height ratio "+image.getWidth()+" "+image.getHeight()+" "+(float)image.getWidth()/image.getHeight());						;
							//int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
							int type = BufferedImage.TYPE_INT_ARGB;						
							
//							BufferedImage resizeImageJpg = resizeImage(originalImage, type);
							if(image.getWidth()>IMG_MAX_SIZE.width || image.getHeight()>IMG_MAX_SIZE.height){
								Dimension targetDemension = getScaledDimension(new Dimension(image.getWidth(), image.getHeight()), IMG_MAX_SIZE);
								image = resize(image, (int)targetDemension.getWidth(), (int)targetDemension.getHeight(), type);
								System.out.println("width or height too big, resized");
								System.out.println("new width height "+(int)targetDemension.getWidth()+" "+(int)targetDemension.getHeight());
							}else{
								image = resize(image, image.getWidth(), image.getHeight(), type);
								System.out.println("not resized");
							}
							image = pad(image, (IMG_MAX_SIZE.width - image.getWidth())/2, (IMG_MAX_SIZE.height - image.getHeight())/2);
							ImageIO.write(image, "jpg", new File(targetFolderPath+sku+".jpg"));
						}
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
		
		BufferedImage resizedImage = new BufferedImage(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		//g.drawImage(originalImage, 0, 0, IMG_MAX_WIDTH, IMG_MAX_HEIGHT, null);
		//g.drawImage(originalImage, 0, 0, IMG_MAX_WIDTH, IMG_MAX_HEIGHT, 0, 0, originalWidth, originalHeight, null);
		//g.drawImage(originalImage, IMG_MAX_WIDTH, IMG_MAX_HEIGHT, null); not working
		g.dispose();

		return resizedImage;
	}

	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){

		BufferedImage resizedImage = new BufferedImage(IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_RESULT_WIDTH, IMG_RESULT_HEIGHT, null);
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
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.drawImage(originalImage, 0, 0, biggerWidth, biggerHeight, null);
	    g.dispose();
	    return resizedImage;
	}
	private static BufferedImage crop(BufferedImage originalImage){
		double targeWidthByHieghtRatio = IMG_MAX_SIZE.getWidth()/IMG_MAX_SIZE.getHeight();
		double originalWidthByHeightRadio = (double)originalImage.getWidth()/originalImage.getHeight();
		System.out.println("target ratio: "+targeWidthByHieghtRatio);
		System.out.println("original ratio: "+originalWidthByHeightRadio);
		System.out.println("width: " +originalImage.getWidth());
		System.out.println("height: " +originalImage.getHeight());
		if(originalWidthByHeightRadio>targeWidthByHieghtRatio){
			double widthToCrop = (double)originalImage.getWidth() - (double)originalImage.getHeight()*IMG_MAX_SIZE.getWidth()/IMG_MAX_SIZE.getHeight();
			System.out.println("crop width: "+ widthToCrop);
			
			
			System.out.println("sub image: x y width height "+(int)(widthToCrop/2)+" "+0+" "+ (int)((double)originalImage.getWidth()-widthToCrop)+" "+ originalImage.getHeight());
			//crop the width of the image
			return originalImage.getSubimage((int)(widthToCrop/2), 0, (int)((double)originalImage.getWidth()-widthToCrop), originalImage.getHeight());
		}else if(originalWidthByHeightRadio<targeWidthByHieghtRatio){
			double heightToCrop = (double)originalImage.getHeight() - (double)originalImage.getWidth()*IMG_MAX_SIZE.getHeight()/IMG_MAX_SIZE.getWidth();
			System.out.println("crop height: "+ heightToCrop);
			
			System.out.println("sub image: x y width height "+0+" "+ (int)(heightToCrop/2)+" "+ originalImage.getWidth()+" "+ (int)((double)originalImage.getHeight()-heightToCrop));
			return originalImage.getSubimage(0, (int)(heightToCrop/2), originalImage.getWidth(), (int)((double)originalImage.getHeight()-heightToCrop));
		}else{
			return originalImage;
		}
	}
	private static boolean checkValidSize(BufferedImage originalImage){
		if(originalImage.getWidth()<IMG_MAX_SIZE.getWidth() || originalImage.getHeight()<IMG_MAX_SIZE.getHeight()){
			return false;
		}else{
			return true;
		}
	}
	private static BufferedImage pad(BufferedImage originalImage, int padWidth, int padHeight){
		System.out.println("padWidth padHeight "+padWidth+padHeight);
		BufferedImage newImage = new BufferedImage(originalImage.getWidth()+2*padWidth, originalImage.getHeight()+2*padHeight, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0,originalImage.getWidth()+2*padWidth,originalImage.getHeight()+2*padHeight);
		g.drawImage(originalImage, padWidth, padHeight, null);
		g.dispose();
		return newImage;
	}
	private static BufferedImage createPureColorImage(int width, int height, String hexColor){
		//Note is is mandatory for setting RGB colors
		int type = BufferedImage.TYPE_INT_RGB;	
		BufferedImage image = new BufferedImage(width, height, type);
		Graphics2D graphics = image.createGraphics();
		System.out.println("hexColor: "+hexColor);
		graphics.setColor(Color.decode(hexColor));
		//graphics.setBackground(Color.GREEN);
		//graphics.setPaint ( Color.RED );
		graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight() );
		//graphics.clearRect( 0, 0, image.getWidth(), image.getHeight() );
		return image;
	}
	
	
	
}