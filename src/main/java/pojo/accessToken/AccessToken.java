package pojo.accessToken;


public class AccessToken {
    /**
     * 成功的token
     */
    private String token;
    /**
     * 过期时间
     */
    private String expiresTimeout;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresTimeout() {
        return expiresTimeout;
    }

    public void setExpiresTimeout(String expiresTimeout) {
        this.expiresTimeout = expiresTimeout;
    }

    /**
     * 构造方法
     * @param token     token
     * @param expiresIn 获取到的时间
     */
    public AccessToken(String token, String expiresIn) {
        this.token = token;
        this.expiresTimeout = System.currentTimeMillis() + Integer.parseInt(expiresIn) * 1000 +"";
    }

    /**
     * 是否过期
     * @return
     */
    public boolean isExpires() {
        return System.currentTimeMillis() > Long.parseLong(this.expiresTimeout);
    }

}
