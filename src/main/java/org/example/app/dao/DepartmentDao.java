package org.example.app.dao;

import org.example.app.model.Department;

import java.util.List;

public interface DepartmentDao {

    public List<Department> listDepartments();

    public Department getDepartment(String id);

    public void saveDepartment(Department department);

    public void deleteDepartment(String id);
}
