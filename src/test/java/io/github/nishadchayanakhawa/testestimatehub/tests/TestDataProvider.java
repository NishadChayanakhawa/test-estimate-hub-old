package io.github.nishadchayanakhawa.testestimatehub.tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import io.nishadc.automationtestingframework.filehandling.JsonFileHandling;
import io.nishadc.automationtestingframework.filehandling.exceptions.FlatFileHandlingException;
import io.nishadc.automationtestingframework.filehandling.exceptions.JsonFileHandlingException;

public class TestDataProvider {
	private static final String TEST_DATA_BASE_LOCATION=
			"./src/test/resources/TestData/";
	private static final String TEST_DATA_JSON_KEY=
			"testData";
	
	private static String getClassName(ITestNGMethod testNGMethod) {
		return testNGMethod.getRealClass().getSimpleName();
	}
	
	private static String getMethodName(ITestNGMethod testNGMethod) {
		return testNGMethod.getMethodName();
	}
	
	private static Object[][] toObjectArray(JSONObject json) throws JsonFileHandlingException, JSONException {
		JSONArray jsonArray;
		try {
			jsonArray=json.getJSONArray(TestDataProvider.TEST_DATA_JSON_KEY);
		} catch(JSONException e) {
			throw (JsonFileHandlingException)new JsonFileHandlingException("Incorrect test data json file format: " + e.getMessage()).initCause(e);
		}
		Object[][] objectArray=new Object[jsonArray.length()][1];
		for(int iArrayCounter=0;iArrayCounter<jsonArray.length();iArrayCounter++) {
			JSONObject testDataRowJson=jsonArray.getJSONObject(iArrayCounter);
			objectArray[iArrayCounter][0]=testDataRowJson;
		}
		return objectArray;
	}
	
	@DataProvider(name="getTestDataFromJson",parallel=true)
	public Object[][] getTestDataFromJson(ITestNGMethod testNGMethod) throws FlatFileHandlingException, JsonFileHandlingException, JSONException {
		String className=TestDataProvider.getClassName(testNGMethod);
		String methodName=TestDataProvider.getMethodName(testNGMethod);
		
		String jsonTestDataFileFullPath=
				String.format("%s%s/%s.json", TestDataProvider.TEST_DATA_BASE_LOCATION,className,methodName);
		JSONObject testDataJson=null;
		try {
			testDataJson = JsonFileHandling.getJsonFileContent(jsonTestDataFileFullPath);
			return TestDataProvider.toObjectArray(testDataJson);
		} catch (FlatFileHandlingException e) {
			throw e;
		} catch (JsonFileHandlingException e) {
			throw e;
		}
	}
}
