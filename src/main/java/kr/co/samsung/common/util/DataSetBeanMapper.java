package kr.co.samsung.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Component;

import com.nexacro.xapi.data.DataSet;
import com.nexacro.xapi.data.DataSetList;
import com.nexacro.xapi.data.DataTypes;
import com.nexacro.xapi.data.PlatformData;
import com.nexacro.xapi.data.Variable;
import com.nexacro.xapi.data.VariableList;
import com.nexacro.xapi.tx.HttpPlatformResponse;
import com.nexacro.xapi.tx.PlatformType;
import kr.co.samsung.com.system.to.BindCodeBean;
import kr.co.samsung.common.annotation.Column;
import kr.co.samsung.common.annotation.Dataset;
import kr.co.samsung.common.annotation.Remove;
import kr.co.samsung.log.base.to.BindLogCdBean;
import kr.co.samsung.log.base.to.OutLogCdBean;

/**
 * @Package  com.bank.his.common.util
 * @Class    DataSetBeanMapper.java
 * @Create   2018. 05. 10.
 * @Author   허민준
 * @Description
 * 
 * @LastUpdated 
 *     
 */
@Component
public class DataSetBeanMapper {

	//test
	public void sendError(HttpServletResponse response,PlatformData outData,int ErrorCode,String ErrorMsg) throws Exception{
		
		HttpPlatformResponse httpPlatformResponse = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
    	
		 VariableList variableList=outData.getVariableList();
		 
		 
		variableList.add("ErrorCode", ErrorCode); 
    	variableList.add("ErrorMsg",  ErrorMsg);
    	 outData.setVariableList(variableList);
    	 
    	 httpPlatformResponse.setData(outData);
         httpPlatformResponse.sendData();
    	
	}
    public <T> List<T> datasetToBeans(PlatformData inData, Class<T> classType) throws Exception {
        String datasetName = getDataSetName(classType);
        DataSet dataset = inData.getDataSet(datasetName);

        List<T> beanList = new ArrayList<T>();
        T bean = null;
        int rowCount = dataset.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            bean = getBean(dataset, classType, rowIndex);
            beanList.add(bean);
        }

        rowCount = dataset.getRemovedRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            bean = getDeletedBean(dataset, classType, rowIndex);
            beanList.add(bean);
        }
        return beanList;
    }

    public <T> T datasetToBean(PlatformData inData, Class<T> classType) throws Exception {
        T bean = null;
        String datasetName = getDataSetName(classType);
        DataSet dataset = inData.getDataSet(datasetName);

        if (dataset.getRemovedRowCount() == 0)
            bean = getBean(dataset, classType, 0);
        else
            bean = getDeletedBean(dataset, classType, 0);
        return bean;
    }

    public <T> void beansToDataset(PlatformData outData, List<T> beanList, Class<T> classType)
            throws Exception {
        Map<String, String> nameMap = new HashMap<String, String>();

        DataSetList datasetList = outData.getDataSetList();
        String datasetName = getDataSetName(classType);
        DataSet dataset = new DataSet(datasetName);
        datasetList.add(dataset);

        Method[] methods = classType.getDeclaredMethods();
        for (Method method : methods)
            setColumnName(dataset, nameMap, method);
        for (T bean : beanList)
            setColumnValue(dataset, nameMap, bean);
    }
    
    public <T> void beansToDataset(PlatformData outData, List<T> beanList, String datasetName, Class<T> classType)
            throws Exception {
        Map<String, String> nameMap = new HashMap<String, String>();

        DataSetList datasetList = outData.getDataSetList();
        DataSet dataset = new DataSet(datasetName);
        datasetList.add(dataset);

        Method[] methods = classType.getDeclaredMethods();
        for (Method method : methods)
            setColumnName(dataset, nameMap, method);
        for (T bean : beanList)
            setColumnValue(dataset, nameMap, bean);
    }
   

    public <T> void beanToDataset(PlatformData outData, T bean, Class<T> classType)
            throws Exception {
        Map<String, String> nameMap = new HashMap<String, String>();
        DataSetList datasetList = outData.getDataSetList();

        String datasetName = getDataSetName(classType);
        DataSet dataset = new DataSet(datasetName);

        datasetList.add(dataset);

        if (bean != null) {
            Method[] methods = classType.getDeclaredMethods();
            for (Method method : methods)
                setColumnName(dataset, nameMap, method);
            setColumnValue(dataset, nameMap, bean);
        }
    }

    public void mapToDataset(PlatformData outData, List<Map<String, Object>> mapList,
            String datasetName) throws Exception {
        DataSetList datasetList = outData.getDataSetList();
        DataSet dataset = new DataSet(datasetName);
        datasetList.add(dataset);

        for (String key : mapList.get(0).keySet()) {
            String columnName = key.toLowerCase();
            dataset.addColumn(columnName, DataTypes.STRING, 256);
        }

        int rowIndex = 0;
        for (Map<String, Object> map : mapList) {
            rowIndex = dataset.newRow();
            for (String key : map.keySet()) {
                Object columnValue = map.get(key);
                dataset.set(rowIndex, key.toLowerCase(), columnValue);
            }
        }
    }

    public List<Map<String, Object>> datasetToMap(PlatformData inData, String datasetName)
            throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();

        DataSet dataset = inData.getDataSet(datasetName);
        int rowCount = dataset.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", dataset.getRowTypeName(rowIndex));
           

            for (int colIndex = 0; colIndex < dataset.getColumnCount(); colIndex++) {
                String key = dataset.getColumn(colIndex).getName();
                Object value = dataset.getObject(rowIndex, key);
                // map.put(formattingToCamel(key), value);
                map.put(formattingToNormal(key), value);
            }
            mapList.add(map);
        }

        rowCount = dataset.getRemovedRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", dataset.getRowTypeName(rowIndex));

            for (int colIndex = 0; colIndex < dataset.getColumnCount(); colIndex++) {
                String key = dataset.getColumn(colIndex).getName();
                Object value = dataset.getObject(rowIndex, key);
                // map.put(formattingToCamel(key), value);
                map.put(formattingToNormal(key), value);
            }
            mapList.add(map);
        }
        return mapList;
    }

    public Map<String, String> variablesToMap(PlatformData inData) throws Exception {
        Map<String, String> argsMap = new HashMap<>();
        int varCount = inData.getVariableCount();
        for (int index = 0; index < varCount; index++) {
            Variable variable = inData.getVariable(index);
            String key = variable.getName();
            String value = variable.getString().trim();
            System.out.println("key : "+key+"    ,      value : "+value);
            if (!"".equalsIgnoreCase(value) && !"undefined".equalsIgnoreCase(value)
                    && !"null".equalsIgnoreCase(value) && !"NaN".equalsIgnoreCase(value)) {
                argsMap.put(key, value);
            }
        }
        return argsMap;
    }
    public Map<String, Object> procedureVariablesToMap(PlatformData inData) throws Exception {
        Map<String, Object> procedureMap = new HashMap<>();
        int varCount = inData.getVariableCount();
        for (int index = 0; index < varCount; index++) {
            Variable variable = inData.getVariable(index);
            String key = variable.getName();
            String value = variable.getString().trim();
            System.out.println("key : "+key+"    ,      value : "+value);
            if (!"".equalsIgnoreCase(value) && !"undefined".equalsIgnoreCase(value)
                    && !"null".equalsIgnoreCase(value) && !"NaN".equalsIgnoreCase(value)) {
                procedureMap.put(key, value);
            }
        }
        procedureMap.put("errorCode", "");
        procedureMap.put("errorMsg", "");
        return procedureMap;
    }
    public void addVariable(PlatformData outData, String name, String value) {
        VariableList variableList = outData.getVariableList();
        Variable variable = new Variable(name);
        variable.set(value);
        variableList.add(variable);
    }

    private <T> String getDataSetName(Class<T> classType) {
        if (classType.isAnnotationPresent(Dataset.class))
            return classType.getAnnotation(Dataset.class).name();
        else
            return "ds" + classType.getName().replace("Bean", "");
    }

    private String getColumnName(Method method) {
        String columnName = null;
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Column) {
                String annotaionName = ((Column) annotation).name();
                columnName = annotaionName;
            }
        }
        if (annotations.length == 0) {
            // columnName = formattingToSnake(method.getName());
            columnName = formattingToNormal(method.getName());
        }
        return columnName;
    }

    private void setColumnName(DataSet dataset, Map<String, String> nameMap, Method method) {
        if (method.getName().startsWith("get")) {
            Column column = method.getAnnotation(Column.class);
            Remove remove = method.getAnnotation(Remove.class);

            if (column != null) {
                dataset.addColumn(column.name(), getDataType(method));
                nameMap.put(column.name(), method.getName());
            } else if (column == null && remove == null) {
                // String columnName = formattingToSnake(method.getName());
                String columnName = formattingToNormal(method.getName());
                dataset.addColumn(columnName, getDataType(method));
                nameMap.put(columnName, method.getName());
            }
        }
    }

    private <T> void setColumnValue(DataSet dataset, Map<String, String> nameMap, T bean)
            throws Exception {
        int rowIndex = dataset.newRow();

        for (String columnName : nameMap.keySet()) {
            String methodName = nameMap.get(columnName);

            Method method = bean.getClass().getDeclaredMethod(methodName);
            Object value = method.invoke(bean);
            dataset.set(rowIndex, columnName, value);
        }
    }

    private <T> T getBean(DataSet dataset, Class<T> classType, int rowIndex) throws Exception {
        T bean = classType.newInstance();
        Method[] methods = classType.getDeclaredMethods();
        Method statusMethod = classType.getMethod("setStatus", String.class);
        
        statusMethod.invoke(bean, dataset.getRowTypeName(rowIndex));

        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String columnName = getColumnName(method);
                if (columnName != null) {
                    Object columnValue = dataset.getObject(rowIndex, columnName);
                    if (columnValue != null)
                        method.invoke(bean, columnValue);
                }
            }
        }
        return bean;
    }

    private <T> T getDeletedBean(DataSet dataset, Class<T> classType, int rowIndex)
            throws Exception {
        T bean = classType.newInstance();
        Method[] methods = classType.getDeclaredMethods();
        Method statusMethod = classType.getMethod("setStatus", String.class);
        statusMethod.invoke(bean, DataSet.ROW_TYPE_NAME_DELETED);

        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String columnName = getColumnName(method);
                if (columnName != null) {
                    Object columnValue = dataset.getRemovedData(rowIndex, columnName);
                    if (columnValue != null)
                        method.invoke(bean, columnValue);
                }
            }
        }
        return bean;
    }

    private int getDataType(Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType == Date.class)
            return DataTypes.DATE;
        else if (returnType == String.class)
            return DataTypes.STRING;
        else if (returnType == int.class || returnType == boolean.class)
            return DataTypes.INT;
        else if (returnType == BigDecimal.class)
            return DataTypes.BIG_DECIMAL;
        else if (returnType == double.class)
            return DataTypes.DOUBLE;
        else if (returnType == byte[].class)
            return DataTypes.BLOB;
        else
            return DataTypes.NULL;
    }

    private String formattingToNormal(String name) {
        if (name.startsWith("set") || name.startsWith("get"))
            name = name.substring(3);
        String normalString = name.substring(0, 1).toLowerCase() + name.substring(1);
        return normalString;
    }

    @SuppressWarnings("unused")
    private String formattingToSnake(String name) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";

        if (name.startsWith("set") || name.startsWith("get"))
            name = name.substring(3);
        String snakeString = name.replaceAll(regex, replacement).toLowerCase();
        return snakeString;
    }

    @SuppressWarnings("unused")
    private String formattingToCamel(String name) {
        if (name.startsWith("set") || name.startsWith("get"))
            name = name.substring(3);
        String camelString = WordUtils.capitalizeFully(name, new char[] { '_' }).replaceAll("_",
                "");
        camelString = camelString.substring(0, 1).toLowerCase() + camelString.substring(1);
        return camelString;
    }

    @Deprecated
    public <T> void bindCode(PlatformData outData, Map<String, List<T>> bindCodeMap,
            List<BindCodeBean> bindCodeList, Class<T> classType) throws Exception {
        DataSetList dataSetList = outData.getDataSetList();
        for (BindCodeBean bcBean : bindCodeList) {
            Map<String, String> nameMapper = new HashMap<String, String>();
            String dataSetName = bcBean.getDsName();
            DataSet dataSet = new DataSet(dataSetName);
            dataSetList.add(dataSet);
            Method[] methods = classType.getDeclaredMethods();
            for (Method method : methods) {
                setColumnName(dataSet, nameMapper, method);
            }
            List<T> beanList = bindCodeMap.get(bcBean.getDsName());
            for (T bean : beanList) {
                setColumnValue(dataSet, nameMapper, bean);
            }
        }
    }

    @Deprecated
    public <T> void bindLogCd(PlatformData outData, Map<String, List<OutLogCdBean>> bindLogCdMap,
            List<BindLogCdBean> bindLogCdList, Class<OutLogCdBean> class1) throws Exception {
        DataSetList dataSetList = outData.getDataSetList();
        for (BindLogCdBean blcdBean : bindLogCdList) {
            Map<String, String> nameMapper = new HashMap<String, String>();
            String dataSetName = blcdBean.getDsLogName();
            DataSet dataSet = new DataSet(dataSetName);
            dataSetList.add(dataSet);
            Method[] methods = class1.getDeclaredMethods();
            for (Method method : methods) {
                setColumnName(dataSet, nameMapper, method);
            }
            List<T> beanList = (List<T>) bindLogCdMap.get(blcdBean.getDsLogName());
            for (T bean : beanList) {
                setColumnValue(dataSet, nameMapper, bean);
            }
        }
    }

    @Deprecated
    public <T> void loginDataset(PlatformData outData, T bean, Class<T> classType)
            throws Exception {
        Map<String, String> nameMapper = new HashMap<>();
        DataSetList dataSetList = outData.getDataSetList();
        DataSet dataSet = new DataSet("gdsEmp");
        if (bean != null) {
            dataSetList.add(dataSet);
            Method[] methods = classType.getDeclaredMethods();
            for (Method method : methods)
                setColumnName(dataSet, nameMapper, method);
        }
        setColumnValue(dataSet, nameMapper, bean);

    }

}
