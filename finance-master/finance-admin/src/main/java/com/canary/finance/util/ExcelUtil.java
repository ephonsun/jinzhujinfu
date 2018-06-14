package com.canary.finance.util;

import static com.canary.finance.util.ConstantUtil.DOT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.canary.finance.pojo.ExportDataVO;
import com.canary.finance.pojo.PaybackOrderVO;

public class ExcelUtil {
	private static final Log LOGGER = LogFactory.getLog(ExcelUtil.class);
	private static final String YES = "\u662f";
	private static final String NO = "\u5426";
	public static String writeToExcel(List<Serializable> list, String excelHeader, String savePath, String tempFileName) {
		String result = null;
		if (list != null && list.size() > 0) {
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet();
			HSSFDataFormat format = workBook.createDataFormat();
			HSSFCellStyle style = workBook.createCellStyle();
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			style.setWrapText(true);
			style.setBorderBottom((short) 1);
			style.setBorderLeft((short) 1);
			style.setBorderRight((short) 1);
			style.setBorderTop((short) 1);
			int rowNum = 0;
			try {
				for (Serializable serialize : list) {
					try {
						if (serialize instanceof ExportDataVO) {
							if (rowNum == 0) {
								style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
								ExcelUtil.addCustomerHeader(sheet, style, excelHeader);
							}
							style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
							ExcelUtil.addCustomerContent(sheet, style, format, (ExportDataVO) serialize, rowNum);
						} else if (serialize instanceof PaybackOrderVO) {
							if (rowNum == 0) {
								style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
								ExcelUtil.addPaybackHeader(sheet, style, excelHeader);
							}
							style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
							ExcelUtil.addPaybackContent(sheet, style, format, (PaybackOrderVO) serialize, rowNum);
						} else {
							break;
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
						continue;
					}
					rowNum++;
				}
				
				File path = new File(savePath);
				if(!path.exists() && !path.isDirectory()){
					try {
						path.mkdir();
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
					}
				}
				
				File file = new File(savePath + tempFileName);
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						LOGGER.error(e.getMessage());
					}
				}
				
				try (FileOutputStream outputStream = new FileOutputStream(savePath + tempFileName)) {
					workBook.write(outputStream);
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
				result = savePath + tempFileName;
			} finally {
				if (workBook != null) {
					try {
						workBook.close();
						workBook = null;
					} catch (IOException e) {
						// do nothing.
					}
				}
			}
		}

		return result;
	}

	private static void addCustomerContent(HSSFSheet sheet, HSSFCellStyle style, HSSFDataFormat format, ExportDataVO order, int rowNum) {
		int column = 0;
		HSSFRow row = sheet.createRow(++rowNum);
		HSSFCell cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(rowNum);

		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getCustomerAccount());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getCustomerName());

		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(NO);

		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getMerchantAccount());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getMerchantName());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(NO);
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getPrincipal());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue("");
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getContractNO());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getOrderNO());
	}
	
	private static void addPaybackContent(HSSFSheet sheet, HSSFCellStyle style, HSSFDataFormat format, PaybackOrderVO order, int rowNum) {
		int column = 0;
		HSSFRow row = sheet.createRow(++rowNum);
		HSSFCell cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(rowNum);

		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getPaybackAmount());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getProductName());

		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getMerchantName());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getPaybackDate());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getCouponAmount());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		cell.setCellValue(order.getInterestAmount());
		
		cell = row.createCell(column++);
		cell.setCellStyle(style);
		if (order.getPayback() == 0) {
			cell.setCellValue("\u672a\u5230\u671f");
		} else if (order.getPayback() == 1) {
			cell.setCellValue("\u5f85\u56de\u6b3e");
		} else if (order.getPayback() == 2) {
			cell.setCellValue("\u56de\u6b3e\u4e2d");
		} else if (order.getPayback() == 3) {
			cell.setCellValue("\u56de\u6b3e\u5931\u8d25");
		} else if (order.getPayback() == 4) {
			cell.setCellValue("\u56de\u6b3e\u6210\u529f");
		} else {
			cell.setCellValue("");
		}
	}

	private static void addCustomerHeader(HSSFSheet sheet, HSSFCellStyle style, String excelHeader) {
		if (StringUtils.isNotBlank(excelHeader)) {
			String[] headers = StringUtils.split(excelHeader, DOT);
			if (headers != null && headers.length == 11) {
				int column = 0;
				HSSFRow header = sheet.createRow(0);
				HSSFCell cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[0]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[1]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[2]);

				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[3]);

				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[4]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[5]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[6]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[7]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[8]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[9]);
				
				sheet.setColumnWidth(column, 25 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[10]);
			}
		}
	}
	
	private static void addPaybackHeader(HSSFSheet sheet, HSSFCellStyle style, String excelHeader) {
		if (StringUtils.isNotBlank(excelHeader)) {
			String[] headers = StringUtils.split(excelHeader, DOT);
			if (headers != null && headers.length == 8) {
				int column = 0;
				HSSFRow header = sheet.createRow(0);
				HSSFCell cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[0]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[1]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[2]);

				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[3]);

				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[4]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[5]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[6]);
				
				sheet.setColumnWidth(column, 20 * 256);
				cell = header.createCell(column++);
				cell.setCellStyle(style);
				cell.setCellValue(headers[7]);
			}
		}
	}
}