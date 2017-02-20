package permission.common;

import java.util.List;

public interface TreeAware<T> {
	int getId();
	int getParentId();
	String getUrl();
	String getText();
	List<T> getChildren();
}
