package com.dc2f.contentrepository.filejson;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributeType;
import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.Authentication;
import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRAdapter;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.NodeTypeInfo;

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


	private SimpleJsonNode getSimpleNode(String file) throws IOException, JSONException {
		
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
			
			SimpleJsonNode node = new SimpleJsonNode(braccess, "/", object, type);
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
		assertEquals("Class attribute was not read correctly.", "org.json.JSONObject", node.get("class"));
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
			
			if("testString".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.STRING);
			} else if("testInteger".equals(propertyName) || "testStringInteger".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.INTEGER);
			} else if("testBoolean".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.BOOLEAN);
			} else if("image".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.BLOB);
			} else if("text".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.CLOB);
			} else if("testAbsoluteReference".equals(propertyName) || "testRelativeReference".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.NODE_REFERENCE);
			} else if("nodetype".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.NODETYPE_REFERENCE);
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
		
		public TestAttributeDefinition(AttributeType string) {
			type = string;
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
			// TODO Auto-generated method stub
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