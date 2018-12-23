package cn.unis.gmweb.utils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import cn.unis.gmweb.task.XjDataCollectorTask;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLUtils {
    private static Logger logger = LoggerFactory.getLogger(XMLUtils.class);

    private XMLUtils() {
    }

    /**
     * 获取ParamList 元素集
     *
     * @param xml xml字符串
     * @return
     */
    public static void getResultMap(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            String msgType = root.element("HEADER").attribute("MsgType").getText();
            System.err.println(msgType);
            Element paramElement = root.element("ParamList");
            Iterator<Element> iterator = paramElement.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                switch (msgType) {
                    case "QueryInspectionResultRes":
                        System.err.println(element.element("Device_Desc").getTextTrim());
                        System.err.println(element.element("Inspection_Result").getTextTrim());
                        System.err.println(element.element("IPC_PIC_Cap").getTextTrim());
                        break;
                    case "QueryDiffResultRes":
                        System.err.println(element.element("Remark").getTextTrim());
                        break;
                    default:
                        break;
                }
            }
            return ;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("xml解析异常" + e.getMessage() + "xml为:" + xml);
        }
        return ;
    }

    /**
     * 解析xml文档返回document对象
     *
     * @param xmlpath xml文档的路径
     * @return
     */
    public static Document getDocument(String xmlpath) {
        try {
            //创建一个解析器
            SAXReader reader = new SAXReader();
            //利用解析器解析xml文档
            Document dom = reader.read(xmlpath);
            return dom;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将修改后dom对象写入xml文件中
     *
     * @param dom
     * @param xmlpath
     */
    public static void Write2Xml(Document dom, String xmlpath) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(
                    new FileOutputStream(xmlpath), "utf-8"), format);
            writer.write(dom);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
