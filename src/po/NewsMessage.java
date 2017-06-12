package po;

import java.util.List;

public class NewsMessage extends BaseMessage{
	private int ArticleCount;
	private List<News> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticls() {
		return Articles;
	}
	public void setArticls(List<News> articls) {
		Articles = articls;
	}
}
