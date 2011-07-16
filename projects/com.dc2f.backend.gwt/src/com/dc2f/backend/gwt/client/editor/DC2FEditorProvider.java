package com.dc2f.backend.gwt.client.editor;

import java.util.Collection;
import java.util.Vector;

import com.dc2f.backend.gwt.client.LazyTree;
import com.dc2f.backend.gwt.client.LazyTree.LazyTreeItem;
import com.dc2f.backend.gwt.client.services.DC2FContentService;
import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class DC2FEditorProvider extends Composite {

	ContentNode actualNode;
	
	DC2FContentServiceAsync contentService = GWT.create(DC2FContentService.class);
	
	HorizontalPanel editorList;
	
	DockPanel main;

	private Widget lastMainWidget;
	
	private Button closeButton = new Button("close");
	
	private Button saveButton = new Button("save");
	
	public DC2FEditorProvider() {
		((ServiceDefTarget) contentService).setServiceEntryPoint(GWT.getModuleBaseURL() + "content");
		editorList = new HorizontalPanel();
		main = new DockPanel();
		main.add(editorList, DockPanel.NORTH);
		HorizontalPanel statusButtonList = new HorizontalPanel();
		statusButtonList.add(closeButton);
		saveButton.setEnabled(false);
		statusButtonList.add(saveButton);
		main.add(statusButtonList, DockPanel.SOUTH);
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
		//Check old editors for compatiblity
		for(Widget editButton : editorList) {
			
		}
		//Add new editors for this article
		for(Editor editor : getAvailableEditors()) {
			final Button editButton = new Button(editor.getName());
			editor.bindToButton(editButton);
			editorList.add(editButton);
		}
	}
	
	private Collection<Editor> getAvailableEditors() {
		Vector<Editor> editors = new Vector<Editor>();
		Editor editor = new AttributeEditor(this);
		editors.add(editor);
		return editors;
	}
	
	protected ContentNode getActualNode() {
		return actualNode;
	}

	public void showEditor(Editor editor) {
		setMain(editor);
		saveButton.setEnabled(false);
	}
	
	private Widget setMain(Widget widget) {
		Widget lastLastMainWidget = null;
		if (lastMainWidget != null) {
			main.remove(lastMainWidget);
		}
		main.add(widget, DockPanel.CENTER);
		lastMainWidget = widget;
		return lastLastMainWidget;
	}

	protected void nodeHasChanged(ChangeEvent event) {
		saveButton.setEnabled(true);
	}

}
