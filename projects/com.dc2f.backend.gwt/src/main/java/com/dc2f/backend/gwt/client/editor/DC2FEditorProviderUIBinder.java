/**
 * 
 */
package com.dc2f.backend.gwt.client.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.dc2f.backend.gwt.client.LazyTree;
import com.dc2f.backend.gwt.client.LazyTree.LazyTreeItem;
import com.dc2f.backend.gwt.client.services.DC2FContentService;
import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.ContentNode;
import com.dc2f.backend.gwt.shared.DC2FException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Label;

/**
 * @author herbert
 *
 */
public class DC2FEditorProviderUIBinder extends Composite {

	ContentNode actualNode;
	
	public DC2FContentServiceAsync getContentService() {
		return contentService;
	}
	
	DC2FContentServiceAsync contentService = GWT.create(DC2FContentService.class);

	private Vector<Editor> availableEditors; 

	private Widget lastMainWidget;

	private static DC2FEditorProviderUIBinderUiBinder uiBinder = GWT
			.create(DC2FEditorProviderUIBinderUiBinder.class);
	@UiField Button closeButton;
	@UiField Button saveButton;
	@UiField HorizontalPanel editorList;
	@UiField SimplePanel centerPanel;
	@UiField Label selectionLabel;
	
	private Timer saveButtonReset = new ResetSaveButton();

	interface DC2FEditorProviderUIBinderUiBinder extends
			UiBinder<Widget, DC2FEditorProviderUIBinder> {
	}

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public DC2FEditorProviderUIBinder() {
		((ServiceDefTarget) contentService).setServiceEntryPoint(GWT.getModuleBaseURL() + "content");
		initWidget(uiBinder.createAndBindUi(this));
		
		
		saveButton.setEnabled(false);
		
		saveButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				save();
			}
		});

		//TODO better get this list dynamically
		availableEditors = new Vector<Editor>();
		Editor attributeEditor = new AttributeEditor(this);
		availableEditors.add(attributeEditor);
		Editor sourceEditor = new SourceEditor(this);
		availableEditors.add(sourceEditor);

	}

	private class ResetSaveButton extends Timer {

		@Override
		public void run() {
			saveButton.setEnabled(false);
			saveButton.setText("save");
		}
		
	}
	
	
	protected void save() {
		// TODO Auto-generated method stub
		contentService.saveNode(actualNode, new AsyncCallback<ContentNode>() {
			public void onSuccess(ContentNode result) {
				saveButton.setText("saved");
				saveButton.setEnabled(false);
				saveButtonReset.schedule(1000);
			}

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
				if (lastMainWidget != null) {
					centerPanel.remove(lastMainWidget);
				}
				selectionLabel.setText("Selected Node: " + item.getPath());
				contentService.getNodeForPath(item.getPath(), new AsyncCallback<ContentNode>() {
					public void onSuccess(ContentNode result) {
						actualNode = result;
						refreshEditors();
						chooseDefaultEditor(result);
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
	
	private void chooseDefaultEditor(ContentNode node) {
		Editor editor = availableEditors.get(0);
		editor.loadNode(node);
		setMain(editor);
	}
	
	private Widget setMain(Widget widget) {
		Widget lastLastMainWidget = null;
		if (lastMainWidget != null) {
			centerPanel.remove(lastMainWidget);
		}
		centerPanel.add(widget);
		lastMainWidget = widget;
		return lastLastMainWidget;
	}

	protected void nodeHasChanged(ChangeEvent event) {
		saveButton.setEnabled(true);
	}

}
