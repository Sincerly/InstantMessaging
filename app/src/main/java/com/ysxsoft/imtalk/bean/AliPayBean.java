package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/16 0016
 */
public class AliPayBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : alipay_sdk=alipay-sdk-php-20161101&app_id=2019071165819326&biz_content=%7B%22body%22%3A%22%5Cu91d1%5Cu5e01%5Cu5145%5Cu503c%22%2C%22subject%22%3A%22%5Cu91d1%5Cu5e01%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22zfb_2019081411111596313%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fchitchat.com%2Fadmin.php%2Fapi%2Falipay%2FalipayRecharge&sign_type=RSA2×tamp=2019-08-14+11%3A11%3A15&version=1.0&sign=YrWmgKyPJWi8rT08x781jKaqe0f7d05251oksiNlJLK8PrlQaKr2Kjdf5NolslsW4EEH%2Fv7rZxMCthA1x4rjKes%2BhOm8OleNWJrx6Ydbd4xIUjL6UUSBbfRV8SDEeHhevJI0bATIKSIKX%2BxrtwM4Hjyn%2FZEy9TrGk%2FkZTLdzC6gzJ5zB9J882Tyc6qSF5OjS6p3Msa9jY2673g9qlsI7upJGiZTPEKKGPBJxajP9fdi6IfHBEPwFbaH1GlwLDPtt8scnI7qmBF30M%2BEJRYrCX3p1Vq3VFoidCc6AnuQ%2FUmMFgJZtO4O4FaFSzD1Xsb8oDQkCaC2JzU3uXKyfQZUU2A%3D%3D
     */

    private int code;
    private String msg;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
