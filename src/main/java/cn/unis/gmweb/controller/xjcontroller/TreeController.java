package cn.unis.gmweb.controller.xjcontroller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import cn.unis.gmweb.dynamic.CustomerContextHolder;
import cn.unis.gmweb.utils.ProperUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.unis.gmweb.pojo.Tree;
import cn.unis.gmweb.service.TreeService;

/**
 * 获取查询下拉树
 * @author lgf
 *
 */
@Controller
@RequestMapping("/xj/tree")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TreeController {

	@Resource
	private TreeService treeService;
	/**
	 * 火探漏电流温度查询树
	 * @param lineName 线路名称 A36
	 * @return
	 */
	@RequestMapping("httree/{lineName}")
	@ResponseBody
	public List<Tree> findHtTree(@PathVariable String lineName) {

		List<Tree> treeList =treeService.findHtTree(lineName);
		System.err.println(treeList);
		return treeList;
	}
	
	/**
	 * 全电量设备查询树
	 * @return
	 */
	@RequestMapping("qdltree/{lineName}")
	@ResponseBody
	public List<Tree> findqdlTree(@PathVariable String lineName){
		CustomerContextHolder.setCustomerType("dataSource_bigdata");
		List<Tree> qdlList =treeService.findqdlTree(lineName);
		System.out.println(qdlList.size()+":"+qdlList.toString());
		return qdlList;
	}
	/**
	 * model 水泵模型
	 * @return
	 */
	@RequestMapping("pump/{sbid}")
	@ResponseBody
	public String findModel(@PathVariable String sbid){
		CustomerContextHolder.setCustomerType("dataSource_bigdata");
		String model =treeService.findThresholdModel(sbid);
		System.err.println(model+"-----------");
		LinkedHashMap<String, String> tmp= treeService.findThresholdModelMap(sbid);
		System.err.println(tmp+"000000000000");
		return model;
	}
	
}
