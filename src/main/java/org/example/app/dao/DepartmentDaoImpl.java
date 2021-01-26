package org.example.app.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.example.app.model.Department;

import java.util.List;
import java.util.UUID;

public class DepartmentDaoImpl implements DepartmentDao {

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public DepartmentDaoImpl() {
        this.client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(this.client);
    }

    @Override
    public List<Department> listDepartments() {
        return this.mapper.scan(Department.class, new DynamoDBScanExpression());
    }

    @Override
    public Department getDepartment(String id) {
        return this.mapper.load(Department.class, id);
    }

    @Override
    public void saveDepartment(Department department) {

        if(department.getDepartmentId() == null) {
            department.setDepartmentId(UUID.randomUUID().toString());
        }
        this.mapper.save(department);
    }

    @Override
    public void deleteDepartment(String id) {
        Department department = this.getDepartment(id);
        if(department != null) {
            this.mapper.delete(department);
        }
    }
}
