package com.mycompany.client;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import java.util.List;

@Path("employees")
interface EmployeeService extends RestService {

    @GET
    void getEmployees(MethodCallback<List<Employee>> callback);

    @DELETE
    void deleteEmployee(final Employee employee, final MethodCallback<Void> callback);

    @PUT
    void addEmployee(final Employee employee, final MethodCallback<Void> callback);
}
