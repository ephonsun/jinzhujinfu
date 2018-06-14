package com.canary.finance.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Base64 {
		private final static int LOWER_CASE_A_VALUE = 26;
		private final static int ZERO_VALUE = 52;
		private final static int PLUS_VALUE = 62;
		private final static int SLASH_VALUE = 63;
		private final static int SIX_BIT_MASK = 63;
		private static final int EIGHT_BIT_MASK = 0xFF;
		private String mString;
		private int mIndex = 0;

		public static String encode(byte data[]) {
			return new Base64().internalEncode(data);
		}

		public String internalEncode(byte data[]) {
			int charCount = ((data.length * 4) / 3) + 4;
			StringBuffer result = new StringBuffer((charCount * 77) / 76);
			int byteArrayLength = data.length;
			int byteArrayIndex = 0;
			int byteTriplet = 0;
			while (byteArrayIndex < byteArrayLength - 2) {
				byteTriplet = convertUnsignedByteToInt(data[byteArrayIndex++]);
				byteTriplet <<= 8;
				byteTriplet |= convertUnsignedByteToInt(data[byteArrayIndex++]);
				byteTriplet <<= 8;
				byteTriplet |= convertUnsignedByteToInt(data[byteArrayIndex++]);
				byte b4 = (byte) (SIX_BIT_MASK & byteTriplet);
				byteTriplet >>= 6;
				byte b3 = (byte) (SIX_BIT_MASK & byteTriplet);
				byteTriplet >>= 6;
				byte b2 = (byte) (SIX_BIT_MASK & byteTriplet);
				byteTriplet >>= 6;
				byte b1 = (byte) (SIX_BIT_MASK & byteTriplet);
				result.append(mapByteToChar(b1));
				result.append(mapByteToChar(b2));
				result.append(mapByteToChar(b3));
				result.append(mapByteToChar(b4));
			}
			if (byteArrayIndex == byteArrayLength - 1) {
				byteTriplet = convertUnsignedByteToInt(data[byteArrayIndex++]);
				byteTriplet <<= 4;
				byte b2 = (byte) (SIX_BIT_MASK & byteTriplet);
				byteTriplet >>= 6;
				byte b1 = (byte) (SIX_BIT_MASK & byteTriplet);
				result.append(mapByteToChar(b1));
				result.append(mapByteToChar(b2));
				result.append("==");
			}

			if (byteArrayIndex == byteArrayLength - 2) {
				byteTriplet = convertUnsignedByteToInt(data[byteArrayIndex++]);
				byteTriplet <<= 8;
				byteTriplet |= convertUnsignedByteToInt(data[byteArrayIndex++]);
				byteTriplet <<= 2;
				byte b3 = (byte) (SIX_BIT_MASK & byteTriplet);
				byteTriplet >>= 6;
				byte b2 = (byte) (SIX_BIT_MASK & byteTriplet);
				byteTriplet >>= 6;
				byte b1 = (byte) (SIX_BIT_MASK & byteTriplet);
				result.append(mapByteToChar(b1));
				result.append(mapByteToChar(b2));
				result.append(mapByteToChar(b3));
				result.append("=");
			}
			return result.toString();
		}

		public static byte[] decode(String data) {
			return new Base64().internalDecode(data);
		}

		public byte[] internalDecode(String data) {
			mString = data;
			mIndex = 0;
			int mUsefulLength = 0;
			int length = mString.length();
			for (int i = 0; i < length; i++) {
				if (isUsefulChar(mString.charAt(i))) {
					mUsefulLength++;
				}
			}

			int byteArrayLength = mUsefulLength * 3 / 4;
			byte result[] = new byte[byteArrayLength];
			int byteTriplet = 0;
			int byteIndex = 0;
			while (byteIndex + 2 < byteArrayLength) {
				byteTriplet = mapCharToInt(getNextUsefulChar());
				byteTriplet <<= 6;
				byteTriplet |= mapCharToInt(getNextUsefulChar());
				byteTriplet <<= 6;
				byteTriplet |= mapCharToInt(getNextUsefulChar());
				byteTriplet <<= 6;
				byteTriplet |= mapCharToInt(getNextUsefulChar());
				result[byteIndex + 2] = (byte) (byteTriplet & EIGHT_BIT_MASK);
				byteTriplet >>= 8;
				result[byteIndex + 1] = (byte) (byteTriplet & EIGHT_BIT_MASK);
				byteTriplet >>= 8;
				result[byteIndex] = (byte) (byteTriplet & EIGHT_BIT_MASK);
				byteIndex += 3;
			}
			if (byteIndex == byteArrayLength - 1) {
				// Take out the last two characters from the String
				byteTriplet = mapCharToInt(getNextUsefulChar());
				byteTriplet <<= 6;
				byteTriplet |= mapCharToInt(getNextUsefulChar());

				// Remove the padded zeros
				byteTriplet >>= 4;
				result[byteIndex] = (byte) (byteTriplet & EIGHT_BIT_MASK);
			}

			// Check if we have two bytes left to decode
			if (byteIndex == byteArrayLength - 2)
			{
				// Take out the last three characters from the String
				byteTriplet = mapCharToInt(getNextUsefulChar());
				byteTriplet <<= 6;
				byteTriplet |= mapCharToInt(getNextUsefulChar());
				byteTriplet <<= 6;
				byteTriplet |= mapCharToInt(getNextUsefulChar());

				// Remove the padded zeros
				byteTriplet >>= 2;
				result[byteIndex + 1] = (byte) (byteTriplet & EIGHT_BIT_MASK);
				byteTriplet >>= 8;
				result[byteIndex] = (byte) (byteTriplet & EIGHT_BIT_MASK);
			}

			return result;
		}

		private int mapCharToInt(char c)
		{
			if (c >= 'A' && c <= 'Z')
			{
				return c - 'A';
			}

			if (c >= 'a' && c <= 'z')
			{
				return (c - 'a') + LOWER_CASE_A_VALUE;
			}

			if (c >= '0' && c <= '9')
			{
				return (c - '0') + ZERO_VALUE;
			}

			if (c == '+')
			{
				return PLUS_VALUE;
			}

			if (c == '/')
			{
				return SLASH_VALUE;
			}

			throw new IllegalArgumentException(c + " is not a valid Base64 character.");
		}

		private char mapByteToChar(byte b)
		{
			if (b < LOWER_CASE_A_VALUE)
			{
				return (char) ('A' + b);
			}

			if (b < ZERO_VALUE)
			{
				return (char) ('a' + (b - LOWER_CASE_A_VALUE));
			}

			if (b < PLUS_VALUE)
			{
				return (char) ('0' + (b - ZERO_VALUE));
			}

			if (b == PLUS_VALUE)
			{
				return '+';
			}

			if (b == SLASH_VALUE)
			{
				return '/';
			}

			throw new IllegalArgumentException("Byte " + new Integer(b) + " is not a valid Base64 value");
		}

		private boolean isUsefulChar(char c)
		{
			return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == '+') || (c == '/');
		}

		private char getNextUsefulChar()
		{
			char result = '_'; // Start with a non-Base64 character
			while (!isUsefulChar(result))
			{
				result = mString.charAt(mIndex++);
			}

			return result;
		}

		private int convertUnsignedByteToInt(byte b)
		{
			if (b >= 0)
			{
				return (int) b;
			}

			return 256 + b;
		}


		
		

		public static String getEncodeMsg(String tMessage)
		{
			String returnStr = "";
			try
			{
				ByteArrayOutputStream byteStream = getCompressedStr(tMessage);
				
				if(byteStream!=null)
					returnStr = Base64.encode(byteStream.toByteArray());
				
				//returnStr = Base64.encode(tMessage.getBytes("UTF-8"));
				
				
				
			}
			catch (Exception ex)
			{
				
				ex.printStackTrace();
			}

			return returnStr;
		}
		
		

		private static ByteArrayOutputStream getCompressedStr(String tMessage)
		{

			ByteArrayOutputStream compressedStream = null;
			try
			{
				if (tMessage != null && !"".equals(tMessage))
				{
					
					byte[] input = tMessage.getBytes("UTF-8");
					
				
					Deflater compresser = new Deflater();
					compresser.setInput(input);
					compresser.finish();

					compressedStream = new ByteArrayOutputStream();
					byte[] buf = new byte[2048];
					
				
					while (!compresser.finished())
					{
						int got = compresser.deflate(buf);
						compressedStream.write(buf, 0, got);
					}
				}

			}
			catch (Exception ex)
			{
				
				ex.printStackTrace();
			} 
			finally
			{
				if (compressedStream != null)
				{
					try
					{
						compressedStream.close();
					}
					catch (IOException ioex)
					{
						
						ioex.printStackTrace();
					}
				}
				
			}
			
			return compressedStream;

		}


		public static String getDecodeMsg(String tMessage) {
			String outputString = "";
			byte[] inputStr  = null;
			try {
				String newStr = tMessage;
				newStr = newStr.replaceAll(" ", "+");
				inputStr = Base64.decode(newStr);
				outputString = getDeCompressedStr(inputStr);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return outputString;
		}

		private static String getDeCompressedStr(byte[] tMessage) {
			String returnStr = "";
			ByteArrayOutputStream aDeCompressedStream = null;
			try {
				Inflater decompresser = new Inflater();
				decompresser.setInput(tMessage);
				aDeCompressedStream = new ByteArrayOutputStream();
				byte[] buf = new byte[2048];
				while (!decompresser.finished()) {
					int got = decompresser.inflate(buf);
					aDeCompressedStream.write(buf, 0, got);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					if(aDeCompressedStream!=null)
						aDeCompressedStream.close();
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
			try {
				returnStr = aDeCompressedStream.toString("UTF-8");
			} catch (UnsupportedEncodingException encodeEx) {
				encodeEx.printStackTrace();
			}
			return returnStr;
		}
}
