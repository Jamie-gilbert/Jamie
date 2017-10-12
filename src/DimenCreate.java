import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * ʹ����С�ߵ����䷽������Android��dimen�ļ�
 * @author ggg
 *
 */
public class DimenCreate {
	Element root;
	StringBuffer outString = new StringBuffer();
	List<Element> dimens = new ArrayList();
	double olddp, newdp;
	String filePath, fileName;
	
	public DimenCreate(String pathname) {
		// ����saxReader����
		SAXReader reader = new SAXReader();
		// ͨ��read������ȡһ���ļ� ת����Document����
		Document document = null;
		try {
			File file = new File(pathname);
			filePath = file.getParentFile().getParentFile().getAbsolutePath();
			fileName = file.getName();
			InputStream is = new FileInputStream(file);
			document = reader.read(is);
			// ��ȡ���ڵ�Ԫ�ض���
			root = document.getRootElement();
			dimens = root.elements("dimen");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDP(double olddp, double newdp){
		this.olddp = olddp;
		this.newdp = newdp;
	}

	/**
	 * ���¼����ļ���dimen����ֵ
	 * @param oldValue
	 * @return
	 */
	private String reset(String oldValue) {
		String newValue = null;
		try {
			String temp = oldValue.replace("dp", "");
			temp = temp.trim();
			double doubleValue = Double.parseDouble(temp);
			double per = newdp / olddp;
			double newDouble = doubleValue * per;
			newValue = newDouble + "dp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newValue;
	}

		/**
		 * �����µ�values�ļ��к����е�dimen�ļ�
		 */
	public void getDimenFile() {
		outString.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		outString.append("<resources>\n");
		for (Element element : dimens) {
			if (element.attribute(0).getText() != null) {
				Attribute attribute = element.attribute("name");
				String attributeName = attribute.getValue();
				String value = element.getText();
				outString.append("<dimen name=\"");
				outString.append(attributeName);
				outString.append("\">");
				outString.append(reset(value));
				outString.append("</dimen>\n");
			}
		}
		outString.append("</resources>");

		System.out.println(outString.toString());
		writeFile();
	}
		/**
		 * ���ļ�д��Ŀ¼��
		 */
	private void writeFile() {
		try {
			File file = new File(filePath + "/values-sw" + ((int)newdp) + "dp/" + fileName);
			if (file.exists() == false) {
				File parentFile=new File(filePath + "/values-sw" + ((int)newdp) + "dp");
				parentFile.mkdirs();
				
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] buffer = outString.toString().getBytes();
			fileOutputStream.write(buffer, 0, buffer.length);
			fileOutputStream.flush();
			fileOutputStream.close();
			outString.delete(0, outString.length());
			System.out.println("\n\n�ļ����ɣ�"+file.getAbsolutePath()+"\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
