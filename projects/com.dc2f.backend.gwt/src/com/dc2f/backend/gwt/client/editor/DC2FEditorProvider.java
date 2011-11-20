package com.dc2f.backend.gwt.client.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.dc2f.backend.gwt.client.LazyTree;
import com.dc2f.backend.gwt.client.LazyTree.LazyTreeItem;
import com.dc2f.backend.gwt.client.services.DC2FContentService;
import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
	
	private Vector<Editor> availableEditors; 
	
	public DC2FEditorProvider() {
		((ServiceDefTarget) contentService).setServiceEntryPoint(GWT.getModuleBaseURL() + "content");
		editorList = new HorizontalPanel();
		main = new DockPanel();
		main.add(editorList, DockPanel.NORTH);
		HorizontalPanel statusButtonList = new HorizontalPanel();
		statusButtonList.add(closeButton);
		saveButton.setEnabled(false);
		statusButtonList.add(saveButton);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				save();
			}
		});
		main.add(statusButtonList, DockPanel.SOUTH);
		initWidget(main);
		
		//TODO better get this list dynamically
		availableEditors = new Vector<Editor>();
		Editor attributeEditor = new AttributeEditor(this);
		availableEditors.add(attributeEditor);
		Editor sourceEditor = new SourceEditor(this);
		availableEditors.add(sourceEditor);
	}
	
	protected void save() {
		// TODO Auto-generated method stub
		contentService.saveNode(actualNode, new AsyncCallback<ContentNode>() {
			public void onSuccess(ContentNode result) {
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
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
		HashMap<String, Widget> editorButtons = new HashMap<String, Widget>(); 
		//Check old editors for compatiblity
		for(Widget editButton : editorList) {
			String name = ((Button) editButton).getText();
			if(getEditor(name).isUsableFor(actualNode)) {
				editorButtons.put(name, editButton);
			} else {
				editorList.remove(editButton);
			}
		}
		//Add new editors for this article
		for(Editor editor : getAvailableEditors()) {
			String name = editor.getName();
			if(editor.isUsableFor(actualNode) && !editorButtons.containsKey(name)) {
				final Button editButton = new Button(name);
				editor.loadEditorOnButtonClick(editButton);
				editorList.add(editButton);
			}
		}
	}
	
	private Editor getEditor(String name) {
		for(Editor editor : getAvailableEditors()) {
			if(editor.getName().equals(name)) {
				return editor;
			}
		}
		return null;
	}

	private Collection<Editor> getAvailableEditors() {
		return availableEditors;
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
