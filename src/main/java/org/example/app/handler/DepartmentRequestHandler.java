package org.example.app.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.dao.DepartmentDao;
import org.example.app.dao.DepartmentDaoImpl;
import org.example.app.model.Department;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entry point for Department API Gateway
 */
public class DepartmentRequestHandler {

    private DepartmentDao departmentDao;

    public DepartmentDao getDepartmentDao() {
        if (this.departmentDao == null) {
            this.departmentDao = new DepartmentDaoImpl();
        }
        return this.departmentDao;
    }

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    public APIGatewayProxyResponseEvent testLambda(APIGatewayProxyRequestEvent request, Context context) {

        Department department = new Department();
        department.setDepartmentId("001");
        department.setDepartmentCode("AAA");
        department.setDepartmentName("Computer Science");

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(department);
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(jsonInString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }

    public APIGatewayProxyResponseEvent listDepartments(APIGatewayProxyRequestEvent request, Context context) {
        List<Department> departments = this.getDepartmentDao().listDepartments();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(departments);

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");

            return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(jsonInString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }

    }

    public APIGatewayProxyResponseEvent createDepartment(APIGatewayProxyRequestEvent request, Context context) {
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        Department department;

        try {
            department = mapper.readValue(body, Department.class);
            this.getDepartmentDao().saveDepartment(department);
            return new APIGatewayProxyResponseEvent().withStatusCode(201);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }

    public APIGatewayProxyResponseEvent getDepartment(APIGatewayProxyRequestEvent request, Context context) {
        String departmentId = request.getPathParameters().get("id");
        Department department = this.getDepartmentDao().getDepartment(departmentId);

        if (department != null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(404);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(department);

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(jsonInString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }

    public APIGatewayProxyResponseEvent updateDepartment(APIGatewayProxyRequestEvent request, Context context) {
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        Department department;

        try {
            department = mapper.readValue(body, Department.class);
            this.getDepartmentDao().saveDepartment(department);
            return new APIGatewayProxyResponseEvent().withStatusCode(200);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return  new APIGatewayProxyResponseEvent().withStatusCode(500);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return  new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }

    public APIGatewayProxyResponseEvent deleteDepartment(APIGatewayProxyRequestEvent request, Context context) {
        String departmentId = request.getPathParameters().get("id");
        this.getDepartmentDao().deleteDepartment(departmentId);

        return new APIGatewayProxyResponseEvent().withStatusCode(200);
    }
}
