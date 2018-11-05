package com.ch.sockettest.until;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;

import com.ch.sockettest.dao.TestReport;

public class ExportTestReport {

	/**
	 * ����Excel���
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static ExportToExcel CreateExcel() {

		List<String> header = new ArrayList<String>();
		header.add("�豸SN");
		header.add("��������");
		header.add("��������(Mbps)");
		header.add("��������(Mbps)");
		header.add("�ϼ�(Mbps)");
		header.add("���Խ��");
		header.add("����ʱ��");

		List<TestReport> testReports = XmlParse
				.parseXMLForDataList(new File(System.getProperty("user.dir") + File.separator + "data.xml"));

		ExportToExcel excel = new ExportToExcel(header, "WiFi��������������");
		HSSFSheet sheet = excel.createSheet("Page 1");
		HSSFCellStyle style = excel.createStyle();

		sheet.setDefaultColumnWidth(60);
		excel.initHeader(sheet);

		for (int i = 1; i <= testReports.size(); i++) {
			HSSFRow row = sheet.createRow(i);

			for (int j = 0; j < 7; j++) {
				sheet.autoSizeColumn(j, true);// ����Ӧ���
				if (j == 0) {
					String id = testReports.get(i - 1).id;
					HSSFCell cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(id);
					cell.setCellStyle(style);
					sheet.setColumnWidth(j, 5120);
				} else if (j == 1) {
					String netType = testReports.get(i - 1).netType;
					HSSFCell cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(netType);
					cell.setCellStyle(style);
					sheet.setColumnWidth(j, 5120);
				} else if (j == 2) {
					double upStreamRate = testReports.get(i - 1).upStreamRate;
					HSSFCell cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(upStreamRate + "");
					cell.setCellStyle(style);
					sheet.setColumnWidth(j, 5120);
				} else if (j == 3) {
					double downStreamRate = testReports.get(i - 1).downStreamRate;
					HSSFCell cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(downStreamRate + "");
					cell.setCellStyle(style);
					sheet.setColumnWidth(j, 5120);
				} else if (j == 4) {
					double totalStreamRate = testReports.get(i - 1).totalStreamRate;
					HSSFCell cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(totalStreamRate + "");
					cell.setCellStyle(style);
					sheet.setColumnWidth(j, 3766);
				} else if (j == 5) {
					String testResult = testReports.get(i - 1).testResult;
					HSSFCell cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(testResult);
					HSSFCellStyle style2 = excel.createStyle();
					style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					if (testResult.equals("Pass")) {
						style2.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
						cell.setCellStyle(style2);
					} else if (testResult.equals("Fail")) {
						style2.setFillForegroundColor(HSSFColor.RED.index);
						cell.setCellStyle(style2);
					} else {
						cell.setCellStyle(style);
					}
					sheet.setColumnWidth(j, 3766);
				} else {
					String testTime = testReports.get(i - 1).testTime;
					HSSFCell cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(testTime);
					cell.setCellStyle(style);
					sheet.setColumnWidth(j, 5120);
				}
			}
		}
		return excel;
	}

	/**
	 * �����������ݵ�����
	 * 
	 * @throws Exception
	 */
	public static void testReportToDesktop() throws Exception {

		ExportToExcel excel = CreateExcel();

		String path = getDeskPath();
		excel.export(path);

		System.out.println("�����ɹ���");
	}

	/**
	 * ���Ϊ�ļ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void testReportSave(String file) throws Exception {

		ExportToExcel excel = CreateExcel();

		FileOutputStream fout = new FileOutputStream(file);
		excel.getWb().write(fout);
		fout.close();
	}

	/**
	 * ��ȡ����·��
	 * 
	 * @return
	 */
	private static String getDeskPath() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File com = fsv.getHomeDirectory(); // ����Ƕ�ȡ����·���ķ�����

		String path = com.getPath() + File.separator;
		path = path.replace("\\", "\\\\");
		return path;
	}

}
