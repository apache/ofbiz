package org.ofbiz.jcr.orm.jackrabbit;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(extend = OfbizRepositoryMappingJackrabbitLocalizedContent.class)
public class OfbizRepositoryMappingJackrabbitArticle extends OfbizRepositoryMappingJackrabbitLocalizedContent {

    @Field(id = true)
    String title = null;
    @Field
    String content = null;

    /**
     *
     * @param nodePath
     * @param language
     * @param title
     * @param content
     */
    public OfbizRepositoryMappingJackrabbitArticle(String nodePath, String language, String title, String content) {
        super(nodePath, language);

        this.title = title;
        this.content = content;
    }

    public OfbizRepositoryMappingJackrabbitArticle() {
        super();
        // create an empty object
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
