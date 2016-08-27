package net.eulerform.web.module.cms.basic.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.eulerform.web.core.annotation.RestEndpoint;
import net.eulerform.web.core.base.controller.BaseRest;
import net.eulerform.web.core.base.request.QueryRequest;
import net.eulerform.web.core.base.response.WebServicePageResponse;
import net.eulerform.web.core.base.response.WebServiceResponse;
import net.eulerform.web.module.cms.basic.entity.News;
import net.eulerform.web.module.cms.basic.entity.Partner;
import net.eulerform.web.module.cms.basic.entity.Slideshow;
import net.eulerform.web.module.cms.basic.service.INewsService;
import net.eulerform.web.module.cms.basic.service.IPartnerService;
import net.eulerform.web.module.cms.basic.service.ISlideshowService;

@RestEndpoint
@Scope("prototype")
@RequestMapping("/cms")
public class CmsRestEndpoint extends BaseRest {

    @Resource IPartnerService partnerService;
    @Resource ISlideshowService slideshowService;
    @Resource INewsService newsService;
    
    @ResponseBody
    @RequestMapping(value ="/partnerByPage", method = RequestMethod.GET)
    public WebServicePageResponse<Partner> findPartnerByPage(HttpServletRequest request, int pageIndex, int pageSize) {
        QueryRequest queryRequest = new QueryRequest(request);
        
        return new WebServicePageResponse<>(this.partnerService.findPartnerByPage(queryRequest, pageIndex, pageSize));
    }
    
    @ResponseBody
    @RequestMapping(value ="/partnerAll", method = RequestMethod.GET)
    public WebServiceResponse<Partner> loadPartnerAll() {
        return new WebServiceResponse<>(this.partnerService.loadPartners());
    }

    @ResponseBody
    @RequestMapping(value ="/newsByPage", method = RequestMethod.GET)
    public WebServicePageResponse<News> findNewsByPage(HttpServletRequest request, int pageIndex, int pageSize, boolean loadText, boolean enableTop) {
        QueryRequest queryRequest = new QueryRequest(request);
        
        return new WebServicePageResponse<>(this.newsService.findNewsByPage(queryRequest, pageIndex, pageSize, loadText, enableTop));
    }
    
    @ResponseBody
    @RequestMapping(value = "/slideshow", method = RequestMethod.GET)
    public WebServiceResponse<Slideshow> loadSlideshow() {
        return new WebServiceResponse<>(this.slideshowService.loadSlideshow());
    }
}