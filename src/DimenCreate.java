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
 * 使用最小边的适配方法生成Android的dimen文件
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
		// 创建saxReader对象
		SAXReader reader = new SAXReader();
		// 通过read方法读取一个文件 转换成Document对象
		Document document = null;
		try {
			File file = new File(pathname);
			filePath = file.getParentFile().getParentFile().getAbsolutePath();
			fileName = file.getName();
			InputStream is = new FileInputStream(file);
			document = reader.read(is);
			// 获取根节点元素对象
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
	 * 重新计算文件中dimen的数值
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
		 * 生成新的values文件夹和其中的dimen文件
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
		 * 将文件写的目录下
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
			System.out.println("\n\n文件生成："+file.getAbsolutePath()+"\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
