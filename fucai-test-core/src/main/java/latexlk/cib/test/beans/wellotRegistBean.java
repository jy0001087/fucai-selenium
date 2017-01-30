package latexlk.cib.test.beans;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bud on 2017/1/26.
 */
public class WellotRegistBean {
    private String url;
    private ArrayList<WebContent> content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<WebContent> getContent() {
        return content;
    }

    public void setContent(ArrayList<WebContent> content) {
        this.content = content;
    }
}
