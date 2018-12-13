package servlet;


import service.WxService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/wx")
public class WxServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
         * timestamp	时间戳
         * nonce	    随机数
         * echostr	    随机字符串
         */
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");
        // 检验signature
        if (WxService.check(timestamp, nonce, signature)) {
            // 校验成功，原样返回echostr参数内容
            System.out.println("校验成功");
            PrintWriter writer = resp.getWriter();
            writer.print(echostr);
            writer.flush();
            writer.close();
        } else {
            System.out.println("校验失败");
        }
    }

    /**
     * 接受消息是事件推送
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setCharacterEncoding("utf8");
        //将xml数据转为Map
        Map<String, String> inputStreamMap = WxService.dealRequest(req.getInputStream());
        //回复所有的消息和事件
        String xmlString = WxService.replyMessageAndEvent(inputStreamMap);
        PrintWriter out = resp.getWriter();
        out.print(xmlString);
        out.flush();
        out.close();
    }
}
