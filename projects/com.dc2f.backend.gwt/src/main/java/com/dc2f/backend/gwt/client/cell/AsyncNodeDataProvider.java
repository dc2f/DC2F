package com.dc2f.backend.gwt.client.cell;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dc2f.backend.gwt.client.services.DC2FNavigationServiceAsync;
import com.dc2f.backend.gwt.shared.Node;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class AsyncNodeDataProvider extends AsyncDataProvider<Node> {
	private Logger logger = Logger.getLogger(AsyncNodeDataProvider.class
			.getName());

	private DC2FNavigationServiceAsync navigationService;
	private Node node;

	public AsyncNodeDataProvider(DC2FNavigationServiceAsync navigationService,
			Node value) {
		// TODO
		this.navigationService = navigationService;
		this.node = value;
	}

	@Override
	protected void onRangeChanged(HasData<Node> display) {
		final Range range = display.getVisibleRange();
		logger.info("onRangeChanged(" + display.getVisibleRange() + ")");
		navigationService.getNodesForPath(node.getPath(),
				new AsyncCallback<List<Node>>() {

					public void onSuccess(List<Node> result) {
						int toIndex = Math.min(
								range.getStart() + range.getLength() + 1,
								result.size());
						int fromIndex = Math.min(range.getStart(), toIndex);
						if (toIndex < 0) {
							logger.info("fromIndex is < 0 - calling updateRowData with null.");
							updateRowData(range.getStart(),
									Collections.<Node> emptyList());
							return;
						}
						List<Node> values = result.subList(fromIndex, toIndex);
						updateRowData(range.getStart(), values);
					}

					public void onFailure(Throwable caught) {
						logger.log(
								Level.SEVERE,
								"Error while getting children of "
										+ node.getPath(), caught);
					}
				});
	}

}
