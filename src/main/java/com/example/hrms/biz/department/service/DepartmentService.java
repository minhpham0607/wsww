package com.example.hrms.biz.department.service;

import com.example.hrms.biz.department.model.Department;
import com.example.hrms.biz.department.model.criteria.DepartmentCriteria;
import com.example.hrms.biz.department.model.dto.DepartmentDTO;
import com.example.hrms.biz.department.repository.DepartmentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public List<Department> findById(Long id) {
        List<Department> departments = departmentMapper.findById(id);
        if (departments.isEmpty()) {
            System.out.println("Department not found: " + id);
        }
        return departments;
    }

    public void insert(DepartmentDTO.Req req) {
        // Kiểm tra trùng tên phòng ban
        if (departmentMapper.countByName(req.getDepartmentName()) > 0) {
            throw new IllegalArgumentException("Department name already exists");
        }

        // Tạo đối tượng Department từ DTO request
        Department department = new Department();
        department.setDepartmentName(req.getDepartmentName());

        // Chèn vào cơ sở dữ liệu
        departmentMapper.insertDepartment(department);

        // Kiểm tra xem departmentId có được gán hay không (nếu cần)
        if (department.getDepartmentId() == null) {
            throw new RuntimeException("Failed to insert department");
        }
    }


    public void updateDepartment(Long id, DepartmentDTO.Req req) {
        if (findById(id).isEmpty()) {
            throw new RuntimeException("Department not found");
        }
        // Kiểm tra tên phòng ban có trùng nhưng loại trừ chính nó
        if (departmentMapper.countByNameExcludingId(req.getDepartmentName(), id) > 0) {
            throw new RuntimeException("Department name already exists");
        }
        Department department = req.toDepartment();
        department.setDepartmentId(id);
        departmentMapper.updateDepartment(department);
    }

    public void deleteDepartment(Long departmentId) {
        if (findById(departmentId).isEmpty()) {
            throw new RuntimeException("Department not found");
        }
        departmentMapper.deleteDepartment(departmentId);
    }

    public int count(DepartmentCriteria criteria) {
        return departmentMapper.count(criteria);
    }

    public List<DepartmentDTO.Resp> list(DepartmentCriteria criteria) {
        List<Department> departments = departmentMapper.filterByCriteria(criteria);
        return departments.stream().map(DepartmentDTO.Resp::toResponse).toList();
    }

    public List<Department> getAllDepartments() {
        return departmentMapper.filterByCriteria(new DepartmentCriteria());
    }
    public List<String> findAllDepartmentNames() {
        return departmentMapper.findAllDepartmentNames();
    }
}