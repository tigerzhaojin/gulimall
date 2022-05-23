package com.tz.mall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.tz.mall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000119680669";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCcsMy5fVlip3sgBjjzLAhUDzFX5Z14sLTHJ5BYZt1F5go/qc/qpSvpH8qo33My/qGsWeU/GJjtrbhqcH7pPnEvPrnMnKb73W3cOa3HkcUWYvaoI76gRrR+A4GQL9IFr4cp1UQ8vX4xj7jrSD0/LQ8kMFU+YJrie1W+vhekOlaRUtCUqQD/KfQidhBo5GDOKb+AVvCuucfsEMw4sfJlF8BQiohGSkBfsylPOscqaA0PfcPg+os1tD4X8x3Rn65MzU6q8T/Go5tBIlsLQil9UhdkblfZALKZGwrzErYsQaw8iFZitXFS6MpL5mm4ITidslbucGauZ6EmfKUZVUjnrayrAgMBAAECggEBAJySzwBLkZZTZWWK0j2sMmZNCDGc8M45RWv8zPCJWXMPfA5B0dHXWxmI+ynkn2jFqShgbAOwwKBAh5dU8nyAOPnM5DziDLqBedYmjNBviVHlcjRyA3qFLLb/Kei64FNuItJameVYNVGxHEd/2DPZAthWs9exERRfyj7gFR+2a+qqDI7RfWhSeqTxzFs7sQVENbyO0QdYX2zoBnn7p7oQDMDlHrRaQ+NPHSNjJkKbJUBB3PtTA9xK/opc9FafZsVb03ah33+KxcenNgmZMD2ARji+aVOqRDlMR6+tvXrhyONRnnrtDUM6RXPSNB30G2FuFP4AZCOEggkCIWHtIpd/aYECgYEA+RnMqvgqlpea7tcZsO1RiIN7KTtqFgEpWBfPlvNcaGUdzoYXmIQCm52ZZRYxPU0LwP9Q7z/D+k7gjfFYE/B92gacW5fr7QGp5mOjtljrfb42LsnYV8zv068onD4TMyDNHq9Mm+/ktRe7cFhB613kbukrI580iqpvMjlw7wiJPmUCgYEAoQfIth80BHK6KhZbfsauFBWL2uDK0juvJjBDG+AYQoZ37EAxr2+JReGqGhD0y4Y/nDE4h5LO0q6CFhXd6SZSvG9VnjAtrw0yCcLEUGcynC2s7OHMQaPjKMU87E0A/M9SYj7XcfAXABIc7s27rTxDx7MgegWYo/kQuRkAiqOnRc8CgYEAwUyr7eA17DYpHD3+ybTfGDgvUoewgvLTynBvvouKodgVkTWtCcUm4OsjJbZa717iSBnYDFBHG+4pgvxNhOuSPaXzn3/8rCYmWYnoeI/37dO06Anpv/hgArxhnxzseThy8+TEZh16NKz93ugd/y8VHJwDxdi9RP2M3ESGxnaxSaECgYAXWimhP6Urf/e/Yr6iA+36u6UseKzxD/a2cwlIYZcjDp0Vhbmu113w5Gjmns7hNPbEj97Cymdz5hq3WTR1OzmEWiDwb8wwNpmrELOndU/Bll4dfCFC46FtqCzNl90szGQGG1bjDw5G1Fmdx/gPh03NCA0NpQIdxWDHdvi9Ij5/wQKBgGOUXuuDqwpiZgvop361yPzugMOf73qR9iPGgI+Iu9IOaOiiLO5ybHVjiOfNhaAFYnw9/Yv54TmcL2dL6Ap9X3T7Ql6PMhpuW3mamej653xbtn3VVuF5JiQtB9JIRTPmjdPH2qmsoSwm7Cui+WnaCh78INuxBL8C71VjL30MbDAk";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjTdkiK01KUAaHM1Qwn8n9kiGg1BKt9CTEX82ySrSsoJnlwSQNDfI74tXiayuTr4Uq+SUKTsSlymrdhBwSaPPYzIWHlLQY/QionmycLwecpqD/LzK95Q++MP0ea4Yw9Ex4jsPd5sscxjGlPgHe1CTPvaajkCnF4hyHcQbQgvPezSCMXxL9PDvSlvbFoJpv9jdUKQXGzRVc9lWEbEJ2LgeF0Dtc1QRdNUNVefCL9fUJv8WsRdqVKMQJqe5pdtc0e+YsH5wX3bZJv/bIq4AGwAUGmN1ccG5aofSSNlgHcpKBY2lJoveBC0kM8OCeU+gpsfcasuATtsGQBqRLWagPZpKjwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url="http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url= "http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\"1m\","  //支付过期时间
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
