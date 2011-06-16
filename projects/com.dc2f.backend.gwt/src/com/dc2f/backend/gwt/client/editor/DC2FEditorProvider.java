package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.client.LazyTree;
import com.dc2f.backend.gwt.client.LazyTree.LazyTreeItem;
import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TreeItem;

public class DC2FEditorProvider extends Composite {

	ContentNode actualNode;
	
	DC2FContentServiceAsync contentService = GWT.create(DC2FContentServiceAsync.class);
	
	public DC2FEditorProvider() {
		((ServiceDefTarget) contentService).setServiceEntryPoint(GWT.getModuleBaseURL() + "content");
		final Button editButton = new Button("Edit");
		final Button viewButton = new Button("View");
		final HorizontalPanel buttonList = new HorizontalPanel();
		buttonList.add(viewButton);
		buttonList.add(editButton);
		final DockPanel main = new DockPanel();
		main.add(buttonList, DockPanel.NORTH);
		initWidget(main);
	}
	
	public void bindToNavigation(LazyTree navigation) {
		navigation.addSelectionHandler(new SelectionHandler<TreeItem>() {
			public void onSelection(SelectionEvent<TreeItem> event) {
				LazyTreeItem item = (LazyTreeItem) event.getSelectedItem();
				System.out.println("selected node " + item.getPath());
				contentService.getNodeForPath(item.getPath(), new AsyncCallback<ContentNode>() {
					public void onSuccess(ContentNode result) {
						actualNode = result;
						refreshEditors();
					}
					
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}

	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
	}
	
	public void refreshEditors() {
		// TODO Auto-generated method stub
		
	}

}
