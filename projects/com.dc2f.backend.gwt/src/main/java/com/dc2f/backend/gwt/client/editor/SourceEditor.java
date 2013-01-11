package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * The source editor allows the user to edit the source of every node as it is. This is intended for experienced users.
 * 
 * @author bigbear3001
 * 
 */
public class SourceEditor extends Editor {

	/**
	 * style name to use if the editor is in an error state (e.g. invalid source code);
	 */
	private static final String ERROR_STYLE_NAME = "error";

	/**
	 * text area containing the json code.
	 */
	private TextArea editor;

	/**
	 * holds the initialized change handler that helps us to save changes in the source code into the attributes.
	 */
	private SourceEditorChangeHandler changeHandler;

	/**
	 * holds the currently loaded Node.
	 */
	private DTOEditableNode loadedNode;

	/**
	 * instantiate a new source editor.
	 * @param dc2fEditorProviderUIBinder binder
	 */
	public SourceEditor(final DC2FEditorProviderUIBinder dc2fEditorProviderUIBinder) {
		super(dc2fEditorProviderUIBinder);
		setName("Source");
		final DockPanel main = new DockPanel();
		editor = new TextArea();
		main.getElement().setId("DC2FSourceEditor");
		main.add(editor, DockPanel.CENTER);
		initWidget(main);
		changeHandler = new SourceEditorChangeHandler("");
	}

	@Override
	public void loadNode(final DTOEditableNode node) {
		DC2FContentServiceAsync contentService = getContentService();
		contentService.getSource(node, new AsyncCallback<String>() {

			public void onFailure(final Throwable caught) {
				editor.setText("Failed to load source for Node.");
				editor.setEnabled(false);
			}

			public void onSuccess(final String result) {
				editor.setText(result);
				editor.setEnabled(true);
				changeHandler.setSource(result);
			}
		});
		loadedNode = node;
		editor.addChangeHandler(getChangeHandler());
	}

	@Override
	protected ChangeHandler getChangeHandler() {
		return changeHandler;
	}

	/**
	 * detected a change.
	 */
	private final class SourceEditorChangeHandler extends NodeChangedChangeHandler {

		/**
		 * node before the change.
		 */
		private DTOEditableNode lastLoadedNode;

		/**
		 * source of the node.
		 */
		private String lastSource;

		/**
		 * a new change handler for source editor.
		 * @param source source code of the node.
		 */
		private SourceEditorChangeHandler(final String source) {
			setSource(source);
		}

		/**
		 * change the source, checks for validity.
		 * @param source new source of the node
		 * @return the new source (ie. the input source if it was valid.)
		 */
		private String setSource(final String source) {
			String result = lastSource;
			if (validate(source) != null) {
				lastSource = source;
			} else {
				lastSource = null;
			}
			return result;
		}

		/**
		 * notified of changes to the node.
		 * @param event event of the change.
		 */
		public void onChange(final ChangeEvent event) {
			if (lastLoadedNode == null || lastLoadedNode.same(loadedNode)) {
				String source = editor.getText();
				JSONValue last;
				if (lastSource != null) {
					last = JSONParser.parseStrict(lastSource);
				} else {
					last = new JSONObject();
				}
				JSONValue current = validate(source);
				if (current != null) {
					JSONObject difference = JSONComparator.compare(current, last).isObject();
					if (difference != null) {
						for (String attributeName : difference.keySet()) {
							// TODO: check for other values than string
							if (difference.get(attributeName).isString() != null) {
								loadedNode.set(attributeName, difference.get(attributeName).isString().stringValue());
							}
						}
						lastSource = source;
					}
				}
			} else {
				lastLoadedNode = loadedNode;
			}
			super.onChange(event);
		}

	}

	/**
	 * validate the given source string.
	 * @param text json source to check.
	 * @return a parsed json object, or null if text is not a valid json object.
	 */
	private JSONValue validate(final String text) {
		try {
			this.removeStyleName(ERROR_STYLE_NAME);
			if (text != null && !text.isEmpty()) {
				return JSONParser.parseStrict(text);
			} else {
				return new JSONObject();
			}
		} catch (JSONException e) {
			// TODO maybe we can get a line number here and give the user a more precise error message
			this.addStyleName(ERROR_STYLE_NAME);
			return null;
		}
	}

}
