package com.dc2f.backend.gwt.client;

import com.dc2f.backend.gwt.client.editor.DC2FEditorProviderUIBinder;
import com.dc2f.backend.gwt.client.services.DC2FNavigationService;
import com.dc2f.backend.gwt.client.services.DC2FNavigationServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DC2FBackend implements EntryPoint {

	private final DC2FNavigationServiceAsync navigationService = GWT.create(DC2FNavigationService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		((ServiceDefTarget) navigationService).setServiceEntryPoint(GWT.getModuleBaseURL() + "navigation");
		final DockPanel main = new DockPanel();
		main.getElement().setId("DC2FMain");
		final LazyTree navigation = new LazyTree(navigationService);
		navigation.getElement().setId("DC2FNavigation");
		main.add(navigation, DockPanel.WEST);

		final DC2FEditorProviderUIBinder editorFrame = new DC2FEditorProviderUIBinder();
		editorFrame.bindToNavigation(navigation);
		main.add(editorFrame, DockPanel.CENTER);

		RootPanel.get().add(main);

	}
}