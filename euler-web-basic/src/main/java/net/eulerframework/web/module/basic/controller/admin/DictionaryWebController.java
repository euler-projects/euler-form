package net.eulerframework.web.module.basic.controller.admin;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;

import net.eulerframework.web.core.annotation.JspController;
import net.eulerframework.web.core.base.controller.JspSupportWebController;
import net.eulerframework.web.module.basic.service.DictionaryService;

@JspController
@RequestMapping("/dictionary")
public class DictionaryWebController extends JspSupportWebController {
    
    @Resource
    private DictionaryService dictionaryService;
    
}
