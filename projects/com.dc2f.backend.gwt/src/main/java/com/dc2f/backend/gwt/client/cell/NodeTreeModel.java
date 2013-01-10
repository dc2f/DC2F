package com.dc2f.backend.gwt.client.cell;

import com.dc2f.backend.gwt.client.services.DC2FNavigationServiceAsync;
import com.dc2f.backend.gwt.shared.Node;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class NodeTreeModel implements TreeViewModel {

	private DC2FNavigationServiceAsync navigationService;
	private SingleSelectionModel<Node> selectionModel;

	public NodeTreeModel(final DC2FNavigationServiceAsync navigationService,
			final SingleSelectionModel<Node> selectionModel) {
		this.navigationService = navigationService;
		this.selectionModel = selectionModel;
	}

	public <T> NodeInfo<?> getNodeInfo(T value) {
		AsyncNodeDataProvider dataProvider = new AsyncNodeDataProvider(
				navigationService, (Node) value);
		return new DefaultNodeInfo<Node>(dataProvider, new NodeCell(),
				selectionModel, null);
	}

	public boolean isLeaf(Object value) {
		Node node = (Node) value;
		return !node.hasSubNodes();
	}

}
