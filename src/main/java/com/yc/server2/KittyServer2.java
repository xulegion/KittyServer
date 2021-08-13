package com.yc.server2;


import com.yc.threadpool.ThreadPoolManger;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class KittyServer2 {

    private Logger logger=Logger.getLogger(KittyServer2.class.getName());

    private ThreadPoolManger tpm;

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        KittyServer2 ks = new KittyServer2();
        ks.startServer();
    }

    boolean flag=false;   //启动主服务器

    private void startServer() throws ParserConfigurationException, SAXException, IOException {

        ServerSocket ss=null;
        int port = parseServerXml();   //解析xml获取端口号

        int cpuCount=Runtime.getRuntime().availableProcessors();
        ThreadPoolManger tpm = new ThreadPoolManger(cpuCount, null);//线程池
        try {
            ss=new ServerSocket(port);
            logger.info("kitty server is starting,and listening to port "+ss.getLocalPort());
        }catch (Exception e){
            logger.error("kitty server's port "+port+" is already in use...");
            return;
        }

        while (!flag){
            try {
                Socket s = ss.accept();
                logger.debug("a client "+s.getInetAddress()+"is connecting to kitty server...");
                tpm.process(new TaskService(s));
            }catch (Exception e){
                e.printStackTrace();
                logger.error("client is down,cause:"+e.getMessage());
            }
        }
    }

    /**
     * 解析xml得到port
     * @return
     */
    private int parseServerXml() throws IOException, SAXException, ParserConfigurationException {
        List<Integer> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //通过DocumentBuilderFactory创建xml解释器

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("conf/server.xml");
            NodeList nl = doc.getElementsByTagName("Connector");
            for (int i=0;i<nl.getLength();i++){
                Element node = (Element) nl.item(i);
                list.add(Integer.parseInt(node.getAttribute("port")));

            }

        }catch (Exception e){
            throw e;
        }
        return list.get(0);
    }
}
