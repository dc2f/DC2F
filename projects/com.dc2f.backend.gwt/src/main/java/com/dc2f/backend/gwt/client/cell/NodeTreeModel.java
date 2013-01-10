package com.dc2f.backend.gwt.client.cell;

import com.dc2f.backend.gwt.client.services.DC2FNavigationServiceAsync;
import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * tree model for dc2f nodes.
 * @author herbert
 */
public class NodeTreeModel implements TreeViewModel {
	/**
	 * @see DC2FNavigationServiceAsync
	 */
	private DC2FNavigationServiceAsync navigationService;
	/**
	 * model which will always be used for selection.
	 */
	private SelectionModel<DTONodeInfo> selectionModel;

	/**
	 * creates a new tree model with the given service and seletion model.
	 * @param navigationService navigation service to retrieve nodes.
	 * @param selectionModel selection model which will be used for the whole hierarchie.
	 */
	public NodeTreeModel(final DC2FNavigationServiceAsync navigationService,
			final SelectionModel<DTONodeInfo> selectionModel) {
		this.navigationService = navigationService;
		this.selectionModel = selectionModel;
	}
	
	/** {@inheritDoc} */
	public <T> NodeInfo<?> getNodeInfo(final T value) {
		AsyncNodeDataProvider dataProvider = new AsyncNodeDataProvider(
				navigationService, (DTONodeInfo) value);
		return new DefaultNodeInfo<DTONodeInfo>(dataProvider, new NodeCell(),
				selectionModel, null);
	}

	/** {@inheritDoc} */
	public boolean isLeaf(final Object value) {
		DTONodeInfo node = (DTONodeInfo) value;
		return !node.hasSubNodes();
	}

}
