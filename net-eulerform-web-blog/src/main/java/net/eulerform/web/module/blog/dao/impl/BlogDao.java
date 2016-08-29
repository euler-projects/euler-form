package net.eulerform.web.module.blog.dao.impl;

import java.util.Date;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import net.eulerform.common.CalendarTool;
import net.eulerform.common.StringTool;
import net.eulerform.web.core.base.dao.impl.hibernate5.BaseDao;
import net.eulerform.web.core.base.request.QueryRequest;
import net.eulerform.web.core.base.response.PageResponse;
import net.eulerform.web.core.extend.hibernate5.RestrictionsX;
import net.eulerform.web.module.blog.dao.IBlogDao;
import net.eulerform.web.module.blog.entity.Blog;

public class BlogDao extends BaseDao<Blog> implements IBlogDao {

    @Override
    public PageResponse<Blog> findBlogByPage(QueryRequest queryRequest, int pageIndex, int pageSize, boolean loadText, boolean enableTop) {

//        String alias = "blog_"; // 查詢時的table別名
//        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(this.entityClass, alias);
//
//        ProjectionList projection = Projections.projectionList();
//        projection.add(Projections.property(alias + "." + "id").as("id"));
//        projection.add(Projections.property(alias + "." + "title").as("title"));
//        projection.add(Projections.property(alias + "." + "author").as("author"));
//        projection.add(Projections.property(alias + "." + "top").as("top"));
//        projection.add(Projections.property(alias + "." + "summary").as("summary"));
//        projection.add(Projections.property(alias + "." + "pubDate").as("pubDate"));
//        if (loadText)
//            projection.add(Projections.property(alias + "." + "text").as("text"));
//        projection.add(Projections.property(alias + "." + "imageFileName").as("imageFileName"));
        
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(this.entityClass);
        
        try {
            String queryValue = null;
            queryValue = queryRequest.getQueryValue("title");
            if (!StringTool.isNull(queryValue)) {
                detachedCriteria.add(RestrictionsX.like("title", queryValue, MatchMode.ANYWHERE).ignoreCase());
            }
            queryValue = queryRequest.getQueryValue("author");
            if (!StringTool.isNull(queryValue)) {
                detachedCriteria.add(RestrictionsX.like("author", queryValue, MatchMode.ANYWHERE).ignoreCase());
            }
            queryValue = queryRequest.getQueryValue("summary");
            if (!StringTool.isNull(queryValue)) {
                detachedCriteria.add(RestrictionsX.like("summary", queryValue, MatchMode.ANYWHERE).ignoreCase());
            }
            queryValue = queryRequest.getQueryValue("text");
            if (!StringTool.isNull(queryValue)) {
                detachedCriteria.add(RestrictionsX.like("text", queryValue, MatchMode.ANYWHERE).ignoreCase());
            }
            queryValue = queryRequest.getQueryValue("pubDate");
            if (!StringTool.isNull(queryValue)) {
                Date pubDate = CalendarTool.parseDate(queryValue, "yyyy-MM-dd");
                Date begin = CalendarTool.beginningOfTheDay(pubDate).getTime();
                Date end = CalendarTool.endingOfTheDay(pubDate).getTime();
                detachedCriteria.add(Restrictions.between("pubDate", begin, end));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(enableTop)
            detachedCriteria.addOrder(Order.desc("top"));
        
        detachedCriteria.addOrder(Order.desc("pubDate"));
        
        ProjectionList projection = Projections.projectionList();
        
        projection.add(Projections.property("id").as("id"));
        projection.add(Projections.property("title").as("title"));
        projection.add(Projections.property("author").as("author"));
        projection.add(Projections.property("top").as("top"));
        projection.add(Projections.property("summary").as("summary"));
        projection.add(Projections.property("pubDate").as("pubDate"));
        projection.add(Projections.property("imageFileName").as("imageFileName"));
        if (loadText)
            projection.add(Projections.property("text").as("text"));
        
        return this.findPageBy(detachedCriteria, pageIndex, pageSize, projection);
    }

}
