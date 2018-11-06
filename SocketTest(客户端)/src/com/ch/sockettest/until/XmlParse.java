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
 * 解析XML文档 Dom4j
 * 
 * @author fdw
 */
public class XmlParse {
	/**
	 * 解析XML配置文件为数据集合
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static List<TestReport> parseXMLForDataList(File xmlFile) {

		List<TestReport> testReports = new ArrayList<>();

		/** 使用SAXReader解析xml文档 */
		SAXReader saxReader = new SAXReader();

		try {
			// 1.读取xml文档
			Document doc = saxReader.read(xmlFile);
			// 2.取得Root节点
			Element rootElement = doc.getRootElement();
			// 3.遍历所有子节点
			for (Iterator<?> iter = rootElement.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				// 4.获取子节点属性值
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

				// 5.创建TestReport类的对象
				TestReport report = new TestReport();
				// 6.设置属性值
				report.id = id;
				report.netType = netType;
				report.upStreamRate = upStreamRate;
				report.downStreamRate = downStreamRate;
				report.totalStreamRate = totalStreamRate;
				report.testResult = testResult;
				report.testTime = testTime;
				// 7.添加report对象
				testReports.add(report);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testReports;
	}

	/**
	 * 保存数据到xml文档
	 * 
	 * @param id
	 * @param upStreamRate
	 * @param downStreamRate
	 * @param totalStreamRate
	 * @param testResult
	 */
	public static void saveDataForXML(String id, String netType, double upStreamRate, double downStreamRate,
			double totalStreamRate, String testResult, String testTime) {

		/** 使用SAXReader解析xml文档 */
		SAXReader saxReader = new SAXReader();
		// id是否存在
		boolean isExistId = false;

		try {
			File file = new File(System.getProperty("user.dir") + File.separator + "data.xml");
			// 1.读取xml文档
			Document doc = saxReader.read(file);
			// 2.取得Root节点
			Element rootElement = doc.getRootElement();
			// 3.获取所有的子节点
			List<?> elements = rootElement.elements("item");
			// xml元素
			Element element = null;
			for (Object obj : elements) {
				element = (Element) obj;
				if (element.attributeValue("id").equalsIgnoreCase(id)
						&& element.element("netType").getText().equalsIgnoreCase(netType)) {// 如果id已经存在,替换数据即可
					isExistId = true;
					element.element("upStreamRate").setText(upStreamRate + "");
					element.element("downStreamRate").setText(downStreamRate + "");
					element.element("totalStreamRate").setText(totalStreamRate + "");
					element.element("testResult").setText(testResult);
					element.element("testTime").setText(testTime);
				}
			}
			if (!isExistId) {// 如果不存在,创建新节点
				// 3.添加新节点
				Element item = rootElement.addElement("item");
				item.addAttribute("id", id);
				// 4.为新节点添加子节点
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
			// 5.指定文件输出的位置
			FileOutputStream outputStream = new FileOutputStream(file);
			// 指定文本的写出的格式
			OutputFormat format = OutputFormat.createPrettyPrint(); // 漂亮格式：有空格换行
			format.setEncoding("UTF-8");
			// 创建写出对象
			XMLWriter writer = new XMLWriter(outputStream, format);
			// 写出Document对象
			writer.write(doc);
			// 关闭流
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

		saveDataForXML("000000038B479","5G", 30, 20, 50, "通过", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		List<TestReport> testReports2 = XmlParse
				.parseXMLForDataList(new File(System.getProperty("user.dir") + File.separator + "data.xml"));
		System.out.println(testReports2.toString());

	}
}
