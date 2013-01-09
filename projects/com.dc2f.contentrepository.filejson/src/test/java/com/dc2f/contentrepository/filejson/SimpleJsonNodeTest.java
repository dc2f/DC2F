package com.dc2f.contentrepository.filejson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributeType;
import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.DefaultNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.NodeTypeInfo;
import com.dc2f.contentrepository.exception.UnknownAttributeException;

public class SimpleJsonNodeTest {
	
	
	
	
	private static final String JSON_FILE_NAME = "test.json";
	
	private static final File REPOSITORY_DIR = new File(SimpleJsonNodeTest.class.getResource(JSON_FILE_NAME).getPath()).getParentFile();

	@Test
	public void testSimpleAttributes() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		assertEquals("String attribute was not read correctly.", "string", node.get("testString"));
		assertEquals("Integer attribute was not read correctly.", 1, node.get("testInteger"));
		//This should return an integer but returns a string.
		//assertEquals("Integer attribute was not read correctly from String.", 1, node.get("testStringInteger"));
		
		assertEquals("Boolean attribute was not returned correctly.", true, node.get("testBoolean"));

	}

	
	@Test
	public void testBinaryAttributes() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		assertEquals("Blob attribute was not read correctly.", "This should be an image", IOUtils.toString(((FileInputStream) node.get("image"))));
		assertEquals("Clob attribute was not read correctly.", "This can be a large textfile", node.get("text"));
	}
	
	@Test
	public void testPathEquals() throws IOException, JSONException {
		assertEquals("Path that only differ by the last slash should be equal.", getSimpleNode(JSON_FILE_NAME, "/test"), getSimpleNode(JSON_FILE_NAME, "/test/"));
	}


	private SimpleJsonNode getSimpleNode(String file) throws IOException, JSONException {
		return getSimpleNode(file, "/");
	}
	
	private SimpleJsonNode getSimpleNode(String file, String path) throws IOException, JSONException {
		
		InputStream stream = null;
		if(file.startsWith("/")) {
			stream = getClass().getResourceAsStream(file.substring(1));
		} else {
			stream = getClass().getResourceAsStream(file);
		}
		if(stream != null) {
			String json = IOUtils.toString(stream);
			JSONObject object = new JSONObject(json);
			NodeType type = new TestNodeType();
			SimpleBranchAccess braccess = new TestBranchAccess();
			
			SimpleJsonNode node = new SimpleJsonNode(braccess, path, object, type);
			return node;
		}
		return null;
	}
	
	@Test
	public void testReferenceAttributes() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		assertEquals("Absolute reference attribute was not read correctly.", getSimpleNode("test2.json"), node.get("testAbsoluteReference"));
		assertEquals("Relative reference attribute was not read correctly.", getSimpleNode("test2.json"), node.get("testRelativeReference"));
	}
	
	@Test
	public void testTypeAttributes() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		assertEquals("Type attribute was not read correctly.", "TestJSON", node.get("type"));
		assertEquals("Class attribute was not read correctly.", "org.json.JSONObject", node.get("class"));
	}
	
	@Test
	public void testNodeTypeAttribute() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		assertEquals("Type attribute was not read correctly.", new TestNodeType(), node.get("nodetype"));
	}
	
	@Test
	public void testNodeAttribute() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		
		Node inlineNode = (Node) node.get("node");
		assertEquals("Inline node cannot resolve attributes.", "node", inlineNode.get("testString"));
	
		Node subInlineNode = (Node) inlineNode.get("node");
		assertEquals("Inline node with nodetype cannot resolve attributes.", "node.node", subInlineNode.get("testString"));

		Node defaultInlineNode = (Node) node.get("node-default");
		assertEquals("Default inline node (without nodetype) has not the default class.", DefaultNodeType.class, defaultInlineNode.getNodeType().getClass());
	}
	
	
	@Test
	public void testNulls() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		Object object = node.get("testNullString");
		assertNull("Null string was not null.", object);
		object = node.get("testNullInteger");
		assertNull("Null integer was not null.", object);
		object = node.get("testNullBoolean");
		assertNull("Null boolean was not null.", object);
		object = node.get("testNullNode");
		assertNull("Null node was not null.", object);
		object = node.get("testNullSubnodes");
		assertNull("Null subnodes was not null.", object);
		object = node.get("testMissingString");
		assertNull("Missing string attribute was not null.", object);
	}
	
	@Test
	public void testListOfNodesAttribute() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		@SuppressWarnings("unchecked")
		List<Node> subnodes = (List<Node>) node.get("subnodes");
		assertEquals("Number of subnodes isn't correct", 2, subnodes.size());
		assertEquals("First subnode didn't return the correct test string.", "subnode1", subnodes.get(0).get("testString"));
		assertEquals("Second subnode didn't return the correct test string.", "subnode2", subnodes.get(1).get("testString"));
	}
	
	@Test
	public void testListOfIntegerNodesAttribute() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		@SuppressWarnings("unchecked")
		List<Node> subnodes = (List<Node>) node.get("integerSubnodes");
		assertEquals("Number of subnodes isn't correct", 2, subnodes.size());
		assertEquals("First subnode didn't return the correct test string.", 1, subnodes.get(0).get("valueInteger"));
		assertEquals("Second subnode didn't return the correct test string.", 2, subnodes.get(1).get("valueInteger"));
	}
	
	@Test(expected=UnknownAttributeException.class)
	public void testUnknownAttribute() throws IOException, JSONException {
		SimpleJsonNode node = getSimpleNode(JSON_FILE_NAME);
		node.get("unknownAttribute");
	}
	
	
	public static class TestNodeType implements NodeType {

		TestAttributesDefinition attributesDefinitions = new TestAttributesDefinition();
		
		@Override
		public NodeTypeInfo getNodeTypeInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void init(NodeTypeInfo nodeTypeInfo) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public AttributesDefinition getAttributeDefinitions() {
			return attributesDefinitions;
		}

		@Override
		public boolean isSubTypeOf(NodeType parentNodeType) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean equals(Object arg0) {
			return arg0.getClass().equals(TestNodeType.class);
		}
		
	}
	
	private static class TestAttributesDefinition implements AttributesDefinition {

		@Override
		public AttributeDefinition getAttributeDefinition(String propertyName) {
			
			if(propertyName.endsWith("String")) {
				return new TestAttributeDefinition(AttributeType.STRING);
			} else if(propertyName.endsWith("Integer")) {
				return new TestAttributeDefinition(AttributeType.INTEGER);
			} else if(propertyName.endsWith("Boolean")) {
				return new TestAttributeDefinition(AttributeType.BOOLEAN);
			} else if("image".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.BLOB);
			} else if("text".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.CLOB);
			} else if("testAbsoluteReference".equals(propertyName) || "testRelativeReference".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.NODE_REFERENCE);
			} else if("nodetype".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.NODETYPE_REFERENCE);
			} else if("node".equals(propertyName) || "testNullNode".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.NODE, "test.nodetype");
			} else if("node-default".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.NODE);
			} else if("subnodes".equals(propertyName) || "testNullSubnodes".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.LIST_OF_NODES);
			} else if("integerSubnodes".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.LIST_OF_NODES, "test.nodetype");
			}
			return null;
		}

		@Override
		public String[] getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static class TestAttributeDefinition implements AttributeDefinition {

		AttributeType type;
		
		String nodetype;
		
		public TestAttributeDefinition(AttributeType attributetype) {
			type = attributetype;
		}
		
		public TestAttributeDefinition(AttributeType string, String typeofnode) {
			type = string;
			nodetype = typeofnode;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public NodeType getNodeType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPath() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object get(String attributeName) {
			if("typeofnode".equals(attributeName) || "typeofsubnodes".equals(attributeName)) {
				return nodetype;
			}
			return null;
		}

		@Override
		public AttributeType getAttributeType() {
			return type;
		}
		
	}
	
	private class TestBranchAccess extends SimpleBranchAccess {

		public TestBranchAccess() {
			super(new TestSession(), null);
		}

		@Override
		public Node getNode(String path) {
			try {
				return getSimpleNode(path);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private class TestSession extends SimpleCRSession {

		TestContentRepository repository;
		
		public TestSession() {
			super(null, null);
			try {
				repository = new TestContentRepository();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public SimpleFileContentRepository getContentRepository() {
			return repository;
		}
		
	}
	
	
	private class TestContentRepository extends SimpleFileContentRepository {
		public TestContentRepository() throws FileNotFoundException{
			super(REPOSITORY_DIR);
		}
	}
}