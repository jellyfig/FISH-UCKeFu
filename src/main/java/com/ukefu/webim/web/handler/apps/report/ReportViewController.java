package com.ukefu.webim.web.handler.apps.report;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ukefu.util.Menu;
import com.ukefu.webim.service.repository.DataDicRepository;
import com.ukefu.webim.service.repository.PublishedReportRepository;
import com.ukefu.webim.web.handler.Handler;
import com.ukefu.webim.web.model.PublishedReport;

@Controller
@RequestMapping("/apps/view")
public class ReportViewController extends Handler{
	
	@Value("${web.upload-path}")
    private String path;
	
	@Value("${uk.im.server.port}")  
    private Integer port; 
	
	@Autowired
	private DataDicRepository dataDicRes;
	
	@Autowired
	private PublishedReportRepository publishedReportRes;
	
	
    @RequestMapping("/index")
    @Menu(type = "setting" , subtype = "report" , admin= true)
    public ModelAndView index(ModelMap map , HttpServletRequest request , @Valid String dicid) {
    	Page<PublishedReport> publishedReportList = null ;
    	if(!StringUtils.isBlank(dicid) && !"0".equals(dicid)){
        	map.put("dataDic", dataDicRes.findByIdAndOrgi(dicid, super.getOrgi(request))) ;
    		map.put("reportList", publishedReportList = publishedReportRes.findByOrgiAndDicid(super.getOrgi(request) , dicid , new PageRequest(super.getP(request), super.getPs(request)))) ;
    	}else{
    		map.put("reportList", publishedReportList = publishedReportRes.findByOrgi(super.getOrgi(request) , new PageRequest(super.getP(request), super.getPs(request)))) ;
    	}
    	if(publishedReportList!=null && publishedReportList.getContent().size() > 0) {
    		map.put("report", publishedReportList.getContent().get(0)) ;
    	}
    	map.put("dataDicList", dataDicRes.findByOrgi(super.getOrgi(request))) ;
    	return request(super.createAppsTempletResponse("/apps/business/view/index"));
    }
}