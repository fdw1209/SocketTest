package com.ch.sockettest.until;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ch.sockettest.dao.TestReport;

/**
 * ����XML�ĵ� Dom4j
 * 
 * @author fdw
 */
public class XmlParse {
	/**
	 * ����XML�����ļ�Ϊ���ݼ���
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static List<TestReport> parseXMLForDataList(File xmlFile) {

		List<TestReport> testReports = new ArrayList<>();

		/** ʹ��SAXReader����xml�ĵ� */
		SAXReader saxReader = new SAXReader();

		try {
			// 1.��ȡxml�ĵ�
			Document doc = saxReader.read(xmlFile);
			// 2.ȡ��Root�ڵ�
			Element rootElement = doc.getRootElement();
			// 3.���������ӽڵ�
			for (Iterator<?> iter = rootElement.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				// 4.��ȡ�ӽڵ�����ֵ
				String id = element.attributeValue("id");

				String netType = "";
				double upStreamRate = 0.0;
				double downStreamRate = 0.0;
				double totalStreamRate = 0.0;
				String testResult = "";
				String testTime = "";

				if (element.elementText("netType") != "") {
					netType = element.elementText("netType");
				}
				if (element.elementText("upStreamRate") != "") {
					upStreamRate = Double.parseDouble(element.elementText("upStreamRate"));
				}
				if (element.elementText("downStreamRate") != "") {
					downStreamRate = Double.parseDouble(element.elementText("downStreamRate"));
				}
				if (element.elementText("totalStreamRate") != "") {
					totalStreamRate = Double.parseDouble(element.elementText("totalStreamRate"));
				}
				if (element.elementText("testResult") != "") {
					testResult = element.elementText("testResult");
				}
				if (element.elementText("testTime") != "") {
					testTime = element.elementText("testTime");
				}

				// 5.����TestReport��Ķ���
				TestReport report = new TestReport();
				// 6.��������ֵ
				report.id = id;
				report.netType = netType;
				report.upStreamRate = upStreamRate;
				report.downStreamRate = downStreamRate;
				report.totalStreamRate = totalStreamRate;
				report.testResult = testResult;
				report.testTime = testTime;
				// 7.���report����
				testReports.add(report);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testReports;
	}

	/**
	 * �������ݵ�xml�ĵ�
	 * 
	 * @param id
	 * @param upStreamRate
	 * @param downStreamRate
	 * @param totalStreamRate
	 * @param testResult
	 */
	public static void saveDataForXML(String id, String netType, double upStreamRate, double downStreamRate,
			double totalStreamRate, String testResult, String testTime) {

		/** ʹ��SAXReader����xml�ĵ� */
		SAXReader saxReader = new SAXReader();
		// id�Ƿ����
		boolean isExistId = false;

		try {
			File file = new File(System.getProperty("user.dir") + File.separator + "data.xml");
			// 1.��ȡxml�ĵ�
			Document doc = saxReader.read(file);
			// 2.ȡ��Root�ڵ�
			Element rootElement = doc.getRootElement();
			// 3.��ȡ���е��ӽڵ�
			List<?> elements = rootElement.elements("item");
			// xmlԪ��
			Element element = null;
			for (Object obj : elements) {
				element = (Element) obj;
				if (element.attributeValue("id").equalsIgnoreCase(id)
						&& element.element("netType").getText().equalsIgnoreCase(netType)) {// ���id�Ѿ�����,�滻���ݼ���
					isExistId = true;
					element.element("upStreamRate").setText(upStreamRate + "");
					element.element("downStreamRate").setText(downStreamRate + "");
					element.element("totalStreamRate").setText(totalStreamRate + "");
					element.element("testResult").setText(testResult);
					element.element("testTime").setText(testTime);
				}
			}
			if (!isExistId) {// ���������,�����½ڵ�
				// 3.����½ڵ�
				Element item = rootElement.addElement("item");
				item.addAttribute("id", id);
				// 4.Ϊ�½ڵ�����ӽڵ�
				Element netType1 = item.addElement("netType");
				netType1.setText(netType);

				Element upStreamRate1 = item.addElement("upStreamRate");
				upStreamRate1.setText(upStreamRate + "");

				Element downStreamRate1 = item.addElement("downStreamRate");
				downStreamRate1.setText(downStreamRate + "");

				Element totalStreamRate1 = item.addElement("totalStreamRate");
				totalStreamRate1.setText(totalStreamRate + "");

				Element testResult1 = item.addElement("testResult");
				testResult1.setText(testResult);

				Element testTime1 = item.addElement("testTime");
				testTime1.setText(testTime);
			}
			// 5.ָ���ļ������λ��
			FileOutputStream outputStream = new FileOutputStream(file);
			// ָ���ı���д���ĸ�ʽ
			OutputFormat format = OutputFormat.createPrettyPrint(); // Ư����ʽ���пո���
			format.setEncoding("UTF-8");
			// ����д������
			XMLWriter writer = new XMLWriter(outputStream, format);
			// д��Document����
			writer.write(doc);
			// �ر���
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean isExistId(Element rootElement, String id) {

		return false;
	}

	public static void main(String[] args) {
		
		List<TestReport> testReports = XmlParse
				.parseXMLForDataList(new File(System.getProperty("user.dir") + File.separator + "data.xml"));
		System.out.println(testReports.toString());

		saveDataForXML("000000038B479","5G", 30, 20, 50, "ͨ��", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		List<TestReport> testReports2 = XmlParse
				.parseXMLForDataList(new File(System.getProperty("user.dir") + File.separator + "data.xml"));
		System.out.println(testReports2.toString());

	}
}
