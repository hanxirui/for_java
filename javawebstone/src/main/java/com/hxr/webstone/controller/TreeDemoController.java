package com.hxr.webstone.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ez.framework.core.servlet.BaseDispatcherServlet;
import com.hxr.webstone.pojo.TreeDemoPojo;
import com.hxr.webstone.util.tree.TreeConfig;
import com.hxr.webstone.util.tree.TreeView;

@RestController
@RequestMapping("/treedemo")
public class TreeDemoController extends BaseController {

	/**
	 * <code>S_WIRELESS_LOCATION_ROOT_ALL</code> - {description}.
	 */
	private static final String S_ROOT_ALL = "全部";

	/**
	 * <code>S_NUMBER_1</code> - {description}.
	 */
	private static final String S_NUMBER_1 = "-1";

	@RequestMapping("/index")
	public ModelAndView index() throws Exception {
		ModelAndView t_view = BaseDispatcherServlet.getModeAndView();
		// 两种生成树的方式：一种是用 TreeView + Provider；另一种是TreeUtil.create；
		// 前面一种应对节点复杂的情况，后面一种适用于节点内容单一的情况。
		// 两种生成树的方式，第一种
		String t_DemoTree = getDemoTree(false, null);
		t_view.getModelMap().put("treedemo", t_DemoTree);

		// 第二种
		// TreeView t_view = new TreeView();
		// t_view.setInput(geneTreeData(false, null));
		// t_view.setContentProvider(t_provider);
		// t_view.setLabelProvider(t_provider);
		// t_DemoTree = t_view.draw("treedemo");
		t_view.setViewName("/treedemo/treedemo");

		return t_view;
	}

	/**
	 * @return 打开节点关联的界面
	 * @throws Exception
	 */
	@RequestMapping("/openNode")
	public ModelAndView openNode() throws Exception {
		ModelAndView t_view = BaseDispatcherServlet.getModeAndView();
		// String t_DemoTree = getDemoTree(false);
		//
		// t_view.getModelMap().put("treedemo", t_DemoTree);
		t_view.setViewName("/treedemo/treedemo");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
		return t_view;
	}

	/**
	 * 异步树
	 * 
	 * @return 获得子节点
	 * @throws Exception
	 */
	@RequestMapping("/getChildNode")
	public void getChildNode(final HttpServletRequest request, final HttpServletResponse response, final String nodeId)
			throws Exception {

		List<TreeDemoPojo> subNodes = geneTreeData(true, nodeId);

		TreeView t_view = new TreeView();
		t_view.setInput(subNodes);
		// t_view.setContentProvider(t_provider);
		// t_view.setLabelProvider(t_provider);
		String t_resInstSubTree = t_view.drawAsyNode();
		response.setCharacterEncoding("UFT-8");
		response.getWriter().print(t_resInstSubTree);
	}

	/**
	 * 导出组织结构树
	 * 
	 * @return String
	 * @throws Exception
	 *             页面异常
	 */
	private String getDemoTree(final boolean isSub, final String nodeId) throws Exception {

		String t_treeByHtml;
		try {
			TreeConfig t_treeConfig = new TreeConfig();
			t_treeConfig.setIcon(true);
			t_treeConfig.setExpand(true);
			t_treeByHtml = "";
			List<TreeDemoPojo> t_list = geneTreeData(isSub, nodeId);
			if (!isSub) {
				t_treeByHtml = DemoTree.create("treedemo", addRootNode(t_list), t_treeConfig);
			} else {
				t_treeByHtml = DemoTree.create("treedemo", t_list, t_treeConfig);
			}

		} catch (Exception t_e) {
			t_e.printStackTrace();
			throw new Exception(t_e.getMessage());
		}

		return t_treeByHtml;

	}

	private List<TreeDemoPojo> geneTreeData(final boolean isSub, final String nodeId) {
		List<TreeDemoPojo> t_pojos = new ArrayList<TreeDemoPojo>();
		if (isSub) {

			TreeDemoPojo father = new TreeDemoPojo();
			father.setId(nodeId + "11");
			father.setParentId(nodeId);
			father.setName("subfather");
			t_pojos.add(father);

			TreeDemoPojo son1 = new TreeDemoPojo();
			son1.setId(nodeId + "12");
			son1.setParentId(nodeId + "11");
			son1.setName("subson1");
			t_pojos.add(son1);

			TreeDemoPojo son2 = new TreeDemoPojo();
			son2.setId(nodeId + "13");
			son2.setParentId(nodeId + "11");
			son2.setName("subson2");
			t_pojos.add(son2);

			TreeDemoPojo grandson1 = new TreeDemoPojo();
			grandson1.setId(nodeId + "14");
			grandson1.setParentId(nodeId + "12");
			grandson1.setName("subgrandson1");
			t_pojos.add(grandson1);

			TreeDemoPojo grandson2 = new TreeDemoPojo();
			grandson2.setId(nodeId + "15");
			grandson2.setParentId(nodeId + "12");
			grandson2.setName("subgrandson2");
			t_pojos.add(grandson2);

		} else {
			TreeDemoPojo father = new TreeDemoPojo();
			father.setId("1");
			father.setParentId("-1");
			father.setName("father");
			t_pojos.add(father);

			TreeDemoPojo son1 = new TreeDemoPojo();
			son1.setId("2");
			son1.setParentId("1");
			son1.setName("son1");
			t_pojos.add(son1);

			TreeDemoPojo son2 = new TreeDemoPojo();
			son2.setId("3");
			son2.setParentId("1");
			son2.setName("son2");
			t_pojos.add(son2);

			TreeDemoPojo grandson1 = new TreeDemoPojo();
			grandson1.setId("4");
			grandson1.setParentId("2");
			grandson1.setName("grandson1");
			t_pojos.add(grandson1);

			TreeDemoPojo grandson2 = new TreeDemoPojo();
			grandson2.setId("5");
			grandson2.setParentId("2");
			grandson2.setName("grandson2");
			t_pojos.add(grandson2);
		}
		return t_pojos;
	}

	/**
	 * 增加【全部】节点到树上。
	 * 
	 * @param list
	 *            list
	 * @return List<LocationPojo>
	 * @throws Exception
	 *             异常
	 */
	private static List<TreeDemoPojo> addRootNode(final List<TreeDemoPojo> list) throws Exception {
		List<TreeDemoPojo> t_newTree = new ArrayList<TreeDemoPojo>();

		TreeDemoPojo t_root = new TreeDemoPojo();
		t_root.setId(S_NUMBER_1);
		t_root.setParentId(S_NUMBER_1);
		t_root.setName(S_ROOT_ALL);

		t_newTree.add(t_root);
		for (TreeDemoPojo t_treePojo : list) {
			System.out.println(t_treePojo.getName() + "=" + t_treePojo.getParentId());
			if (t_treePojo.getParentId().equals(S_NUMBER_1)) {
				t_treePojo.setParentId(S_NUMBER_1);
			}

			t_newTree.add(t_treePojo);
		}

		return t_newTree;
	}
}
