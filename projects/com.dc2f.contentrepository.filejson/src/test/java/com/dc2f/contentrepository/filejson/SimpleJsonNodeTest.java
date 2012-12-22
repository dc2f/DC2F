package com.dc2f.contentrepository.filejson;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributeType;
import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.NodeTypeInfo;

public class SimpleJsonNodeTest {
	
	@Test
	public void testSimpleJsonNode() throws IOException, JSONException {
		String json = IOUtils.toString(getClass().getResourceAsStream("test.json"));
		JSONObject object = new JSONObject(json);
		NodeType type = new TestNodeType();
		
		SimpleJsonNode node = new SimpleJsonNode(null, getClass().getResource("test.json").getPath(), object, type);
		assertEquals("String attribute was not read correctly.", "string", node.get("testString"));
		assertEquals("Integer attribute was not read correctly.", 1, node.get("testInteger"));
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
			} else if("testInteger".equals(propertyName)) {
				return new TestAttributeDefinition(AttributeType.INTEGER);
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
}
