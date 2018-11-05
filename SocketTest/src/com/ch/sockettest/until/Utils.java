package com.ch.sockettest.until;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 工具类
 * 
 * @author yuanji
 * @2013-12-13 上午09:29:02
 */
public class Utils {
	/**
	 * 判断是否为合法IP
	 * 
	 * @return true or false
	 */
	public static boolean isIP(String s) {
		if (s == null) {
			return false;
		}
		s = s.replaceAll("\\s", "");
		return s.matches("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
	}
	/**
	 * 判断是否为合法Mac 00:0e:c6:d6:6b:59
	 * @param s
	 * @return
	 */
	public static boolean isMac(String s) {
		boolean bool = false;
		if (s == null) {
			return false;
		}
		s = s.trim().replaceAll("\\s", "").toUpperCase();// "\\s"正则表达式，表示一个空白字符，也就是空格
		if (s.length() == 12) {// 0023abcdef
			for (int i = 0; i < 12; i++) {
				if ((s.charAt(i) >= '0' && s.charAt(i) <= '9')
						|| (s.charAt(i) >= 'A' && s.charAt(i) <= 'F')) {
					bool = true;
					continue;
				} else {
					bool = false;
					break;
				}
			}
		} else if (s.contains(":")) {
			bool = s.matches("^[\\da-fA-F]{2}(:[\\da-fA-F]{2}){5}$");// ^ 和 $
																		// 他们是分别用来匹配字符串的开始和结束
		} else if (s.contains("-")) {
			bool = s.matches("^[\\da-fA-F]{2}(-[\\da-fA-F]{2}){5}$");
		}
		return bool;
	}

	public static boolean isPort(String port_str) {
		boolean bool = false;
		int port = -1;
		try {
			port = Integer.parseInt(port_str);
			if (port > 0 && port < 65535) {
				bool = true;
			}
		} catch (NumberFormatException e) {
			bool = false;
		}

		return bool;
	}

	/**
	 * asdf1231434 is true, 1234549988 is true, 123adf33123 is false,
	 * 131344adfjlakdf is false, alkfjalkdfjal is false,
	 * 
	 * @param sn
	 * @return
	 */
	public static boolean isSN(String sn) {
		if (sn == null || sn.trim().equals(""))
			return false;
		StringBuilder sb = new StringBuilder(sn.trim());
		int lastcharIndex = sn.length() - 1;

		while (lastcharIndex >= 0 && sb.charAt(lastcharIndex) >= '0'
				&& sb.charAt(lastcharIndex) <= '9') {
			lastcharIndex--;
		}
		return (lastcharIndex < sn.length() - 1);

	}

	public static String[] macArray(String mac) {
		if (!isMac(mac))
			return null;
		String[] arr = new String[6];
		if (mac.contains(":")) {
			mac = mac.replaceAll(":", "");
		} else if (mac.contains("-")) {
			mac = mac.replaceAll("-", "");
		} else if (mac.contains(" ")) {
			mac = mac.replaceAll(" ", "");
		}
		if (mac.length() == 12) {
			arr[0] = mac.substring(0, 2);
			arr[1] = mac.substring(2, 4);
			arr[2] = mac.substring(4, 6);
			arr[3] = mac.substring(6, 8);
			arr[4] = mac.substring(8, 10);
			arr[5] = mac.substring(10);
		}
		return arr;
	}

	/**
	 * 验证mac1是否=mac2
	 * 
	 * @param mac1
	 * @param mac2
	 * @return 相等返回ture,否则返回false
	 */
	public static boolean validatemac(String mac1, String mac2) {
		if (!Utils.isMac(mac1) || !Utils.isMac(mac2)) {
			return false;
		}
		boolean b = true;
		String[] s1 = Utils.macArray(mac1.toUpperCase());
		String[] s2 = Utils.macArray(mac2.toUpperCase());
		if (s1.length == 6 && s2.length == 6) {
			for (int i = 0; i < 6; i++) {
				b &= s1[i].equals(s2[i]);
			}
		} else {
			b = false;
		}
		return b;
	}

	/**
	 * 将MAC字符串格式化为 AA:BB:CC:DD:EE:FF的形式
	 * 
	 * @param mac
	 * @return
	 * @author yuanji
	 * @2014-11-10 下午02:17:39
	 */
	public static String macformat(String mac) {

		return macformat(mac, ":");
	}

	public static String macformat(String mac, String delmi) {
		String formated = null;
		if (Utils.isMac(mac)) {
			String[] s = Utils.macArray(mac);
			formated = s[0] + delmi + s[1] + delmi + s[2] + delmi + s[3]
					+ delmi + s[4] + delmi + s[5];
		} else {
			formated = mac;
		}
		return formated;
	}

	/**
	 * XXXX-XXXX-XXXX或XXXX:XXXX:XXXX
	 * 
	 * @param mac
	 * @param delmi
	 * @return
	 */
	public static String macformat4(String mac, String delmi) {
		String formated = null;
		if (Utils.isMac(mac)) {
			String[] s = Utils.macArray(mac.toUpperCase());
			formated = s[0] + s[1] + delmi + s[2] + s[3] + delmi + s[4] + s[5];
		} else {
			formated = mac;
		}
		return formated;
	}

	public static String getclearmac(String mac) {
		String[] s = Utils.macArray(mac.toUpperCase());
		if (s.length != 6)
			return null;
		return s[0] + s[1] + s[2] + s[3] + s[4] + s[5];
	}

	public static void copyFile(File src, File trg) throws Exception {
		FileInputStream fi = null;

		FileOutputStream fo = null;

		FileChannel in = null;

		FileChannel out = null;

		try {

			fi = new FileInputStream(src);

			fo = new FileOutputStream(trg);

			in = fi.getChannel();// 得到对应的文件通道

			out = fo.getChannel();// 得到对应的文件通道

			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (fi != null)
					fi.close();
				if (in != null)
					in.close();
				if (fo != null)
					fo.close();
				if (out != null)
					out.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}
	}

	public static Long macStr2Long(String mac) {
		if (!isMac(mac))
			return (long) 0;
		String[] mac_arr = macArray(mac);
		if (mac_arr != null) {
			String hexstr = mac_arr[0] + mac_arr[1] + mac_arr[2] + mac_arr[3]
					+ mac_arr[4] + mac_arr[5];
			Long lon = Long.parseLong(hexstr, 16);
			return lon;
		} else {
			return (long) 0;
		}
	}

	public static String macLong2Str(Long maclong, String split) {
		StringBuilder sb = new StringBuilder();
		String m = Long.toHexString(maclong);
		while (m.length() < 12) {
			m = "0" + m;
		}
		if (split == null) {
			sb.append(m);
		} else {
			for (int i = 0; i < 6; i++) {
				sb.append(m.substring(i * 2, i * 2 + 2));
				if (i < 5) {
					sb.append(split);
				}
			}

		}
		return sb.toString();
	}

	/**
	 * 检查MAC是否是begin~end段中的MAC
	 * 
	 * @param mac
	 * @param step_mac
	 * @param begin
	 * @param end
	 * @return 是则返回true,否则返回false
	 * @throws Exception
	 * @author yuanji
	 * @2014-5-30 下午01:32:11
	 */
	public static boolean mac_checkScope(String mac, String begin, String end)
			throws Exception {
		if (begin == null)
			begin = "00-00-00-00-00-00";
		if (end == null)
			end = "FF-FF-FF-FF-FF-FF";
		Long m = macStr2Long(mac);
		Long b = macStr2Long(begin);
		Long e = macStr2Long(end);
		return m >= b && m <= e;

	}

	public static boolean sn_checkScope(String sn, String begin, String end)
			throws Exception {
		String snTail = snTail(sn);
		String beginTail = snTail(begin);
		String endTail = snTail(end);
		Long snLong = Long.parseLong(snTail);
		Long beginLong = Long.parseLong(beginTail);
		Long endLong = Long.parseLong(endTail);
		return snLong >= beginLong && snLong <= endLong;

	}

	public static String snHeader(String sn) {
		StringBuilder sb = new StringBuilder(sn.trim());
		int lastcharIndex = sn.length() - 1;

		while (lastcharIndex >= 0 && sb.charAt(lastcharIndex) >= '0'
				&& sb.charAt(lastcharIndex) <= '9') {
			lastcharIndex--;
		}
		String h = sn.substring(0, lastcharIndex + 1);
		return h;
	}

	public static String snTail(String sn) {
		StringBuilder sb = new StringBuilder(sn.trim());
		int lastcharIndex = sn.length() - 1;

		while (lastcharIndex >= 0 && sb.charAt(lastcharIndex) >= '0'
				&& sb.charAt(lastcharIndex) <= '9') {
			lastcharIndex--;
		}
		String h = sn.substring(lastcharIndex + 1);
		return h;
	}

	public static boolean snLessThan(String sn1, String sn2) {

		Long tail1 = Long.parseLong(snTail(sn1));
		Long tail2 = Long.parseLong(snTail(sn2));
		return tail1 <= tail2;
	}

	public static String mac_increment(String mac, int step) throws Exception {
		if (!Utils.isMac(mac))
			return mac;

		Long maclong = macStr2Long(mac);
		String split = null;
		if (mac.contains(":")) {
			split = ":";
		} else if (mac.contains("-")) {
			split = "-";
		}

		return macLong2Str(maclong + step, split).toUpperCase();
	}

	/**
	 * 
	 * @param mac
	 * @param offset
	 *            [0,5]
	 * @param step
	 * @return
	 * @throws Exception
	 */
	public static String mac_increment(String mac, int offset, int step)
			throws Exception {
		if (!Utils.isMac(mac))
			return mac;
		if (offset < 0 || offset > 5)
			offset = 5;
		String split = null;
		if (mac.contains(":")) {
			split = ":";
		} else if (mac.contains("-")) {
			split = "-";
		}
		String[] mac_arr = macArray(mac);

		mac_arr[offset] = Integer.toHexString((Integer.parseInt(
				mac_arr[offset], 16) + step) & 0xFF);
		String hexstr = mac_arr[0] + mac_arr[1] + mac_arr[2] + mac_arr[3]
				+ mac_arr[4] + mac_arr[5];
		Long lon = Long.parseLong(hexstr, 16);

		return macLong2Str(lon, split).toUpperCase();
	}

	public static String ip_increment(String ip, int step) throws Exception {
		if (!Utils.isIP(ip))
			return ip;
		String[] ips = ip.split("\\.");
		Integer ip3 = Integer.parseInt(ips[3]) + step;
		if (ip3 > 255) {
			throw new Exception("IP增长超过范围");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(ips[0]);
		sb.append(".");
		sb.append(ips[1]);
		sb.append(".");
		sb.append(ips[2]);
		sb.append(".");
		sb.append(ip3.toString());
		return sb.toString();
	}

	public static String sn_increment(String sn, int step) throws Exception {
		if (sn == null || sn.trim().equals(""))
			return sn;
		StringBuilder sb = new StringBuilder(sn.trim());
		StringBuilder result = new StringBuilder();
		int lastcharIndex = sn.length() - 1;

		while (lastcharIndex >= 0 && sb.charAt(lastcharIndex) >= '0'
				&& sb.charAt(lastcharIndex) <= '9') {
			lastcharIndex--;
		}
		@SuppressWarnings("unused")
		String h = sn.substring(0, lastcharIndex + 1);
		result.append(sn.substring(0, lastcharIndex + 1));

		String digit = sn.substring(lastcharIndex + 1);
		if (digit.length() > 0) {
			Long incremed = Long.parseLong(digit) + step;
			String tem = incremed.toString();
			while (tem.length() < digit.length()) {
				tem = "0" + tem;
			}
			result.append(tem);
		}
		// while (result.toString().length() < sn.length()) {
		// result.insert(0, "0");
		// }

		return result.toString();
	}

	public static void main(String[] args) {

		// double vout = 36;
		// int APD_DAC_OFFSET = 7;
		// double APD_AC = (vout - 0.8 * 105.6 / 5.6) * 10 - 16 *
		// APD_DAC_OFFSET;
		// System.out.println("APD_AC:" + APD_AC);
		try {
			System.out.println(sn_increment("6CD10001", 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String bytesToHexString(byte[] src, String demi) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length == 0) {
			return "";
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			if (i < src.length - 1) {
				stringBuilder.append(demi);
			}
		}
		return stringBuilder.toString();
	}

	public static String byteStringToString(String[] bstr) {
		if (bstr == null)
			return null;
		if (bstr.length == 0)
			return "";
		byte[] bts = new byte[bstr.length];
		for (int i = 0; i < bstr.length; i++) {
			bts[i] = Byte.valueOf(bstr[i]);

		}
		return new String(bts);
	}

	public static boolean checkEmpty(Control control, String emptyTip) {
		boolean bool = true;
		String text = "";
		if (control instanceof Text) {
			text = ((Text) control).getText();
		} else if (control instanceof Combo) {
			text = ((Combo) control).getText();
		}
		if (text.trim().equals("")) {
			control.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
			control.setToolTipText(emptyTip);
			bool = false;

		} else {
			control.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			control.setToolTipText("");
		}
		return bool;
	}

	/**
	 * 字节转换为浮点
	 * 
	 * @param b
	 *            字节（至少4个字节）
	 * @param index
	 *            开始位置
	 * @return
	 */
	public static float byte2float(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	/**
	 * 产生指定长度的随机字符串
	 * 
	 * @param length
	 *            指定的字符串长度
	 * @return
	 */
	public static String genRandomPwd(int length) {
		char[] codes = { '2', '3', '4', '5', '6', '7', '9', 'A', 'C', 'D', 'E',
				'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
				'U', 'V', 'W', 'X', 'Y', 'Z'
		// , 'a', 'b',
		// 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p',
		// 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
		};
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int k = r.nextInt(codes.length - 1);
			sb.append(codes[k]);
		}

		return sb.toString();
	}

	public static String genRandomHex(int length) {

		char[] codes = { '0', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
				'C', 'D', 'E', 'F' };
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int k = r.nextInt(codes.length - 1);
			sb.append(codes[k]);
		}

		return sb.toString();

	}

	/**
	 * 如果data的格式为 k1=v1;k2=v2;k3=v3...的形式，可以通过此方法获取到指定ki对应的值vi
	 * 
	 * @param data
	 * @param key
	 * @param delmi
	 * @return
	 */
	public static String getValueByKey(String data, String key, String delmi) {
		String v = null;
		if (data.contains(delmi)) {
			String[] kvs = data.split(delmi);
			if (kvs.length > 0) {
				for (String kv : kvs) {
					if (kv.contains(key + "=")) {
						v = kv.substring(kv.indexOf("=") + 1).trim();
					}
				}
			}
		} else if (data.contains(key + "=")) {
			v = data.substring(data.indexOf("=") + 1).trim();
		}
		return v;
	}

	public static void clearArp(final String ip) {
		if (!Utils.isIP(ip))
			return;
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				Process pro = null;
				try {
					// System.out.println("clear arp of " + ip);
					pro = Runtime.getRuntime().exec("arp -d " + ip + "\n");
					pro.waitFor();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (pro != null) {
						pro.destroy();
					}
				}

			}
		}, "CLEAR ARP");
		t.start();
	}

	public static void clearArpInThread(String ip) {
		Process pro = null;
		try {
			System.out.println("clear arp of " + ip);
			pro = Runtime.getRuntime().exec("arp -d " + ip + "\n");
			pro.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pro != null) {
				pro.destroy();
			}
		}
	}

	/**
	 * 将shell的位置定位到屏幕中央
	 * 
	 * @param shell
	 */
	public static void shellCenter(final Shell shell) {
		if (shell == null || shell.isDisposed())
			return;
		Monitor monitor = shell.getMonitor();
		Rectangle bounds = monitor.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * ascii 字符串，形如“ 31 31 31 31 31”，转换成对应的字符串
	 * 
	 * @param asciistr
	 *            待转换的字符串
	 * @param redix
	 *            字符串的进制数
	 * @return
	 */
	public static String asciiString2String(String asciistr, int redix)
			throws Exception {
		if (asciistr == null)
			return null;
		StringBuffer sb = new StringBuffer();
		if (asciistr.trim().contains(" ")) {
			String[] ss = asciistr.split("\\s+");
			for (String s : ss) {
				sb.append((char) Integer.parseInt(s, redix));
			}
		} else {
			sb.append((char) Integer.parseInt(asciistr, redix));
		}
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	public static String[] EnumToArray(Class cls) {
		Field fields[] = cls.getFields();
		String arr[] = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			arr[i] = fields[i].getName();
		}

		return arr;

	}
}
