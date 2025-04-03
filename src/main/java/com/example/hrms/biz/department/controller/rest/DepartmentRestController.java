package com.example.hrms.biz.department.controller.rest;

import com.example.hrms.biz.department.model.Department;
import com.example.hrms.biz.department.model.criteria.DepartmentCriteria;
import com.example.hrms.biz.department.model.dto.DepartmentDTO;
import com.example.hrms.biz.department.service.DepartmentService;
import com.example.hrms.common.http.model.Result;
import com.example.hrms.common.http.model.ResultData;
import com.example.hrms.common.http.model.ResultPageData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name = "Department API v1")
@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentRestController {

    private final DepartmentService departmentService;

    public DepartmentRestController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "List departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get success",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DepartmentDTO.Resp.class))) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content) })

    @GetMapping("")
    public ResultPageData<List<DepartmentDTO.Resp>> list(DepartmentCriteria criteria) {
        int total = departmentService.count(criteria);
        ResultPageData<List<DepartmentDTO.Resp>> response = new ResultPageData<>(criteria, total);
        response.setResultData(total > 0 ? departmentService.list(criteria) : Collections.emptyList());
        return response;
    }

    @Operation(summary = "Get all department names")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @GetMapping("/employee")
    public List<String> getAllDepartmentNames() {
        return departmentService.findAllDepartmentNames();
    }

    @Operation(summary = "Get all departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Department.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)
    })

    @GetMapping("/all")
    public ResultData<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return new ResultData<>(Result.SUCCESS, "Departments retrieved successfully", departments);
    }


    @Operation(summary = "Get department by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Department.class)) }),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public List<Department> getDepartmentById(@PathVariable Long id) {
        List<Department> departments = departmentService.findById(id);
        if (departments.isEmpty()) {
            throw new RuntimeException("Department not found");
        }
        return departments;
    }

    @Operation(summary = "Create department")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Department created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Result.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content) })
    @PostMapping("")
    public Result createDepartment(@RequestBody DepartmentDTO.Req departmentReq) {
        departmentService.insert(departmentReq);
        return new Result("Success", "Department created successfully.");
    }


    @Operation(summary = "Update department")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Result.class)) }),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content) })

    @PutMapping("/{id}")
    public Result updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO.Req departmentReq) {
        departmentService.updateDepartment(id, departmentReq);
        return new Result("Success", "Department updated successfully.");
    }


    @Operation(summary = "Delete department")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Result.class)) }),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content) })

    @DeleteMapping("/{id}")
    public Result deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new Result("Success", "Department deleted successfully.");
    }
}