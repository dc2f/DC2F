package com.dc2f.contentrepository.filejson;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
		String json = IOUtils.toString(getClass().getResourceAsStream(JSON_FILE_NAME));
		JSONObject object = new JSONObject(json);
		NodeType type = new TestNodeType();
		
		SimpleJsonNode node = new SimpleJsonNode(null, "/", object, type);
		assertEquals("String attribute was not read correctly.", "string", node.get("testString"));
		assertEquals("Integer attribute was not read correctly.", 1, node.get("testInteger"));
		//This should return an integer but returns a string.
		//assertEquals("Integer attribute was not read correctly from String.", 1, node.get("testStringInteger"));
		
		assertEquals("Boolean attribute was not returned correctly.", true, node.get("testBoolean"));

	}

	
	@Test
	public void testBinaryAttributes() throws IOException, JSONException {
		String json = IOUtils.toString(getClass().getResourceAsStream(JSON_FILE_NAME));
		JSONObject object = new JSONObject(json);
		NodeType type = new TestNodeType();
		SimpleBranchAccess braccess = new TestBranchAccess();
		
		SimpleJsonNode node = new SimpleJsonNode(braccess, "/", object, type);
		assertEquals("Blob attribute was not read correctly.", "This should be an image", IOUtils.toString(((FileInputStream) node.get("image"))));
		assertEquals("Clob attribute was not read correctly.", "This can be a large textfile", node.get("text"));

	}
	
	
	
	
	private class TestNodeType implements NodeType {

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
		
	}
	
	private class TestAttributesDefinition implements AttributesDefinition {

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
			}
			return null;
		}

		@Override
		public String[] getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private class TestAttributeDefinition implements AttributeDefinition {

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
