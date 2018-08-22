package com.lf.ninghaisystem.bean.entity;

/**
 * Created by admin on 2017/11/24.
 */

public class DutyPDetail {

    private int employeeId;
    private String phone;
    private String area;
    private String department;
    private String representativeTeam;
    private String representativeType;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRepresentativeTeam() {
        return representativeTeam;
    }

    public void setRepresentativeTeam(String representativeTeam) {
        this.representativeTeam = representativeTeam;
    }

    public String getRepresentativeType() {
        return representativeType;
    }

    public void setRepresentativeType(String representativeType) {
        this.representativeType = representativeType;
    }
}
