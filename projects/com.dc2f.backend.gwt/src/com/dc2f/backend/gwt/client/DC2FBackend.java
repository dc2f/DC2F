package com.dc2f.backend.gwt.client;

import com.dc2f.backend.gwt.client.services.DC2FNavigationService;
import com.dc2f.backend.gwt.client.services.DC2FNavigationServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DC2FBackend implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	private final DC2FNavigationServiceAsync navigationService = GWT.create(DC2FNavigationService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		((ServiceDefTarget) navigationService).setServiceEntryPoint(GWT.getModuleBaseURL() + "navigation");
		final DockPanel main = new DockPanel();

		final LazyTree navigation = new LazyTree(navigationService);
		main.add(navigation, DockPanel.WEST);

		final HTML content = new HTML("<!-- here comes the main module -->");
		main.add(content, DockPanel.CENTER);

		RootPanel.get().add(main);

	}
}
