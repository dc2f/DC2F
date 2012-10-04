package com.dc2f.renderer.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.ContentRepositoryFactory;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;

public class NodeTypeDocGenerator {
	public static void main(String[] args) throws IOException, Exception {
		generateDocumentation("../../design/example", new String[] { "src", "../com.dc2f.nodetype/src", "../com.dc2f.core/src" });
	}

	private static void generateDocumentation(String repositoryRoot, String[] sourceDirs) throws IOException, Exception {
		// Search for .json files in the source directories ..
		List<String> nodeTypes = new ArrayList<String>();
		for(String sourceDir : sourceDirs) {
			findNodeTypes(nodeTypes, new File(sourceDir), new File(sourceDir));
		}
		System.out.println("nodeTypes: " + nodeTypes);
		
		// initialize Json Repository
		ContentRepository cr = ContentRepositoryFactory.getInstance().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)repositoryRoot));
		CRSession conn = cr.authenticate(null);
		BranchAccess transaction = conn.openTransaction(null);
		//ContentRepository cr = new SimpleFileContentRepository(new File(repositoryRoot));
		
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.newDocument();
		//Document doc = domImpl.createDocument("test", "dc2f", null);
		
		Element root = doc.createElement("nodetypedoc");
		doc.appendChild(root);
		

		for(String nodeTypeName : nodeTypes) {
			NodeType nodeType = transaction.getNodeType(nodeTypeName);
			System.out.println("nodeType: " + nodeType);
			System.out.println(" comment: " + nodeType.getNodeTypeInfo().get("_comment"));
			Element nodeTypeElement = doc.createElement("nodetype");
			nodeTypeElement.setAttribute("name", nodeType.getNodeTypeInfo().getName());
			nodeTypeElement.setAttribute("path", nodeType.getNodeTypeInfo().getPath());
			nodeTypeElement.setAttribute("comment", (String) nodeType.getNodeTypeInfo().get("_comment"));
			NodeType parent = nodeType.getNodeTypeInfo().getParentNodeType();
			if (parent != null && parent.getNodeTypeInfo() != null) {
				nodeTypeElement.setAttribute("extends", parent.getNodeTypeInfo().getPath());
			}
			
			AttributesDefinition attributesDef = nodeType.getAttributeDefinitions();
			if (attributesDef != null && attributesDef.getAttributeNames() != null) {
				for(String attributeName : attributesDef.getAttributeNames()) {
					Element attrElement = doc.createElement("property");
					Node attrDef = attributesDef.getAttributeDefinition(attributeName);
					attrElement.setAttribute("name", attributeName);
					attrElement.setAttribute("type", (String) attrDef.get("type"));
					attrElement.setAttribute("typeofsubnodes", (String) attrDef.get("typeofsubnodes"));
					attrElement.setAttribute("comment", (String) attrDef.get("_comment"));
					nodeTypeElement.appendChild(attrElement);
				}
			}
			
			
			root.appendChild(nodeTypeElement);
		}
		
		
		
		
		DOMSource source = new DOMSource(doc);
		FileOutputStream outputStream = new FileOutputStream("target/xmldoc.xml");
		Result result = new StreamResult(new OutputStreamWriter(outputStream));
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(source, result);
		outputStream.close();
	}

	private static void findNodeTypes(List<String> nodeTypes, File rootDir, File dir) throws IOException {
		for(File file : dir.listFiles()) {
			if (file.isDirectory()) {
				findNodeTypes(nodeTypes, rootDir, file);
			} else if (file.isFile() && file.getName().endsWith(".json")) {
				nodeTypes.add("classpath:" + file.getCanonicalPath().replace(rootDir.getCanonicalPath() + "/", "").replace(".json",""));
			}
		}
	}
	
	
}
