package com.dc2f.backend.client;

import java.util.List;

import com.dc2f.backend.client.services.DC2FNavigationServiceAsync;
import com.dc2f.backend.shared.Node;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class LazyTree extends Tree {

	DC2FNavigationServiceAsync service;
	
	public LazyTree(DC2FNavigationServiceAsync navigationService) {
		service = navigationService;
		loadItems("/", "DC2F");
		this.addOpenHandler(new LazyOpenHandler<LazyTreeItem>());
	}

	private void loadItems(String path, String itemName) {
		LazyTreeItem item = new LazyTreeItem(path, itemName);
		this.addItem(item);
		loadItems(path, item);
	}

	private void loadItems(String path, LazyTreeItem root) {
		service.getNodesForPath(path, new LazyTreeNodeLoadCallback<List<Node>>(root));
	}
	
	class LazyOpenHandler<T extends TreeItem> implements OpenHandler<TreeItem> {

		public void onOpen(OpenEvent event) {
			LazyTree tree = (LazyTree) event.getSource();
			LazyTreeItem item = (LazyTreeItem) event.getTarget();
			System.out.println("opening item " + item.getPath() + " checking its children for their children.");
			LazyTreeItem child = null;
			for (int i = 0; i < item.getChildCount(); i++) {
				child = (LazyTreeItem) item.getChild(i);
				tree.loadItems(child.getPath(), child);
			}
		}
		
	}
	
	public class LazyTreeItem extends TreeItem {
		String path;
		
		public LazyTreeItem(String itemName) {
			super(itemName);
		}

		public LazyTreeItem(String pathForItem, String itemName) {
			this(itemName);
			setPath(pathForItem);
		}

		void setPath(String givenPath) {
			path = givenPath;
		}
		
		String getPath() {
			return path;
		}
	}
	
	class LazyTreeNodeLoadCallback<T extends List<Node>> implements AsyncCallback<T> {

		LazyTreeItem root;
		
		public LazyTreeNodeLoadCallback(LazyTreeItem givenRoot) {
			root = givenRoot;
		}
		
		public void onSuccess(T resultList) {
			for(Node result : resultList) {
				String name = result.getName();
				if(name == null || "".equals(name))name = "---";
				System.out.println("Got node " + name + " for path " + result.getPath() + ".");
				final LazyTreeItem item = new LazyTreeItem(result.getPath(), name);
				root.addItem(item);
			}
		}
		
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

	}
}
