package com.ch.sockettest.until;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * ʹ��poi��ܽ�����ת����excel
 * 
 * @author fdw
 */
public class ExportToExcel {
	
	/**
	 * excelÿһ�еı���
	 */
	private List<String> headers;
	// �����ļ�����
	private String fileName;

	private FileOutputStream fos;

	public ExportToExcel() {
	}

	private static final int HEADER_NUMBER = 0;// ��������λ��

	// excel����
	private HSSFWorkbook wb = new HSSFWorkbook();

	public ExportToExcel(List<String> headers, String fileName) {
		this.headers = headers;
		this.fileName = fileName;
	}
	
	public HSSFCellStyle createStyle(){
		// ���ñ�ͷ����
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}

	/**
	 * ��ʼ��������
	 */
	public void initHeader(HSSFSheet sheet) {
		if (headers != null) {
			HSSFRow row = sheet.createRow(HEADER_NUMBER);
			for (short i = 0; i < headers.size(); i++) {
				HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
				// ������Ԫ���Ҹ���Ԫ��ֵ
				HSSFCell cell=row.createCell(i, CellType.STRING);
				cell.setCellValue(text);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HorizontalAlignment.CENTER);
				cell.setCellStyle(style);
			}
		}
	}

	/**
	 * ������ת����excel
	 *
	 * @param path
	 *            �����ļ�����·��
	 */
	public void export(String path) throws Exception {
		String filename = this.getFileName() + ".xls";
		fos = new FileOutputStream(new File(path, filename));
		wb.write(fos);
		fos.close();
	}

	/**
	 * ����һ���µ�sheet
	 *
	 * @param sheetName
	 *            sheet������
	 * @return
	 */
	public HSSFSheet createSheet(String sheetName) {
		return wb.createSheet(sheetName);
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HSSFWorkbook getWb() {
		return wb;
	}

	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}
	
	
	
}
