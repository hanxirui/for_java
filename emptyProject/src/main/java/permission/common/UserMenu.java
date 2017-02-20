package permission.common;

import java.util.List;

public class UserMenu implements TreeAware<UserMenu> {
	private int tabId;
	private String tabName;

	private int menuId;
	private String text;
	private int parentId;
	private String url;
	private List<UserMenu> children;

	public void setId(int id) {
		this.menuId = id;
	}

	@Override
	public int getId() {
		return menuId;
	}

	@Override
	public int getParentId() {
		return parentId;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public List<UserMenu> getChildren() {
		return children;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setChildren(List<UserMenu> children) {
		this.children = children;
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
