package pojo.message;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;
import java.util.Map;
@XStreamAlias("xml")
public class NewsMessage extends BaseMessage{
    /**
     * 图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；
     * 其余场景最多可回复8条图文消息
     */
    @XStreamAlias("ArticleCount")
    private String articleCount;
    /**
     * 图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数
     */
    @XStreamAlias("Articles")
    private List<Articles> articles;

    public String getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(String articleCount) {
        this.articleCount = articleCount;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    public NewsMessage(Map<String, String> inputStreamMap, List<Articles> articles) {
        super(inputStreamMap);
        this.setMsgType(MSG_SEND_TYPE_NEWS);
        this.articleCount = articles.size()+"";
        this.articles = articles;
    }
}
