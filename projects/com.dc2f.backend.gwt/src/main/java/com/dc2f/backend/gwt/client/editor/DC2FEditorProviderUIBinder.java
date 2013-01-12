/**
 * 
 */
package com.dc2f.backend.gwt.client.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
 * 
 */
public class DC2FEditorProviderUIBinder extends Composite {

	/**
	 * node which is edited.
	 */
	private DTOEditableNode actualNode;

	/**
	 * server side content service to retrieve data from CR.
	 */
	private DC2FContentServiceAsync contentService = GWT.create(DC2FContentService.class);

	/**
	 * list of all available editors.
	 */
	private List<Editor> availableEditors;

	/**
	 * the main widget of the active editor.
	 */
	private Widget lastMainWidget;

	/**
	 * UI binder singleton instance.
	 */
	private static DC2FEditorProviderUIBinderUiBinder uiBinder = GWT.create(DC2FEditorProviderUIBinderUiBinder.class);
	
	/**
	 * button to close the editor.
	 */
	@UiField
	protected Button closeButton;

	/**
	 * button to save editor changes.
	 */
	@UiField
	protected Button saveButton;
	
	/**
	 * list of editors to choose from.
	 */
	@UiField
	protected HorizontalPanel editorList;
	
	/**
	 * panel containing the editor.
	 */
	@UiField
	protected SimplePanel centerPanel;
	
	/**
	 * ?
	 */
	@UiField
	protected Label selectionLabel;

	/**
	 * ui binder.
	 */
	interface DC2FEditorProviderUIBinderUiBinder extends UiBinder<Widget, DC2FEditorProviderUIBinder> {
	}
	
	
	/**
	 * @return retrieves content service used to retrieve nodes, etc.
	 */
	public DC2FContentServiceAsync getContentService() {
		return contentService;
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder template. In other
	 * words, it can be used in other *.ui.xml files as follows: 
	 * &lt;ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder" xmlns:g="urn:import:**user's package**"&gt;
	 * &lt;g:**UserClassName**&gt;Hello!&lt;/g:**UserClassName&gt; &lt;/ui:UiBinder&gt; Note that depending on the
	 * widget that is used,
	 * it may be necessary to implement HasHTML instead of HasText.
	 */
	public DC2FEditorProviderUIBinder() {
		((ServiceDefTarget) contentService)
				.setServiceEntryPoint(GWT.getModuleBaseURL() + "content");
		initWidget(uiBinder.createAndBindUi(this));

		saveButton.setEnabled(false);

		saveButton.addClickHandler(new ClickHandler() {

			public void onClick(final ClickEvent event) {
				save();
			}
		});

		// TODO better get this list dynamically
		availableEditors = new Vector<Editor>();
		Editor attributeEditor = new AttributeEditor(this);
		availableEditors.add(attributeEditor);
		Editor sourceEditor = new SourceEditor(this);
		availableEditors.add(sourceEditor);

	}

	/**
	 * save the editor.
	 */
	protected void save() {
		// TODO Auto-generated method stub
		contentService.saveNode(actualNode, new AsyncCallback<DTOEditableNode>() {
			public void onSuccess(final DTOEditableNode result) {
			}

			public void onFailure(final Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * binds to navigation selection events.
	 * @param selectionModel the selection model handling the selection.
	 */
	public void bindToNavigation(final SingleSelectionModel<DTONodeInfo> selectionModel) {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(final SelectionChangeEvent event) {
				DTONodeInfo node = selectionModel.getSelectedObject();
				System.out.println("selected node " + node.getPath());
				if (lastMainWidget != null) {
					centerPanel.remove(lastMainWidget);
				}
				selectionLabel.setText("Selected Node: " + node.getPath());
				contentService.getEditableNodeForPath(node.getPath(), new AsyncCallback<DTOEditableNode>() {
					public void onSuccess(final DTOEditableNode result) {
						actualNode = result;
						refreshEditors();
						chooseDefaultEditor(result);
					}

					public void onFailure(final Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
			}
		});
	}

	/**
	 * called when module was loaded.
	 */
	public void onModuleLoad() {
		// TODO Auto-generated method stub

	}

	/**
	 * refreshes the list of editors.
	 */
	public void refreshEditors() {
		HashMap<String, Widget> editorButtons = new HashMap<String, Widget>();
		// Check old editors for compatiblity
		for (Widget editButton : editorList) {
			String name = ((Button) editButton).getText();
			if (getEditor(name).isUsableFor(actualNode)) {
				editorButtons.put(name, editButton);
			} else {
				editorList.remove(editButton);
			}
		}
		// Add new editors for this article
		for (Editor editor : getAvailableEditors()) {
			String name = editor.getName();
			if (editor.isUsableFor(actualNode) && !editorButtons.containsKey(name)) {
				final Button editButton = new Button(name);
				editor.loadEditorOnButtonClick(editButton);
				editorList.add(editButton);
			}
		}
	}

	/**
	 * returns an editor with the given name.
	 * @param name name of the editor to return
	 * @return the editor with the given name, or null if none was found.
	 */
	private Editor getEditor(final String name) {
		for (Editor editor : getAvailableEditors()) {
			if (editor.getName().equals(name)) {
				return editor;
			}
		}
		return null;
	}

	/**
	 * @return all available editors.
	 */
	private Collection<Editor> getAvailableEditors() {
		return availableEditors;
	}

	/**
	 * @return currently edited node.
	 */
	protected DTOEditableNode getActualNode() {
		return actualNode;
	}

	/**
	 * shows the given editor.
	 * @param editor the editor to show.
	 */
	public void showEditor(final Editor editor) {
		setMain(editor);
		saveButton.setEnabled(false);
	}

	/**
	 * select a default editro for the given node.
	 * @param node node to modify.
	 */
	private void chooseDefaultEditor(final DTOEditableNode node) {
		Editor editor = availableEditors.get(0);
		editor.loadNode(node);
		setMain(editor);
	}

	/**
	 * set the main widget of the editor.
	 * @param widget main widget of the editor
	 * @return the widget (which was passed in)
	 */
	private Widget setMain(final Widget widget) {
		Widget lastLastMainWidget = null;
		if (lastMainWidget != null) {
			centerPanel.remove(lastMainWidget);
		}
		centerPanel.add(widget);
		lastMainWidget = widget;
		return lastLastMainWidget;
	}

	/**
	 * node has changed.
	 * @param event hange event.
	 */
	protected void nodeHasChanged(final ChangeEvent event) {
		saveButton.setEnabled(true);
	}

}
