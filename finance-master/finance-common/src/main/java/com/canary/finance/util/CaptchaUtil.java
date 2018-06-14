package com.canary.finance.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {
	public enum ComplexLevel {
		SIMPLE, MEDIUM, HARD
	}

	private static final char[] SIMPLE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static final char[] MEDIUM = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Z', 'Y', 'Z' };
	private static final char[] HARD = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Z', 'Y', 'Z' };
	private static Random random = new Random();

	public static Object[] getCaptchaImage(int width, int height, int fontSize, int lineCount, int pointCount, boolean border, boolean colours, ComplexLevel complexLevel) {
		return getCaptchaImage(width, height, fontSize, lineCount, pointCount, border, colours, null, null, complexLevel);
	}

	public static Object[] getCaptchaImage(int width, int height, int fontSize, int lineCount, int pointCount, boolean border, boolean colours, Color fontColor, Color bgColor, ComplexLevel complexLevel) {
		Object[] object = new Object[2];
		StringBuilder sb = new StringBuilder();
		int position = 0;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics grap = bi.getGraphics();
		if (bgColor != null) {
			grap.setColor(bgColor);
		}
		grap.fillRect(0, 0, width, height);
		grap.setFont(new Font(Font.MONOSPACED, Font.BOLD, fontSize));
		grap.setColor(Color.BLACK);
		if (border) {
			grap.drawRect(0, 0, width - 1, height - 1);
		}

		switch (complexLevel) {
		case SIMPLE:
			for (int i = 0; i < 4; i++) {
				if (colours) {
					grap.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
				}
				if (fontColor != null) {
					grap.setColor(fontColor);
				}
				position = random.nextInt(SIMPLE.length);
				grap.drawString(SIMPLE[position] + "", (i * ((width - 10) / 4)) + 5 + random.nextInt(((width - 10) / 4) / 2), height / 2 + random.nextInt(height / 2));
				sb.append(SIMPLE[position]);
			}
			break;
		case MEDIUM:
			for (int k = 0; k < pointCount; k++) {
				grap.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
			}
			for (int i = 0; i < 5; i++) {
				if (colours) {
					grap.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
				}
				position = random.nextInt(MEDIUM.length);
				grap.drawString(MEDIUM[position] + "", (i * ((width - 10) / 5)) + 5 + random.nextInt(((width - 10) / 5) / 2), height / 2 + random.nextInt(height / 2));
				sb.append(MEDIUM[position]);
			}
			break;
		case HARD:
			for (int j = 0; j < lineCount; j++) {
				grap.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
			}
			for (int k = 0; k < pointCount; k++) {
				grap.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
			}
			for (int i = 0; i < 6; i++) {
				if (colours) {
					grap.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
				}
				position = random.nextInt(HARD.length);
				grap.drawString(HARD[position] + "", (i * ((width - 10) / 6)) + 5 + random.nextInt(((width - 10) / 6) / 2), height / 2 + random.nextInt(height / 2));
				sb.append(HARD[position]);
			}
			break;
		default:
			break;
		}
		grap.dispose();

		object[0] = bi;
		object[1] = sb.toString();
		return object;
	}

//	public static void main(String[] args) throws IOException, InterruptedException {
//		for (int i = 0; i < 10; i++) {
//			Object[] obj = getCaptchaImage(70, 30, 16, 100, 500, false, true, new Color(85, 85, 85), new Color(238, 238, 238), ComplexLevel.SIMPLE);
//			new File("D:\\test").mkdir();
//			ImageIO.write((BufferedImage) obj[0], "jpg", new File("D:\\test\\" + System.currentTimeMillis() + ".jpg"));
//			Thread.sleep(500L);
//		}
//	}
}
