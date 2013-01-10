/**
 * 
 */
package com.dc2f.backend.gwt.client.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.dc2f.backend.gwt.client.services.DC2FContentService;
import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author herbert
 *
 */
public class DC2FEditorProviderUIBinder extends Composite {

	DTOEditableNode actualNode;
	
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

	
	protected void save() {
		// TODO Auto-generated method stub
		contentService.saveNode(actualNode, new AsyncCallback<DTOEditableNode>() {
			public void onSuccess(DTOEditableNode result) {
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void bindToNavigation(final SingleSelectionModel<DTONodeInfo> selectionModel) {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				DTONodeInfo node = selectionModel.getSelectedObject();
				System.out.println("selected node " + node.getPath());
				if (lastMainWidget != null) {
					centerPanel.remove(lastMainWidget);
				}
				selectionLabel.setText("Selected Node: " + node.getPath());
				contentService.getEditableNodeForPath(node.getPath(), new AsyncCallback<DTOEditableNode>() {
					public void onSuccess(DTOEditableNode result) {
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
	
	protected DTOEditableNode getActualNode() {
		return actualNode;
	}

	public void showEditor(Editor editor) {
		setMain(editor);
		saveButton.setEnabled(false);
	}
	
	private void chooseDefaultEditor(DTOEditableNode node) {
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
