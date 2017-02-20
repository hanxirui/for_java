package permission.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import permission.common.TreeAware;

public class RSysRes implements TreeAware<RSysRes> {
	private int srId;
	private int parentId;
	private String resName;
	private String url;
	private int tabId;
	private String tabName;

	private List<RSysRes> children;
	private List<RSysFunction> sysFuns;

	public void setSrId(int srId) {
		this.srId = srId;
	}

	public int getSrId() {
		return this.srId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Override
	public int getParentId() {
		return this.parentId;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResName() {
		return this.resName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	@JSONField(name = "_parentId")
	public int getEasyUIParentId() {
		return parentId;
	}

	@Override
	public List<RSysRes> getChildren() {
		return children;
	}

	public void setChildren(List<RSysRes> children) {
		this.children = children;
	}

	@Override
	public int getId() {
		return this.srId;
	}

	@Override
	public String getText() {
		return resName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return this.srId == ((RSysRes) obj).srId;
	}

	public List<RSysFunction> getSysFuns() {
		return sysFuns;
	}

	public void setSysFuns(List<RSysFunction> sysFuns) {
		this.sysFuns = sysFuns;
	}

	public int getTabId() {
		return tabId;
	}

	public void setTabId(int tabId) {
		this.tabId = tabId;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

}